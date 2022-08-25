package model;

import controller.CommandPromptType;
import helper.Parser;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import javax.imageio.ImageIO;


/**
 * Represents rooms and characters in the game.
 * Implements Space interface.
 */
public class WorldModelImpl implements WorldModel {

  private String name;
  private int width;
  private int height;
  private final int wallThickness;

  private int curTurn;
  private int curPlayerIdx;
  private int maxTurn;
  private int maxItemNumCarried;
  private TargetCharacter targetCharacter;
  private Pet pet;
  private final StringBuilder lastTurnInfo;
  private final StringBuilder resultInfo;

  private final List<Room> roomList;
  private final List<Item> itemList;
  private final List<Player> playerList;
  private final Map<Integer, List<Integer>> playerItemTable;
  private final Map<Integer, Set<Integer>> roomPlayerTable;
  private final Map<Integer, List<Integer>> roomAdjacentTable;
  private final Map<Integer, Set<Integer>> roomItemTable;
  private final Stack<Integer> petDfsStack;
  private final Set<Integer> petDfsVisitedStack;

  private String currentConfig;

  /**
   * Constructor of the World class.
   *
   * @param readable a readable which contains information of the world specification
   */
  public WorldModelImpl(Readable readable) throws IllegalArgumentException {
    if (readable == null) {
      throw new IllegalArgumentException("readable invalid");
    }

    name = null;
    width = 0;
    height = 0;
    wallThickness = 1;

    curTurn = 1;
    curPlayerIdx = 0;
    maxTurn = 0;
    maxItemNumCarried = 0;

    targetCharacter = null;
    pet = null;

    roomList = new ArrayList<>();
    itemList = new ArrayList<>();
    playerList = new ArrayList<>();
    lastTurnInfo = new StringBuilder();
    resultInfo = new StringBuilder();
    playerItemTable = new Hashtable<>();
    roomPlayerTable = new Hashtable<>();
    roomAdjacentTable = new Hashtable<>();
    roomItemTable = new Hashtable<>();
    petDfsStack = new Stack<>();
    petDfsVisitedStack = new HashSet<>();

    currentConfig = null;

    reinitializeWithNewConfig(readable);
  }

  private void resetAll() {
    name = null;
    width = 0;
    height = 0;

    curPlayerIdx = 0;
    curTurn = 1;
    maxTurn = 0;
    maxItemNumCarried = 0;
    targetCharacter = null;
    pet = null;

    roomList.clear();
    itemList.clear();
    playerList.clear();
    playerItemTable.clear();
    roomPlayerTable.clear();
    roomAdjacentTable.clear();
    roomItemTable.clear();
    petDfsStack.clear();
    petDfsVisitedStack.clear();

    lastTurnInfo.delete(0, resultInfo.length());
    resultInfo.delete(0, resultInfo.length());
  }

  @Override
  public void reinitializeWithCurrentConfig() {
    if (currentConfig == null) {
      throw new IllegalStateException("currentConfig not initialized");
    }
    initialize(currentConfig);
  }

  @Override
  public void reinitializeWithNewConfig(Readable readable) {
    if (readable == null) {
      throw new IllegalArgumentException("readable invalid");
    }
    StringBuilder sb = new StringBuilder();
    Scanner scanner = new Scanner(readable);
    while (scanner.hasNextLine()) {
      sb.append(scanner.nextLine());
      sb.append(System.lineSeparator());
    }
    initialize(sb.toString());
  }

