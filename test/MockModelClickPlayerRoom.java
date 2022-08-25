import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import model.AiActionParam;
import model.Player;
import model.PlayerType;
import model.WorldModel;

/**
 * Mock WorldModel for testing controller.
 */
public class MockModelClickPlayerRoom implements WorldModel {
  private final StringBuilder log;
  private final int uniqueCode;

  /**
   * Constructor of MockModel.
   *
   * @param log        output information
   * @param uniqueCode mark this unique instance
   */
  public MockModelClickPlayerRoom(StringBuilder log, int uniqueCode) {
    this.log = log;
    this.uniqueCode = uniqueCode;
  }

  @Override
  public void reinitializeWithCurrentConfig() {
    log.append("reinitializeWithCurrentConfig: ").append(uniqueCode).append("\n");
  }

  @Override
  public void reinitializeWithNewConfig(Readable readable) throws IllegalArgumentException {
    log.append("reinitializeWithNewConfig: ").append(uniqueCode)
            .append(",").append(readable == null).append("\n");
  }

  @Override
  public void setPlayers(List<String> name, List<Integer> roomIdx, List<Boolean> isAi) {
    log.append("setPlayers: ").append(uniqueCode)
            .append(",").append(name).append(",").append(roomIdx).append(",").append(isAi)
            .append("\n");
  }

  @Override
  public void setPlayers(List<Player> players) throws IllegalArgumentException {
    log.append("setPlayers: ").append(uniqueCode)
            .append(",").append(players).append("\n");
  }

  @Override
  public void setMaxTurn(int turn) throws IllegalArgumentException {
    log.append("setMaxTurn: ").append(uniqueCode)
            .append(",").append(turn).append("\n");
  }

  @Override
  public void setMaxItemCarried(int maxItemCarried) throws IllegalArgumentException {
    log.append("setMaxItemCarried: ").append(uniqueCode)
            .append(",").append(maxItemCarried).append("\n");
  }

  @Override
  public void curHumanPlayerMovePet(int roomIdx) throws IllegalStateException,
          IllegalArgumentException {
    log.append("curHumanPlayerMovePet: ").append(uniqueCode)
            .append(",").append(roomIdx).append("\n");
  }

  @Override
  public void curHumanPlayerAttackTargetCharacter(int itemIdx) throws IllegalStateException,
          IllegalArgumentException {
    log.append("curHumanPlayerAttackTargetCharacter: ").append(uniqueCode)
            .append(",").append(itemIdx).append("\n");
  }

  @Override
  public AiActionParam aiPlayerDoAction() throws IllegalStateException {
    log.append("aiPlayerDoAction: ").append(uniqueCode).append("\n");

    return null;
  }

  @Override
  public void curHumanPlayerMove(int roomIdx) throws IllegalArgumentException,
          IllegalStateException {
    log.append("curHumanPlayerMove: ").append(roomIdx).append("\n");
  }

  @Override
  public void curHumanPlayerDisplayNeighborRooms() throws IllegalStateException {
    log.append("curHumanPlayerDisplayNeighborRooms: ").append(uniqueCode).append("\n");
  }

  @Override
  public void curHumanPlayerPickupItem(int itemIdx) throws IllegalArgumentException,
          IllegalStateException {
    log.append("curHumanPlayerPickupItem: ").append(uniqueCode).append(",")
            .append(itemIdx).append("\n");
  }

  @Override
  public void outputMapImage(String imgFilePath, int imgScale) throws IllegalArgumentException,
          UncheckedIOException {
    log.append("outputMapImage: ").append(uniqueCode).append(imgFilePath).append(",")
            .append(imgScale).append("\n");
  }

  @Override
  public int getPlayerNumber() {
    log.append("getPlayerNumber: ").append(uniqueCode).append("\n");
    return 2;
  }

  @Override
  public String getPlayerName(int playerIdx) {
    log.append("getPlayerName: ").append(uniqueCode).append(",").append(playerIdx).append("\n");
    return null;
  }

  @Override
  public int getTargetCharacterRoomIdx() {
    log.append("getTargetCharacterRoomIdx: ").append(uniqueCode).append("\n");

    return 0;
  }

  @Override
  public int getCurPlayerIdx() {
    log.append("getCurPlayerIdx: ").append(uniqueCode).append("\n");
    return 0;
  }

  @Override
  public List<Integer> getCurPlayerCarryItems() {
    log.append("getCurPlayerCarryItems: ").append(uniqueCode).append("\n");
    return null;
  }

