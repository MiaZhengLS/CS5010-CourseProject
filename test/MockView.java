import controller.WorldFeatureController;
import java.util.List;
import model.WorldViewModel;
import view.WorldView;

/**
 * Mock WorldView for testing controller.
 */
public class MockView implements WorldView {
  private final StringBuilder log;
  private final int uniqueCode;

  /**
   * Constructor of MockView.
   *
   * @param log output information
   * @param uniqueCode mark this unique instance
   */
  public MockView(StringBuilder log, int uniqueCode) {
    this.log = log;
    this.uniqueCode = uniqueCode;
  }

  @Override
  public void showTurnInfo(String str) {
    log.append("showTurnInfo: ").append(uniqueCode).append(",").append(str).append("\n");
  }

  @Override
  public void showTurnResult(String str) {
    log.append("showTurnResult: ").append(uniqueCode).append(",").append(str).append("\n");
  }

  @Override
  public void showPrompt(String str) {
    log.append("showPrompt: ").append(uniqueCode).append(",").append(str).append("\n");
  }

  @Override
  public void showWarning(String str) {
    log.append("showWarning: ").append(uniqueCode).append(",").append(str).append("\n");
  }

  @Override
  public void showGameResult(String str) {
    log.append("showWarning: ").append(uniqueCode).append(",").append(str).append("\n");
  }

  @Override
  public void movePlayerTo(int playerIdx, int roomIdx) throws IllegalArgumentException {
    log.append("movePlayerTo: ").append(uniqueCode).append(",")
            .append(playerIdx).append(",").append(roomIdx).append("\n");
  }

  @Override
  public void moveTargetCharacterTo(int roomIdx) throws IllegalArgumentException {
    log.append("moveTargetCharacterTo: ").append(uniqueCode)
            .append(",").append(roomIdx).append("\n");
  }

  @Override
  public void showPlayerInfo(String str) throws IllegalArgumentException {
    log.append("showPlayerInfo: ").append(uniqueCode).append(",").append(str).append("\n");
  }

  @Override
  public void resetAllInfo() {
    log.append("resetAllInfo: ").append(uniqueCode).append("\n");
  }

  @Override
  public void resetAllData() {
    log.append("resetAllData: ").append(uniqueCode).append("\n");
  }

  @Override
  public void initMap(String mapImgFilePath, List<int[]> roomBounds,
                      List<String> playerIcons, String targetIcon) {
    log.append("initMap: ").append(uniqueCode).append(",")
            .append(mapImgFilePath).append(",").append(roomBounds).append(",")
            .append(playerIcons).append(",").append(targetIcon).append("\n");
  }

  @Override
  public void showInitPlayerPanel(int maxPlayerNum) {
    log.append("showInitPlayerPanel: ").append(uniqueCode)
            .append(",").append(maxPlayerNum).append("\n");
  }

  @Override
  public void closeInitPlayerPanel() {
    log.append("closeInitPlayerPanel: ").append(uniqueCode).append("\n");
  }

  @Override
  public void showInitMaxItemCarriedPanel() {
    log.append("showInitMaxItemCarriedPanel: ").append(uniqueCode).append("\n");
  }

  @Override
  public void closeInitMaxItemCarriedPanel() {
    log.append("closeInitMaxItemCarriedPanel: ").append(uniqueCode).append("\n");
  }

  @Override
  public void showInitMaxTurnPanel() {
    log.append("showInitMaxTurnPanel: ").append(uniqueCode).append("\n");
  }

  @Override
  public void closeInitMaxTurnPanel() {
    log.append("closeInitMaxTurnPanel: ").append(uniqueCode).append("\n");
  }

  @Override
  public void showPickupItemPanel(List<Integer> items) {
    log.append("showPickupItemPanel: ").append(uniqueCode).append(",").append(items).append("\n");

  }

  @Override
  public void closePickupItemPanel() {
    log.append("closePickupItemPanel: ").append(uniqueCode).append("\n");
  }

  @Override
  public void showMovePetPanel() {
    log.append("showMovePetPanel: ").append(uniqueCode).append("\n");
  }

  @Override
  public void closeMovePetPanel() {
    log.append("closeMovePetPanel: ").append(uniqueCode).append("\n");
  }

  @Override
  public void showUseItemPanel(List<Integer> items) {
    log.append("showUseItemPanel: ").append(uniqueCode).append(",").append(items).append("\n");
  }

  @Override
  public void closeUseItemPanel() {
    log.append("closeUseItemPanel: ").append(uniqueCode).append("\n");

  }

  @Override
  public void setViewLock(boolean lockInput, boolean lockTurnActionListener) {
    log.append("setViewLock: ").append(uniqueCode).append(",")
            .append(lockInput).append(",").append(lockTurnActionListener).append("\n");

  }

  @Override
  public void setViewModel(WorldViewModel m) throws IllegalArgumentException {
    log.append("setViewModel: " + uniqueCode + "\n");
  }

  @Override
  public void setFeatures(WorldFeatureController f) throws IllegalArgumentException {
    log.append("setFeatures: " + uniqueCode + "\n");
  }
}