  private void initialize(String config) {
    if (config == null) {
      throw new IllegalArgumentException("sb invalid");
    }

    int worldColumn = 0;
    int worldRow = 0;
    String worldName = null;
    TargetCharacter targetCharacter = null;
    Pet pet = null;
    List<Room> roomList = new ArrayList<>();
    List<Item> itemList = new ArrayList<>();


    BufferedReader reader = new BufferedReader(new StringReader(config));

    int currenLine;
    int leastElement;
    String line;

    //read world's basic information
    currenLine = 1;
    leastElement = 3;
    try {
      line = reader.readLine();
      if (line != null) {
        line = line.trim();
        String[] split = line.split("\\s+");
        if (split.length < leastElement) {
          throw new IllegalArgumentException(String.format("need at least %d elements in line %d",
                  leastElement, currenLine));
        }
        worldRow = Parser.getInstance().getIntFromString(split[0]);
        worldColumn = Parser.getInstance().getIntFromString(split[1]);
        worldName = Parser.getInstance().concatStringWithSpace(split, 2);
        if (worldRow <= 0) {
          throw new IllegalArgumentException("world row should be positive");
        }
        if (worldColumn <= 0) {
          throw new IllegalArgumentException("world column should be positive");
        }
        if (worldName == null || worldName.trim().isEmpty()) {
          throw new IllegalArgumentException("world name invalid");
        }
      }
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }


    //read target character's information
    currenLine++;
    leastElement = 2;
    try {
      line = reader.readLine();
      if (line != null) {
        line = line.trim();
        String[] split = line.split("\\s+");
        if (split.length < leastElement) {
          throw new IllegalArgumentException(String.format(
                  "need at least %d elements in line %d",
                  leastElement, currenLine));
        }
        targetCharacter =
                new TargetCharacterImpl(Parser.getInstance().concatStringWithSpace(split, 1),
                        Parser.getInstance().getIntFromString(split[0]), 0
                );
      }
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }

    //read pet's information
    currenLine++;
    try {
      line = reader.readLine();
      if (line != null) {
        line = line.trim();
        pet = new PetImpl(0, line.trim());
      }
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }


    //read rooms' information
    currenLine++;
    try {
      line = reader.readLine();
      if (line != null) {
        line = line.trim();
        int roomNumber = Parser.getInstance().getIntFromString(line);
        leastElement = 5;
        for (int it = 0; it < roomNumber; ++it) {
          currenLine++;
          line = reader.readLine();
          if (line != null) {
            line = line.trim();
            String[] split = line.split("\\s+");
            if (split.length < leastElement) {
              throw new IllegalArgumentException(String.format(
                      "need at least %d elements in line %d", leastElement, currenLine));
            }
            int[] pos = new int[leastElement - 1];
            for (int it2 = 0; it2 < leastElement - 1; ++it2) {
              pos[it2] = Parser.getInstance().getIntFromString(split[it2]);
            }
            String name = Parser.getInstance().concatStringWithSpace(split, leastElement - 1);
            Room room = new RoomImpl(it, name, new int[]{pos[0], pos[1]},
                    new int[]{pos[2], pos[3]});
            for (int i = 0; i < roomList.size(); ++i) {
              if (RoomImpl.isRoomOverlapped(roomList.get(i), room)) {
                throw new IllegalArgumentException(String.format(
                        "room %s and room %s overlap!",
                        roomList.get(i).toString(), room.toString()));
              }
            }
            roomList.add(room);
          }
        }
      }
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }


    //read items' information
    currenLine++;
    try {
      line = reader.readLine();
      if (line != null) {
        line = line.trim();
        int itemNumber = Parser.getInstance().getIntFromString(line);
        leastElement = 3;
        for (int it = 0; it < itemNumber; ++it) {
          currenLine++;
          line = reader.readLine();
          if (line != null) {
            line = line.trim();
            String[] split = line.split("\\s+");
            if (split.length < leastElement) {
              throw new IllegalArgumentException(String.format(
                      "need at least %d elements in line %d", leastElement, currenLine));
            }
            int roomIdx = Parser.getInstance().getIntFromString(split[0]);
            int attack = Parser.getInstance().getIntFromString(split[1]);
            String name = Parser.getInstance().concatStringWithSpace(split, leastElement - 1);
            Item item = new ItemImpl(it, name, attack, roomIdx);
            itemList.add(item);
          }
        }
      }
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }

    for (int i = 0; i < itemList.size(); ++i) {
      if (itemList.get(i).getRoomIdx() >= roomList.size()) {
        throw new IllegalStateException(String.format(
                "item %s has an invalid room index %d",
                itemList.get(i).getName(), itemList.get(i).getRoomIdx()));
      }
    }

    for (int i = 0; i < roomList.size(); ++i) {
      if (roomList.get(i).getIndex() >= roomList.size()) {
        throw new IllegalStateException(String.format(
                "room %s has an invalid index %d", roomList.get(i).getName(),
                roomList.get(i).getIndex()));
      }
    }

    resetAll();
    currentConfig = config;
    this.name = worldName;
    this.width = worldColumn + 1;
    this.height = worldRow + 1;
    this.roomList.addAll(roomList);
    this.itemList.addAll(itemList);
    this.targetCharacter = targetCharacter;
    this.pet = pet;

    initRoomItemTable();
    initAdjacentRoomTable();
  }

  @Override
  public void setPlayers(List<String> names, List<Integer> rooms, List<Boolean> isAi)
          throws IllegalArgumentException {
    if (names == null || rooms == null || isAi == null) {
      throw new IllegalArgumentException("parameter is null");
    }
    if (names.isEmpty() || rooms.isEmpty() || isAi.isEmpty()) {
      throw new IllegalArgumentException("list is empty");
    }
    if (names.size() != rooms.size() || names.size() != isAi.size()) {
      throw new IllegalArgumentException("list is not of the same length");
    }

    for (int i = 0; i < names.size(); ++i) {
      var curName = names.get(i);
      if (curName == null || curName.isBlank() || curName.isEmpty()) {
        throw new IllegalArgumentException("player name can't be empty or null");
      }
      for (int j = 0; j < i; ++j) {
        if (names.get(j).equals(curName)) {
          throw new IllegalArgumentException("player names can't be the same");
        }
      }
      var curRoom = rooms.get(i);
      if (curRoom < 0 || curRoom >= roomList.size()) {
        throw new IllegalArgumentException(String.format(
                "room index should be between 0 and %d", roomList.size() - 1));
      }
    }

    playerList.clear();
    this.playerItemTable.clear();
    this.roomPlayerTable.clear();
    for (int i = 0; i < names.size(); ++i) {
      Player player = null;
      if (isAi.get(i)) {
        player = new PlayerAiImpl(names.get(i), rooms.get(i), new AiActionControllerImpl());
      } else {
        player = new PlayerHumanImpl(names.get(i), rooms.get(i));
      }
      playerList.add(player);
    }
    for (int i = 0; i < playerList.size(); ++i) {
      if (!this.roomPlayerTable.containsKey(playerList.get(i).getRoomIdx())) {
        this.roomPlayerTable.put(playerList.get(i).getRoomIdx(), new HashSet<>());
      }
      this.roomPlayerTable.get(playerList.get(i).getRoomIdx()).add(i);
    }
  }