  @Override
  public int getItemAttack(int itemIdx) {
    log.append("getItemAttack: ").append(uniqueCode).append(",").append(itemIdx).append("\n");
    return 0;
  }

  @Override
  public String getItemName(int itemIdx) {
    log.append("getItemName: ").append(uniqueCode).append(",").append(itemIdx).append("\n");
    return null;
  }

  @Override
  public int getPlayerRoomIdx(int playerIdx) {
    log.append("getPlayerRoomIdx: ").append(uniqueCode).append(",").append(playerIdx).append("\n");

    return 2;
  }

  @Override
  public int[] getRoomLeftTopCorner(int roomIdx) {
    log.append("getRoomLeftTopCorner: ").append(uniqueCode).append(",")
            .append(roomIdx).append("\n");

    return null;
  }

  @Override
  public int[] getRoomRightBottomCorner(int roomIdx) {
    log.append("getRoomRightBottomCorner: ").append(uniqueCode).append(",")
            .append(roomIdx).append("\n");

    return null;
  }

  @Override
  public String getCurTurnInfo() throws IllegalStateException {
    log.append("getCurTurnInfo: ").append(uniqueCode).append("\n");
    return null;
  }

  @Override
  public int getMaxTurn() {
    log.append("getMaxTurn: ").append(uniqueCode).append("\n");
    return 20;
  }

  @Override
  public int getMaxItemNumCarried() {
    log.append("getMaxItemNumCarried: ").append(uniqueCode).append("\n");
    return 1;
  }

  @Override
  public String getCurPlayerName() {
    log.append("getCurPlayerName: ").append(uniqueCode).append("\n");
    return null;
  }

  @Override
  public PlayerType getCurPlayerType() {
    log.append("getCurPlayerType: ").append(uniqueCode).append("\n");
    return PlayerType.HUMAN;
  }

  @Override
  public int getCurPlayerRoomIdx() {
    log.append("getCurPlayerRoomIdx: ").append(uniqueCode).append("\n");
    return 2;
  }

  @Override
  public int getCurPlayerItemNum() {
    log.append("getCurPlayerItemNum: ").append(uniqueCode).append("\n");
    return 0;
  }

  @Override
  public String getRoomName(int roomIdx) throws IllegalArgumentException {
    log.append("getRoomName: ").append(uniqueCode).append(",").append(roomIdx).append("\n");

    return null;
  }

  @Override
  public String printPlayerInfo(int playerIdx) throws IllegalArgumentException {
    log.append("printPlayerInfo: ").append(uniqueCode).append(",").append(playerIdx).append("\n");

    return "player 0 information";
  }

  @Override
  public String getLastTurnMsg() {
    log.append("getLastTurnMsg: ").append(uniqueCode).append("\n");
    return null;
  }

  @Override
  public String getGameResultMsg() {
    log.append("getGameResultMsg: ").append(uniqueCode).append("\n");
    return null;
  }

  @Override
  public boolean isGameOver() {
    log.append("isGameOver: ").append(uniqueCode).append("\n");
    return false;
  }

  @Override
  public int getRoomNumber() {
    log.append("getRoomNumber: ").append(uniqueCode).append("\n");
    return 20;
  }

  @Override
  public int getItemNumber() {
    log.append("getItemNumber: ").append(uniqueCode).append("\n");
    return 0;
  }

  @Override
  public List<Integer> getRoomNeighbors(int roomIdx) throws IllegalArgumentException {
    log.append("getRoomNeighbors: ").append(uniqueCode).append(",").append(roomIdx).append("\n");
    List<Integer> neighbors = new ArrayList<>();
    neighbors.add(1);
    return neighbors;
  }

  @Override
  public List<Integer> getRoomItems(int roomIdx) throws IllegalArgumentException {
    log.append("getRoomItems: ").append(uniqueCode).append(",").append(roomIdx).append("\n");
    List<Integer> items = new ArrayList<>();
    items.add(1);
    return items;
  }

  @Override
  public String printRoomInfo(int roomIdx) throws IllegalArgumentException {
    log.append("printRoomInfo: ").append(uniqueCode).append(",").append(roomIdx).append("\n");
    return null;
  }

  @Override
  public int getWidth() {
    log.append("getWidth: ").append(uniqueCode).append("\n");
    return 0;
  }

  @Override
  public int getHeight() {
    log.append("getHeight: ").append(uniqueCode).append("\n");
    return 0;
  }

  @Override
  public String getName() {
    log.append("getName: ").append(uniqueCode).append("\n");
    return null;
  }
}
