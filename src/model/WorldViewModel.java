package model;

import java.util.List;


/**
 * Represents a single game of World.
 * The world consists of rooms, items, players and target player.
 */
public interface WorldViewModel {

  /**
   * Get the number of players.
   *
   * @return number of players
   */
  int getPlayerNumber();

  /**
   * Get the name of player with given index.
   * The index is determined by the player order.
   *
   * @param playerIdx index of the player in the player list
   * @return name of the player
   * @IllegalArgumentException playerIdx is out of range
   */
  String getPlayerName(int playerIdx);

  /**
   * Get the index of the room where the target character is in.
   *
   * @return index of the room
   */
  int getTargetCharacterRoomIdx();

  /**
   * Get the index of current player.
   * This should be called before current turn ends, or the index will be next player's index.
   * The index is determined by the player order.
   *
   * @return index of current player
   */
  int getCurPlayerIdx();

  /**
   * Get the list of items current player carries.
   *
   * @return list of items current player carries
   */
  List<Integer> getCurPlayerCarryItems();

  /**
   * Get the attack of the item with given index.
   *
   * @param itemIdx index of the item in the item list
   * @return attack of the item
   * @IllegalArgumentException itemIdx is out of range
   */
  int getItemAttack(int itemIdx);

  /**
   * Get the name of the item with given index.
   *
   * @param itemIdx index of the item in the item list
   * @return name of the item
   * @IllegalArgumentException itemIdx is out of range
   */
  String getItemName(int itemIdx);

  /**
   * Get the room index of the player with given index.
   *
   * @param playerIdx index of the player in the player list
   * @return room index of the player
   * @IllegalArgumentException playerIdx is out of range
   */
  int getPlayerRoomIdx(int playerIdx);

  /**
   * Get the left top corner's coordinate of the room with given index.
   *
   * @param roomIdx index of the room in the room list
   * @return left top corner's coordinate of the room
   * @IllegalArgumentException roomIdx is out of range
   */
  int[] getRoomLeftTopCorner(int roomIdx);

  /**
   * Get the right bottom corner's coordinate of the room with given index.
   *
   * @param roomIdx index of the room in the room list
   * @return right bottom corner's coordinate of the room
   * @IllegalArgumentException roomIdx is out of range
   */
  int[] getRoomRightBottomCorner(int roomIdx);

  /**
   * Get the information of current turn,
   * including the name of the space, the items in the space,
   * the players in the space,
   * whether the pet is in the space,
   * and what spaces are visible from the space.
   *
   * @return a string that shows information of current turn
   * @throws IllegalStateException game is over
   */
  String getCurTurnInfo() throws IllegalStateException;

  /**
   * Get the max turn allowed for the game.
   *
   * @return max turn allowed for the game
   */
  int getMaxTurn();

  /**
   * Get the max item carried allowed for the game.
   *
   * @return max item carried allowed for the game
   */
  int getMaxItemNumCarried();

  /**
   * Get the name of current player.
   *
   * @return name of current player
   */
  String getCurPlayerName();

  /**
   * Get the type of current player.
   *
   * @return type of current player
   */
  PlayerType getCurPlayerType();

  /**
   * Get the room index current player is in.
   *
   * @return room index current player is in
   */
  int getCurPlayerRoomIdx();

  /**
   * Get the item number current player carries.
   *
   * @return item number current player carries
   */
  int getCurPlayerItemNum();

  /**
   * Get the room name of given index.
   *
   * @param roomIdx index of the room
   * @return room name of given index
   * @throws IllegalArgumentException roomIdx is negative or exceeds max room index
   */
  String getRoomName(int roomIdx) throws IllegalArgumentException;

  /**
   * Get the information of player of given order.
   *
   * @param playerIdx order of the player
   * @return information of player
   * @throws IllegalArgumentException playerIdx is negative or exceeds max player index
   */
  String printPlayerInfo(int playerIdx) throws IllegalArgumentException;

  /**
   * Get the information of the previous turn.
   *
   * @return information of the previous turn
   */
  String getLastTurnMsg();

  /**
   * Get the game result.
   * Should be called after game ends.
   *
   * @return game result
   * @throws IllegalStateException game hasn't ended yet
   */
  String getGameResultMsg() throws IllegalStateException;

  /**
   * Whether the game ends.
   * When max turn is reached, game ends.
   *
   * @return true if game ends, false if not
   */
  boolean isGameOver();

  /**
   * Get the number of rooms.
   *
   * @return the number of rooms
   */
  int getRoomNumber();

  /**
   * Get the number of items.
   *
   * @return the number of items
   */
  int getItemNumber();

  /**
   * Get adjacent rooms' indexes of a given room.
   *
   * @param roomIdx target room's index
   * @return adjacent rooms' indexes of the target room
   * @throws IllegalArgumentException index is negative,or over the number of the list
   */
  List<Integer> getRoomNeighbors(int roomIdx) throws IllegalArgumentException;

  /**
   * Get items in the room.
   *
   * @param roomIdx target room's index
   * @return items in the room
   * @throws IllegalArgumentException index is negative,or over the number of the list
   */
  List<Integer> getRoomItems(int roomIdx) throws IllegalArgumentException;

  /**
   * Print the information of the room with given index.
   *
   * @param roomIdx the index of the room
   * @return information of the room
   * @throws IllegalArgumentException roomIdx is negative or over the number of the list
   */
  String printRoomInfo(int roomIdx) throws IllegalArgumentException;

  /**
   * Get the width of the world.
   *
   * @return the width of the world
   */
  int getWidth();

  /**
   * Get the height of the world.
   *
   * @return the height of the world
   */
  int getHeight();

  /**
   * Get the name of the world.
   *
   * @return the name of the world
   */
  String getName();

}