  @Override
  public void setPlayers(List<Player> players) throws IllegalArgumentException {
    if (players == null) {
      throw new IllegalArgumentException("parameter is null");
    }
    if (players.isEmpty()) {
      throw new IllegalArgumentException("list is empty");
    }
    playerList.clear();
    playerList.addAll(players);
    this.playerItemTable.clear();
    this.roomPlayerTable.clear();
    for (int i = 0; i < playerList.size(); ++i) {
      if (!this.roomPlayerTable.containsKey(playerList.get(i).getRoomIdx())) {
        this.roomPlayerTable.put(playerList.get(i).getRoomIdx(), new HashSet<>());
      }
      this.roomPlayerTable.get(playerList.get(i).getRoomIdx()).add(i);
    }
  }

  @Override
  public void setMaxTurn(int turn) throws IllegalArgumentException {
    if (turn < 1) {
      throw new IllegalArgumentException("max turn less than one");
    }
    this.maxTurn = turn;
  }

  @Override
  public void setMaxItemCarried(int maxItemCarried) throws IllegalArgumentException {
    if (maxItemCarried > itemList.size() || maxItemCarried < 1) {
      throw new IllegalArgumentException("max item number out of range");
    }
    this.maxItemNumCarried = maxItemCarried;
  }

  @Override
  public AiActionParam aiPlayerDoAction() throws IllegalStateException {
    if (isGameOver()) {
      throw new IllegalStateException("game over!");
    }
    if (getCurPlayerType() != PlayerType.AI) {
      throw new IllegalStateException("current player isn't AI!");
    }
    var curPlayer = playerList.get(curPlayerIdx);
    AiActionParam ac = null;
    var aiPlayer = (PlayerAi) curPlayer;

    //attack first
    if (aiPlayer.getRoomIdx() == targetCharacter.getRoomIdx()
            && !curPlayerSeenExceptPetRoomPlayer()) {
      StringBuilder sb = new StringBuilder();
      var idx = getCurrentPlayerMaxAttackItemIdx();
      if (idx >= 0) {
        curPlayerAttemptUseItemAttack(idx);
        ac = new AiActionParamImpl(PlayerActionType.ATTACK, idx);
      } else {
        curPlayerAttemptPokeEyes();
        ac = new AiActionParamImpl(PlayerActionType.ATTACK, -1);
      }
      return ac;
    }

    //other actions
    if (aiPlayer.usePredefinedAction()) {
      ac = aiPlayer.getPredefinedAction();
    } else {
      while (true) {
        PlayerActionType playerActionType = ((PlayerAi) curPlayer).getRandomAction();
        boolean validAction = true;
        switch (playerActionType) {
          case PICKUP_ITEM: {
            if (playerItemTable.containsKey(curPlayerIdx)
                    && playerItemTable.get(curPlayerIdx).size() >= maxItemNumCarried) {
              continue;
            }
            var roomIdx = getCurPlayerRoomIdx();
            if (!roomItemTable.containsKey(roomIdx) || roomItemTable.get(roomIdx).size() == 0) {
              validAction = false;
              continue;
            }
            int itemIdx = aiPlayer.getRandomNumber(roomItemTable.get(roomIdx));
            ac = new AiActionParamImpl(PlayerActionType.PICKUP_ITEM, itemIdx);
          }
          break;
          case MOVE: {
            var oldRoom = getCurPlayerRoomIdx();
            var adjacentRooms = getRoomNeighbors(oldRoom);
            if (adjacentRooms.size() == 0) {
              validAction = false;
              continue;
            }
            int roomIdx = aiPlayer.getRandomNumber(roomAdjacentTable.get(oldRoom));
            ac = new AiActionParamImpl(PlayerActionType.MOVE, roomIdx);
          }
          break;
          case MOVE_PET: {
            while (true) {
              int roomIdx = aiPlayer.getRandomNumberBetween(0, roomList.size());
              if (roomIdx != pet.getRoomIdx()) {
                ac = new AiActionParamImpl(PlayerActionType.MOVE_PET, roomIdx);
                break;
              }
            }
          }
          break;
          case LOOK_AROUND: {
            ac = new AiActionParamImpl(PlayerActionType.LOOK_AROUND, 0);
          }
          break;
          default:
            break;
        }
        if (validAction) {
          break;
        }
      }
    }

    switch (ac.getActionType()) {
      case MOVE:
        curPlayerMoveToNeighborRoom(ac.getActionParameter());
        break;
      case PICKUP_ITEM:
        curPlayerPickUpItem(ac.getActionParameter());
        break;
      case LOOK_AROUND:
        curPlayerDisplayNeighbor();
        break;
      case MOVE_PET:
        curPlayerMovePet(ac.getActionParameter());
        break;
      default:
        break;
    }
    return ac;
  }

  @Override
  public int getPlayerNumber() {
    return playerList.size();
  }

  @Override
  public String getPlayerName(int playerIdx) {
    if (playerIdx < 0 || playerIdx >= playerList.size()) {
      throw new IllegalArgumentException("player index invalid");
    }
    return playerList.get(playerIdx).getName();
  }

  @Override
  public int getTargetCharacterRoomIdx() {
    return targetCharacter.getRoomIdx();
  }

  @Override
  public int getCurPlayerIdx() {
    return curPlayerIdx;
  }

  @Override
  public List<Integer> getCurPlayerCarryItems() {
    if (playerItemTable.containsKey(curPlayerIdx)) {
      return playerItemTable.get(curPlayerIdx);
    }
    return null;
  }

  @Override
  public int getItemAttack(int itemIdx) {
    if (itemIdx < 0 || itemIdx >= itemList.size()) {
      throw new IllegalArgumentException("item index invalid");
    }
    return itemList.get(itemIdx).getAttack();
  }

  @Override
  public String getItemName(int itemIdx) {
    if (itemIdx < 0 || itemIdx >= itemList.size()) {
      throw new IllegalArgumentException("item index invalid");
    }
    return itemList.get(itemIdx).getName();
  }

  @Override
  public int getPlayerRoomIdx(int playerIdx) {
    if (playerIdx < 0 || playerIdx >= playerList.size()) {
      throw new IllegalArgumentException("player index invalid");
    }
    return playerList.get(playerIdx).getRoomIdx();
  }

  @Override
  public int[] getRoomLeftTopCorner(int roomIdx) {
    if (roomIdx < 0 || roomIdx >= roomList.size()) {
      throw new IllegalArgumentException("room index invalid");
    }
    return roomList.get(roomIdx).getLeftTopCorner();
  }

  @Override
  public int[] getRoomRightBottomCorner(int roomIdx) {
    if (roomIdx < 0 || roomIdx >= roomList.size()) {
      throw new IllegalArgumentException("room index invalid");
    }
    return roomList.get(roomIdx).getRightBottomCorner();
  }

  @Override
  public String getCurTurnInfo() {
    if (isGameOver()) {
      throw new IllegalStateException("game over!");
    }
    var roomIdx = getCurPlayerRoomIdx();
    List<Integer> items = getRoomItems(roomIdx);
    StringBuilder sb = new StringBuilder();
    sb.append(String.format(
            CommandPromptType.TURN_INFO.toString(),
            curTurn, getCurPlayerName(), curPlayerIdx,
            getCurPlayerName(), getCurPlayerItemNum(),
            getCurPlayerRoomIdx(), getRoomName(getCurPlayerRoomIdx())));
    sb.append(printRoomInfo(roomIdx));
    return sb.toString();
  }

  @Override
  public void curHumanPlayerMovePet(int roomIdx)
          throws IllegalStateException, IllegalArgumentException {
    if (isGameOver()) {
      throw new IllegalStateException("game over!");
    }

    if (getCurPlayerType() != PlayerType.HUMAN) {
      throw new IllegalStateException("current player isn't Human!");
    }

    if (roomIdx < 0 || roomIdx > roomList.size()) {
      throw new IllegalArgumentException("invalid roomIdx!");
    }
    curPlayerMovePet(roomIdx);
  }

  private void curPlayerMovePet(int roomIdx) {
    pet.moveTo(roomIdx);
    resetPetWanderDfs();
    finishCurTurn(String.format("Pet %s is moved to %d-indexed room %s by player %s.\n",
            pet.getName(), roomIdx, roomList.get(roomIdx).getName(), getCurPlayerName()));
  }

  private void resetPetWanderDfs() {
    petDfsStack.clear();
    petDfsVisitedStack.clear();
  }

  private void petWanderDfs() {
    if (petDfsStack.empty()) {
      if (petDfsVisitedStack.size() > 0) {
        petDfsVisitedStack.clear();
      }
      int roomIdx = pet.getRoomIdx();
      petDfsVisitedStack.add(roomIdx);
      List<Integer> neighbors = getRoomNeighbors(roomIdx);
      for (int i = neighbors.size() - 1; i >= 0; i--) {
        if (!petDfsVisitedStack.contains(neighbors.get(i))) {
          petDfsStack.add(neighbors.get(i));
        }
      }
    }
    int roomIdx = petDfsStack.pop();
    pet.moveTo(roomIdx);
    petDfsVisitedStack.add(roomIdx);
    List<Integer> neighbors = getRoomNeighbors(roomIdx);
    for (int i = neighbors.size() - 1; i >= 0; i--) {
      if (!petDfsVisitedStack.contains(neighbors.get(i))
              && !petDfsStack.contains(neighbors.get(i))) {
        petDfsStack.add(neighbors.get(i));
      }
    }
  }

  @Override
  public void curHumanPlayerAttackTargetCharacter(int itemIdx)
          throws IllegalStateException, IllegalArgumentException {
    if (isGameOver()) {
      throw new IllegalStateException("game over!");
    }

    if (getCurPlayerType() != PlayerType.HUMAN) {
      throw new IllegalStateException("current player isn't Human!");
    }

    if (itemIdx == -1) {
      curPlayerAttemptPokeEyes();
    } else if (itemIdx >= 0 && itemIdx < itemList.size()) {
      curPlayerAttemptUseItemAttack(itemIdx);
    } else {
      throw new IllegalArgumentException("invalid itemIdx!");
    }
  }

  private void curPlayerAttemptPokeEyes() {
    StringBuilder sb = new StringBuilder();
    if (!curPlayerSeenExceptPetRoomPlayer() && !curPlayerSeenByPetRoomPlayer()) {
      targetCharacter.attacked(1);
      sb.append(String.format("Player %s pokes %s's eyes successfully.\n",
              getCurPlayerName(), targetCharacter.getName()));
      sb.append(String.format(
              "Target character's health, reduced by %d points, now is %d.\n",
              1, targetCharacter.getHealth()));
    } else {
      sb.append(String.format("Player %s's attack attempt failed for being seen by others.\n",
              getCurPlayerName()));
    }
    finishCurTurn(sb.toString());
  }

  private void curPlayerAttemptUseItemAttack(int itemIdx) {
    StringBuilder sb = new StringBuilder();
    if (playerItemTable.containsKey(curPlayerIdx) && playerItemTable.get(curPlayerIdx)
            .contains(itemIdx)) {
      String itemName = itemList.get(itemIdx).getName();

      if (!curPlayerSeenByPetRoomPlayer() && !curPlayerSeenExceptPetRoomPlayer()) {
        targetCharacter.attacked(itemList.get(itemIdx).getAttack());
        sb.append(String.format("Player %s attempts attack with item %s successfully.\n",
                getCurPlayerName(), itemName));
        sb.append(String.format(
                "Target character's health, reduced by %d points, now is %d.\n",
                itemList.get(itemIdx).getAttack(), targetCharacter.getHealth()));
      } else {
        sb.append(String.format("Player %s's attack attempt failed for being seen by others.\n",
                getCurPlayerName()));
      }
      removeUsedItem(itemIdx);
      sb.append(String.format("Item %s is removed from the game.\n",
              itemList.get(itemIdx).getName()));
      finishCurTurn(sb.toString());
    } else {
      throw new IllegalArgumentException("item doesn't belong to current player!");
    }
  }

  private void removeUsedItem(int itemIdx) {
    if (playerItemTable.containsKey(curPlayerIdx) && playerItemTable.get(curPlayerIdx)
            .contains(itemIdx)) {
      Item it = itemList.get(itemIdx);
      it.setUsed();
      playerItemTable.get(curPlayerIdx).remove(Integer.valueOf(itemIdx));
    }
  }

  private int getCurrentPlayerMaxAttackItemIdx() {
    List<Integer> items = playerItemTable.get(curPlayerIdx);
    if (items == null || items.size() == 0) {
      return -1;
    } else {
      int highestAttack = 0;
      int retIdx = 0;
      for (var i : items) {
        Item it = itemList.get(i);
        if (it.hasBeenUsed()) {
          continue;
        }
        if (it.getAttack() > highestAttack) {
          highestAttack = it.getAttack();
          retIdx = i;
        }
      }
      return retIdx;
    }
  }

  private boolean curPlayerSeenExceptPetRoomPlayer() {
    var roomIdx = getCurPlayerRoomIdx();
    var players = roomPlayerTable.get(roomIdx);
    if (players.size() > 1) {
      return true;
    }
    if (pet.getRoomIdx() == roomIdx) {
      return false;
    }
    var neighbor = getRoomNeighbors(roomIdx);
    if (neighbor != null && neighbor.size() > 0) {
      for (var n : neighbor) {
        if (pet.getRoomIdx() == n) {
          continue;
        }
        players = roomPlayerTable.get(n);
        if (players != null && players.size() >= 1) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean curPlayerSeenByPetRoomPlayer() {
    var roomIdx = getCurPlayerRoomIdx();
    var petRoomIdx = pet.getRoomIdx();
    if (RoomImpl.isRoomAdjacent(roomList.get(roomIdx), roomList.get(petRoomIdx), 1)) {
      if (roomPlayerTable.containsKey(petRoomIdx)) {
        Set<Integer> players = roomPlayerTable.get(petRoomIdx);
        if (players.size() > 0) {
          return true;
        }
        return false;
      } else {
        return false;
      }
    } else if (roomIdx == petRoomIdx) {
      return roomPlayerTable.containsKey(roomIdx) && roomPlayerTable.get(roomIdx).size() > 1;
    } else {
      return false;
    }
  }

  /**
   * Get the max turn allowed for the game.
   *
   * @return max turn allowed for the game
   */
  @Override
  public int getMaxTurn() {
    return maxTurn;
  }

  /**
   * Get the max item carried allowed for the game.
   *
   * @return max item carried allowed for the game
   */
  @Override
  public int getMaxItemNumCarried() {
    return maxItemNumCarried;
  }

  /**
   * Get the name of current player.
   *
   * @return name of current player
   */
  @Override
  public String getCurPlayerName() {
    return playerList.get(curPlayerIdx).getName();
  }

  /**
   * Get the type of current player.
   *
   * @return type of current player
   */
  @Override
  public PlayerType getCurPlayerType() {
    return playerList.get(curPlayerIdx).getPlayerType();
  }


  /**
   * Get the room index current player is in.
   *
   * @return room index current player is in
   */
  @Override
  public int getCurPlayerRoomIdx() {
    return playerList.get(curPlayerIdx).getRoomIdx();
  }

  /**
   * Get the item number current player carries.
   *
   * @return item number current player carries
   */
  @Override
  public int getCurPlayerItemNum() {
    if (playerItemTable.containsKey(curPlayerIdx)) {
      return playerItemTable.get(curPlayerIdx).size();
    }
    return 0;
  }

  /**
   * Get the room name of given index.
   *
   * @param roomIdx index of the room
   * @return room name of given index
   * @throws IllegalArgumentException roomIdx is negative or exceeds max room index
   */
  @Override
  public String getRoomName(int roomIdx) throws IllegalArgumentException {
    if (roomIdx < 0 || roomIdx >= roomList.size()) {
      throw new IllegalArgumentException("room index can't be negative or exceed max room index!");
    }
    return roomList.get(roomIdx).getName();
  }

  /**
   * Get the information of player of given order.
   *
   * @param playerIdx order of the player
   * @return information of player
   * @throws IllegalArgumentException playerIdx is negative or exceeds max player index
   */
  @Override
  public String printPlayerInfo(int playerIdx) throws IllegalArgumentException {
    if (playerIdx < 0 || playerIdx >= playerList.size()) {
      throw new IllegalArgumentException("invalid player index!");
    }
    StringBuilder sb = new StringBuilder();
    sb.append(playerList.get(playerIdx).toString());
    if (playerItemTable.containsKey(playerIdx) && playerItemTable.get(playerIdx).size() > 0) {
      sb.append(String.format("Carried %d item(s):\n", playerItemTable.get(playerIdx).size()));
      for (var idx : playerItemTable.get(playerIdx)) {
        Item item = itemList.get(idx);
        sb.append(item.toString());
      }
    } else {
      sb.append("No item carried.\n");
    }
    return sb.toString();
  }


  /**
   * Moves current human player.
   *
   * @param roomIdx index of the room that current player will move to
   * @throws IllegalArgumentException roomIdx isn't neighbor of current room of current player,
   *                                  roomIdx is negative or exceeds max room index,
   *                                  or is equal to current room index
   * @throws IllegalStateException    game ends or current player isn't human player
   */
  @Override
  public void curHumanPlayerMove(int roomIdx)
          throws IllegalArgumentException, IllegalStateException {
    if (isGameOver()) {
      throw new IllegalStateException("game over!");
    }

    if (getCurPlayerType() != PlayerType.HUMAN) {
      throw new IllegalStateException("current player isn't Human!");
    }

    if (roomIdx < 0 || roomIdx >= roomList.size()) {
      throw new IllegalArgumentException("invalid roomIdx index!");
    }

    var oldRoom = getCurPlayerRoomIdx();
    if (roomIdx == oldRoom) {
      throw new IllegalArgumentException("equal to current room!");
    }
    var adjacentRooms = getRoomNeighbors(oldRoom);
    if (!adjacentRooms.contains(roomIdx)) {
      throw new IllegalArgumentException("not neighbor!");
    }
    curPlayerMoveToNeighborRoom(roomIdx);
  }


  /**
   * Display information of neighbor rooms of the room current player is in.
   *
   * @throws IllegalStateException game ends
   */
  @Override
  public void curHumanPlayerDisplayNeighborRooms()
          throws IllegalArgumentException, IllegalStateException {
    if (isGameOver()) {
      throw new IllegalStateException("game over!");
    }

    if (getCurPlayerType() != PlayerType.HUMAN) {
      throw new IllegalStateException("current player isn't Human!");
    }

    curPlayerDisplayNeighbor();
  }

  /**
   * Current human player picks up the item of given index.
   *
   * @param itemIdx index of the item
   * @throws IllegalArgumentException itemIdx isn't in the room current player is in
   * @throws IllegalStateException    game ends, or current player isn't human,
   *                                  or the player's item number has reached the limit
   */
  @Override
  public void curHumanPlayerPickupItem(int itemIdx)
          throws IllegalArgumentException, IllegalStateException {
    if (isGameOver()) {
      throw new IllegalStateException("game over!");
    }

    if (getCurPlayerType() != PlayerType.HUMAN) {
      throw new IllegalStateException("current player isn't Human!");
    }
    if (playerItemTable.containsKey(curPlayerIdx)
            && playerItemTable.get(curPlayerIdx).size() >= maxItemNumCarried) {
      throw new IllegalStateException("max items carried!");
    }
    var roomIdx = getCurPlayerRoomIdx();
    if (!roomItemTable.containsKey(roomIdx) || !roomItemTable.get(roomIdx).contains(itemIdx)) {
      throw new IllegalStateException("item isn't in the room!");
    }

    if (roomItemTable.containsKey(roomIdx) && roomItemTable.get(roomIdx).contains(itemIdx)) {
      curPlayerPickUpItem(itemIdx);
    }
  }

  private void curPlayerPickUpItem(int itemIdx) {
    var roomIdx = getCurPlayerRoomIdx();
    if (!playerItemTable.containsKey(curPlayerIdx)) {
      playerItemTable.put(curPlayerIdx, new ArrayList<>());
    }
    playerItemTable.get(curPlayerIdx).add(itemIdx);
    roomItemTable.get(roomIdx).remove(itemIdx);
    if (roomItemTable.get(roomIdx).size() == 0) {
      roomItemTable.remove(roomIdx);
    }
    finishCurTurn(String.format(
            "%d-indexed Player %s picks up %d-indexed item %s (atk:%d) from %d-indexed room %s\n",
            curPlayerIdx, playerList.get(curPlayerIdx).getName(), itemIdx,
            itemList.get(itemIdx).getName(), itemList.get(itemIdx).getAttack(),
            roomIdx, roomList.get(roomIdx).getName()));
  }

  private void curPlayerMoveToNeighborRoom(int roomIdx) {
    var oldRoom = getCurPlayerRoomIdx();
    if (roomPlayerTable.containsKey(oldRoom)) {
      roomPlayerTable.get(oldRoom).remove(curPlayerIdx);
    }
    if (!roomPlayerTable.containsKey(roomIdx)) {
      roomPlayerTable.put(roomIdx, new HashSet<>());
    }
    roomPlayerTable.get(roomIdx).add(curPlayerIdx);
    playerList.get(curPlayerIdx).moveTo(roomIdx);

    finishCurTurn(String.format(
            "%d-indexed Player %s moves from %d-indexed room %s to %d-indexed room %s.\n",
            curPlayerIdx, playerList.get(curPlayerIdx).getName(), oldRoom,
            roomList.get(oldRoom).getName(), roomIdx, roomList.get(roomIdx).getName()));
  }

  private String curPlayerDisplayNeighbor() throws IllegalArgumentException {
    var roomIdx = getCurPlayerRoomIdx();
    var neighbors = roomAdjacentTable.get(roomIdx);
    if (neighbors == null || neighbors.size() == 0) {
      finishCurTurn("No neighbor rooms.\n");
      return getLastTurnMsg();
    }
    StringBuilder sb = new StringBuilder();
    sb.append("The room current player is in:\n");
    sb.append(String.format("Room index:%d. Name:%s.\n",
            roomList.get(roomIdx).getIndex(), roomList.get(roomIdx).getName()));
    sb.append(printRoomInfo(roomIdx));
    sb.append("Neighbor rooms:\n");
    for (var r : neighbors) {
      sb.append(String.format("Room index:%d. Name:%s.\n",
              roomList.get(r).getIndex(), roomList.get(r).getName()));
      if (r != pet.getRoomIdx()) {
        sb.append(printRoomInfo(r));
      }
    }
    finishCurTurn(sb.toString());
    return getLastTurnMsg();
  }

  private void finishCurTurn(String str) {
    lastTurnInfo.delete(0, lastTurnInfo.length());
    lastTurnInfo.append(str);
    var oldRoom = targetCharacter.getRoomIdx();
    if (targetCharacter.getHealth() > 0) {
      moveTargetCharacter();
      lastTurnInfo.append(String.format(
              "Target player moves from %d-indexed room to %d-indexed room.\n",
              oldRoom, targetCharacter.getRoomIdx()));
    }
    petWanderDfs();
    //    System.out.println(String.format("Pet %s moves to %d-indexed room.\n",
    //            pet.getName(), pet.getRoomIdx()));
    lastTurnInfo.append(String.format("Round %d finish.\n", curTurn));
    if (targetCharacter.getHealth() == 0) {
      resultInfo.append(String.format("Target character is killed! Winner is %s.\n",
              getCurPlayerName()));
      return;
    }
    curTurn++;
    if (curTurn > maxTurn) {
      resultInfo.append(String.format("Target character slipped away! Nobody wins.\n"));
      return;
    }
    curPlayerIdx = (curPlayerIdx + 1) % playerList.size();
  }

  /**
   * Get the information of the previous turn.
   *
   * @return information of the previous turn
   */
  @Override
  public String getLastTurnMsg() {
    return lastTurnInfo.toString();
  }

  @Override
  public String getGameResultMsg() {
    if (!isGameOver()) {
      throw new IllegalStateException("game hasn't ended");
    }
    return resultInfo.toString();
  }

  /**
   * Whether the game ends.
   * When max turn is reached, game ends.
   *
   * @return true if game ends, false if not
   */
  @Override
  public boolean isGameOver() {
    return curTurn > maxTurn || targetCharacter.getHealth() == 0;
  }

  /**
   * Get the number of rooms.
   *
   * @return the number of rooms
   */
  @Override
  public int getRoomNumber() {
    return roomList.size();
  }

  /**
   * Get the number of items.
   *
   * @return the number of items
   */
  @Override
  public int getItemNumber() {
    return itemList.size();
  }

  /**
   * Get adjacent rooms' indexes of a given room.
   *
   * @param roomIdx target room's index
   * @return adjacent rooms' indexes of the target room
   * @throws IllegalArgumentException index is negative,or over the number of the list
   */
  @Override
  public List<Integer> getRoomNeighbors(int roomIdx) throws IllegalArgumentException {
    if (roomIdx < 0) {
      throw new IllegalArgumentException("room index shouldn't be negative");
    }
    if (roomIdx >= roomList.size()) {
      throw new IllegalArgumentException("room index shouldn't be bigger than rooms' total number");
    }
    List<Integer> ret = new ArrayList<>();
    if (roomAdjacentTable.containsKey(roomIdx)) {
      ret.addAll(roomAdjacentTable.get(roomIdx));
    }
    return ret;
  }

  private void initAdjacentRoomTable() {
    for (int i = 0; i < roomList.size(); ++i) {
      for (int j = i + 1; j < roomList.size(); ++j) {
        try {
          if (RoomImpl.isRoomAdjacent(roomList.get(i), roomList.get(j), wallThickness)) {
            if (!roomAdjacentTable.containsKey(i)) {
              roomAdjacentTable.put(i, new ArrayList<>());
            }
            roomAdjacentTable.get(i).add(j);

            if (!roomAdjacentTable.containsKey(j)) {
              roomAdjacentTable.put(j, new ArrayList<>());
            }
            roomAdjacentTable.get(j).add(i);
          }
        } catch (IllegalArgumentException ex) {
          throw new IllegalArgumentException(ex);
        }
      }
    }
  }

  /**
   * Get items in the room.
   *
   * @param roomIdx target room's index
   * @return items in the room
   * @throws IllegalArgumentException index is negative,or over the number of the list
   */
  @Override
  public List<Integer> getRoomItems(int roomIdx) throws IllegalArgumentException {
    if (roomIdx < 0) {
      throw new IllegalArgumentException("room index shouldn't be negative");
    }
    if (roomIdx >= roomList.size()) {
      throw new IllegalArgumentException("room index shouldn't be bigger than rooms' total number");
    }
    List<Integer> ret = new ArrayList<>();
    if (roomItemTable.containsKey(roomIdx)) {
      ret.addAll(roomItemTable.get(roomIdx));
    }
    return ret;
  }

  private void initRoomItemTable() {
    for (int i = 0; i < itemList.size(); ++i) {
      int roomIdx = itemList.get(i).getRoomIdx();
      if (!roomItemTable.containsKey(roomIdx)) {
        roomItemTable.put(roomIdx, new HashSet<>());
      }
      roomItemTable.get(roomIdx).add(i);
    }
  }

  private void moveTargetCharacter() {
    int idx = targetCharacter.getRoomIdx() + 1;
    if (idx == roomList.size()) {
      idx = 0;
    }
    targetCharacter.moveTo(idx);
  }

  /**
   * Print the information of the room with given index.
   *
   * @param roomIdx the index of the room
   * @throws IllegalArgumentException roomIdx is negative or over the number of the list
   */
  @Override
  public String printRoomInfo(int roomIdx) throws IllegalArgumentException {
    if (roomIdx < 0) {
      throw new IllegalArgumentException("room index shouldn't be negative");
    }
    if (roomIdx >= roomList.size()) {
      throw new IllegalArgumentException("room index shouldn't be bigger than rooms' total number");
    }

    List<Integer> items = getRoomItems(roomIdx);
    StringBuilder sb = new StringBuilder();

    if (items != null && items.size() > 0) {
      sb.append(String.format("--%d items are in the room:\n", items.size()));
      for (Integer idx : items) {
        sb.append("\t");
        sb.append(itemList.get(idx));
      }
    }

    if (targetCharacter.getRoomIdx() == roomIdx) {
      sb.append("--Target character is in the room:\n");
      sb.append("\t");
      sb.append(targetCharacter);
    }

    if (pet.getRoomIdx() == roomIdx) {
      sb.append("--Pet is in the room:\n");
      sb.append("\t");
      sb.append(pet.getName());
      sb.append("\n");
    }

    Set<Integer> players = roomPlayerTable.get(roomIdx);
    if (players != null && players.size() > 0) {
      sb.append(String.format("--%d players are in the room:\n", players.size()));
      for (Integer idx : players) {
        sb.append("\t");
        sb.append(playerList.get(idx).getName());
        sb.append("\n");
      }
    }

    List<Integer> adjacentRooms = getRoomNeighbors(roomIdx);
    if (adjacentRooms != null && adjacentRooms.size() > 0) {
      sb.append(String.format("--%d adjacent rooms:\n", adjacentRooms.size()));
      for (Integer idx : adjacentRooms) {
        sb.append("\t");
        sb.append(String.format("Room index:%d. Name:%s.\n",
                roomList.get(idx).getIndex(), roomList.get(idx).getName()));
      }
    }

    return sb.toString();
  }

  /**
   * Output the map of the rooms to a given image file.
   *
   * @param imgScale    how much should the map be scaled, expect an even integer
   * @param imgFilePath file path of the image
   * @throws IllegalArgumentException imgScale is less than one
   * @throws UncheckedIOException     deleting old file fails, or writing image to file fails
   */
  @Override
  public void outputMapImage(String imgFilePath, int imgScale)
          throws IllegalArgumentException, UncheckedIOException {
    if (imgScale <= 1) {
      throw new IllegalArgumentException("imgScale should be at least 2");
    }
    if (imgScale % 2 != 0) {
      throw new IllegalArgumentException("imgScale should be even");
    }
    if (imgFilePath == null || imgFilePath.isEmpty()) {
      throw new IllegalArgumentException("imgFilePath invalid");
    }

    int halfUnit = (int) (imgScale * 0.5f);
    int imgWidth = imgScale * width + halfUnit * 2;
    int imgHeight = imgScale * height + halfUnit * 2;
    BufferedImage img = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_4BYTE_ABGR);
    Graphics2D graphics2D = img.createGraphics();
    for (int i = 0; i < roomList.size(); ++i) {
      Room room = roomList.get(i);
      int[] leftTopCorner = room.getLeftTopCorner();
      //-1 for wall thickness
      //+halfUnit*2 for neighbor wall overlay
      int width = (room.getWidth() - 1) * imgScale + halfUnit * 2;
      int height = (room.getHeight() - 1) * imgScale + halfUnit * 2;
      int leftTopX = leftTopCorner[1] * imgScale + halfUnit;
      int leftTopY = leftTopCorner[0] * imgScale + halfUnit;
      graphics2D.setColor(Color.black);
      graphics2D.drawRect(leftTopX, leftTopY, width, height);
      graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
              RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
      graphics2D.setColor(Color.BLUE);
      Font font = new Font("Microsoft YaHei", Font.BOLD, imgScale / 3);
      graphics2D.setFont(font);

      FontMetrics metrics = graphics2D.getFontMetrics(font);
      int x = leftTopX + (width - metrics.stringWidth(room.getName())) / 2;
      int y = leftTopY + ((height - metrics.getHeight()) / 2) + metrics.getAscent();

      graphics2D.drawString(room.getName(), x, y);
    }
    graphics2D.dispose();
    Path path = Paths.get(imgFilePath);
    if (Files.exists(path)) {
      try {
        Files.delete(path);
      } catch (IOException ex) {
        throw new UncheckedIOException(ex);
      }
    }
    try {
      ImageIO.write(img, "png", new File(imgFilePath));
    } catch (IOException ex) {
      throw new UncheckedIOException(ex);
    }
  }

  /**
   * Get the width of the world.
   *
   * @return the width of the world
   */
  @Override
  public int getWidth() {
    return width;
  }

  /**
   * Get the height of the world.
   *
   * @return the height of the world
   */
  @Override
  public int getHeight() {
    return height;
  }

  /**
   * Get the name of the world.
   *
   * @return the name of the world
   */
  @Override
  public String getName() {
    return name;
  }
}
