import static org.junit.Assert.assertEquals;

import controller.AttemptAttackStep1Command;
import controller.AttemptAttackStep2Command;
import controller.ClickPlayerCommand;
import controller.ClickRoomCommand;
import controller.LookAroundCommand;
import controller.MovePetStep1Command;
import controller.MovePetStep2Command;
import controller.NewGameCommand;
import controller.PickupItemStep1Command;
import controller.PickupItemStep2Command;
import controller.SetMaxItemCarriedCommand;
import controller.SetMaxTurnCommand;
import controller.SetPlayerCommand;
import controller.WorldCommand;
import controller.WorldFeatureController;
import controller.WorldFeatureControllerImpl;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import model.WorldModel;
import org.junit.Test;
import view.WorldView;


/**
 * Test for the world feature controller.
 */
public class WorldFeatureControllerTest {

  @Test(expected = IllegalArgumentException.class)
  public void setViewNull() {
    WorldFeatureController controller = new WorldFeatureControllerImpl(null);
  }

  @Test
  public void setView() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 3;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldFeatureController controller = new WorldFeatureControllerImpl(m);
    controller.setView(v);
    String expected = "setFeatures: 3\n"
            + "setViewModel: 3\n";
    assertEquals(expected, mockLog.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void executeNullCommand() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 3;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldFeatureController controller = new WorldFeatureControllerImpl(m);
    controller.setView(v);
    controller.executeCommand(null, null);
  }

  @Test(expected = IllegalStateException.class)
  public void executeCommandWithNoView() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 3;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new MockCommand(mockLog, uniqCode);
    WorldFeatureController controller = new WorldFeatureControllerImpl(m);
    controller.executeCommand(cmd, null);
  }

  @Test
  public void cmdNewGameNullParam() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new NewGameCommand();
    cmd.execute(m, v, null);
    String expected = "reinitializeWithCurrentConfig: 5\n"
            + "getMaxTurn: 5\n"
            + "setMaxTurn: 5,20\n"
            + "showInitPlayerPanel: 5,10\n";
    assertEquals(expected, mockLog.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdNewGameInvalidParam() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new NewGameCommand();
    cmd.execute(m, v, 2);
  }

  @Test
  public void cmdNewGameFileParam() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new NewGameCommand();
    cmd.execute(m, v, new File("GravityFalls.txt"));
    String expected = "reinitializeWithNewConfig: 5,false\n"
            + "showInitMaxTurnPanel: 5\n";
    assertEquals(expected, mockLog.toString());
  }

  @Test
  public void cmdNewGameHandleException() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModelNewGameException(mockLog, uniqCode);
    WorldCommand cmd = new NewGameCommand();
    cmd.execute(m, v, new File("WrongContent.txt"));
    String expected = "reinitializeWithNewConfig: 5,false\n"
            + "showWarning: 5,invalid file content\n";
    assertEquals(expected, mockLog.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdSetPlayerNullParam() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new SetPlayerCommand(null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdSetPlayerListNotEqualLength() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    List<String> names = new ArrayList<>();
    List<Integer> rooms = new ArrayList<>();
    List<Boolean> isAi = new ArrayList<>();
    names.add("aaa");
    names.add("bb");
    WorldCommand cmd = new SetPlayerCommand(names, rooms, isAi);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdSetPlayerListAllEmpty() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    List<String> names = new ArrayList<>();
    List<Integer> rooms = new ArrayList<>();
    List<Boolean> isAi = new ArrayList<>();
    WorldCommand cmd = new SetPlayerCommand(names, rooms, isAi);
  }

  @Test
  public void cmdSetPlayerAllAi() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    List<String> names = new ArrayList<>();
    List<Integer> rooms = new ArrayList<>();
    List<Boolean> isAi = new ArrayList<>();
    names.add("aaa");
    rooms.add(0);
    isAi.add(true);
    WorldCommand cmd = new SetPlayerCommand(names, rooms, isAi);
    cmd.execute(m, v, null);
    String expected = "showWarning: 5,There must be at least one human player\n";
    assertEquals(expected, mockLog.toString());
  }

  @Test
  public void cmdSetPlayerHandleException() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModelSetPlayerException(mockLog, uniqCode);
    List<String> names = new ArrayList<>();
    List<Integer> rooms = new ArrayList<>();
    List<Boolean> isAi = new ArrayList<>();
    names.add("aaa");
    names.add("aaa");
    rooms.add(0);
    rooms.add(0);
    isAi.add(true);
    isAi.add(false);
    WorldCommand cmd = new SetPlayerCommand(names, rooms, isAi);
    cmd.execute(m, v, null);
    String expected = "showWarning: 5,player name duplicate\n";
    assertEquals(expected, mockLog.toString());
  }

  @Test
  public void cmdSetPlayer() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    List<String> names = new ArrayList<>();
    List<Integer> rooms = new ArrayList<>();
    List<Boolean> isAi = new ArrayList<>();
    names.add("aaa");
    names.add("bbb");
    rooms.add(0);
    rooms.add(0);
    isAi.add(true);
    isAi.add(false);
    WorldCommand cmd = new SetPlayerCommand(names, rooms, isAi);
    cmd.execute(m, v, null);
    String expected =
            "setPlayers: 5,[aaa, bbb],[0, 0],[true, false]\n"
                    + "closeInitPlayerPanel: 5\n"
                    + "showInitMaxItemCarriedPanel: 5\n";
    assertEquals(expected, mockLog.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdSetPlayerNullModel() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    List<String> names = new ArrayList<>();
    List<Integer> rooms = new ArrayList<>();
    List<Boolean> isAi = new ArrayList<>();
    names.add("aaa");
    names.add("bbb");
    rooms.add(0);
    rooms.add(0);
    isAi.add(true);
    isAi.add(false);
    WorldCommand cmd = new SetPlayerCommand(names, rooms, isAi);
    cmd.execute(null, v, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdSetPlayerNullView() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    List<String> names = new ArrayList<>();
    List<Integer> rooms = new ArrayList<>();
    List<Boolean> isAi = new ArrayList<>();
    names.add("aaa");
    names.add("bbb");
    rooms.add(0);
    rooms.add(0);
    isAi.add(true);
    isAi.add(false);
    WorldCommand cmd = new SetPlayerCommand(names, rooms, isAi);
    cmd.execute(m, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdSetMaxItemNullParam() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new SetMaxItemCarriedCommand();
    cmd.execute(m, v, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdSetMaxItemWrongTypeParam() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new SetMaxItemCarriedCommand();
    cmd.execute(m, v, "sss");
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdSetMaxItemLessThanOne() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new SetMaxItemCarriedCommand();
    cmd.execute(m, v, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdSetMaxItemNullModel() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new SetMaxItemCarriedCommand();
    cmd.execute(null, v, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdSetMaxItemNullView() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new SetMaxItemCarriedCommand();
    cmd.execute(m, null, 2);
  }

  @Test
  public void cmdSetMaxItemAndStartGame() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new SetMaxItemCarriedCommand();
    cmd.execute(m, v, 1);
    String expected =
            "setMaxItemCarried: 5,1\n"
                    + "closeInitMaxItemCarriedPanel: 5\n"
                    + "isGameOver: 5\n"
                    + "getPlayerNumber: 5\n"
                    + "getMaxTurn: 5\n"
                    + "getMaxItemNumCarried: 5\n"
                    + "outputMapImage: 5runtime_res/map.png,40\n"
                    + "resetAllInfo: 5\n"
                    + "getRoomNumber: 5\n"
                    + "getPlayerNumber: 5\n"
                    + "getPlayerNumber: 5\n"
                    + "getPlayerNumber: 5\n"
                    + "initMap: 5,runtime_res/map.png,[],"
                    + "[runtime_res/character/001.png, runtime_res/character/002.png],"
                    + "runtime_res/character/boss.png\n"
                    + "showPrompt: 5,Press 'C' to start game\n"
                    + "\n"
                    + "setViewLock: 5,false,true\n";
    assertEquals(expected, mockLog.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdSetMaxTurnNullParam() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new SetMaxTurnCommand();
    cmd.execute(m, v, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdSetMaxTurnWrongTypeParam() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new SetMaxTurnCommand();
    cmd.execute(m, v, "ll");
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdSetMaxTurnLessThanOne() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new SetMaxTurnCommand();
    cmd.execute(m, v, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdSetMaxTurnNullModel() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new SetMaxTurnCommand();
    cmd.execute(null, v, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdSetMaxTurnNullView() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new SetMaxTurnCommand();
    cmd.execute(m, null, 2);
  }

  @Test
  public void cmdSetMaxTurn() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new SetMaxTurnCommand();
    cmd.execute(m, v, 10);
    String expected =
            "setMaxTurn: 5,10\n"
                    + "closeInitMaxTurnPanel: 5\n"
                    + "showInitPlayerPanel: 5,10\n";
    assertEquals(expected, mockLog.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdClickPlayerNullParam() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new ClickPlayerCommand();
    cmd.execute(m, v, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdClickPlayerNullModel() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new ClickPlayerCommand();
    cmd.execute(null, v, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdClickPlayerNullView() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new ClickPlayerCommand();
    cmd.execute(m, null, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdClickPlayerWrongTypeParam() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new ClickPlayerCommand();
    cmd.execute(m, v, "3e");
  }

  @Test
  public void cmdClickPlayer() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModelClickPlayerRoom(mockLog, uniqCode);
    WorldCommand cmd = new ClickPlayerCommand();
    cmd.execute(m, v, 0);
    String expected =
            "isGameOver: 5\n"
                    + "getCurPlayerType: 5\n"
                    + "getCurPlayerIdx: 5\n"
                    + "getCurPlayerType: 5\n"
                    + "printPlayerInfo: 5,0\n"
                    + "showPlayerInfo: 5,player 0 information\n";
    assertEquals(expected, mockLog.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdClickRoomNullParam() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new ClickRoomCommand();
    cmd.execute(m, v, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdClickRoomNullModel() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new ClickRoomCommand();
    cmd.execute(null, v, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdClickRoomNullView() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new ClickRoomCommand();
    cmd.execute(m, null, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdClickRoomWrongTypeParam() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new ClickRoomCommand();
    cmd.execute(m, v, "3e");
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdClickRoomOutRange() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModelClickPlayerRoom(mockLog, uniqCode);
    WorldCommand cmd = new ClickRoomCommand();
    cmd.execute(m, v, 30);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdClickRoomNegative() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModelClickPlayerRoom(mockLog, uniqCode);
    WorldCommand cmd = new ClickRoomCommand();
    cmd.execute(m, v, -2);
  }

  @Test
  public void cmdClickRoomNotNeighbor() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModelClickPlayerRoom(mockLog, uniqCode);
    WorldCommand cmd = new ClickRoomCommand();
    cmd.execute(m, v, 0);
    String expected =
            "isGameOver: 5\n"
                    + "getCurPlayerType: 5\n"
                    + "getRoomNumber: 5\n"
                    + "getCurPlayerRoomIdx: 5\n"
                    + "getRoomNeighbors: 5,2\n"
                    + "getCurPlayerIdx: 5\n";
    assertEquals(expected, mockLog.toString());
  }

  @Test
  public void cmdClickRoom() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModelClickPlayerRoom(mockLog, uniqCode);
    WorldCommand cmd = new ClickRoomCommand();
    cmd.execute(m, v, 1);
    String expected =
            "isGameOver: 5\n"
                    + "getCurPlayerType: 5\n"
                    + "getRoomNumber: 5\n"
                    + "getCurPlayerRoomIdx: 5\n"
                    + "getRoomNeighbors: 5,2\n"
                    + "getCurPlayerIdx: 5\n"
                    + "curHumanPlayerMove: 1\n"
                    + "movePlayerTo: 5,0,1\n"
                    + "resetAllInfo: 5\n"
                    + "getLastTurnMsg: 5\n"
                    + "showTurnResult: 5,null\n"
                    + "isGameOver: 5\n"
                    + "getTargetCharacterRoomIdx: 5\n"
                    + "moveTargetCharacterTo: 5,0\n"
                    + "setViewLock: 5,false,true\n"
                    + "showPrompt: 5,Press 'C' to continue game\n"
                    + "\n";
    assertEquals(expected, mockLog.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdPickupItemNullModel() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new PickupItemStep1Command();
    cmd.execute(null, v, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdPickupItemNullView() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new PickupItemStep1Command();
    cmd.execute(m, null, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdPickupItem2NullModel() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new PickupItemStep2Command();
    cmd.execute(null, v, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdPickupItem2NullView() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new PickupItemStep2Command();
    cmd.execute(m, null, 0);
  }

  @Test
  public void cmdPickupItemCurAi() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModelCurPlayerAi(mockLog, uniqCode);
    WorldCommand cmd = new PickupItemStep1Command();
    cmd.execute(m, v, 0);
    String expected =
            "isGameOver: 5\n"
                    + "getCurPlayerType: 5\n"
                    + "closePickupItemPanel: 5\n";
    assertEquals(expected, mockLog.toString());

    mockLog = new StringBuilder();
    uniqCode = 5;
    v = new MockView(mockLog, uniqCode);
    m = new MockModelCurPlayerAi(mockLog, uniqCode);
    cmd = new PickupItemStep2Command();
    cmd.execute(m, v, 0);
    expected =
            "isGameOver: 5\n"
                    + "getCurPlayerType: 5\n"
                    + "closePickupItemPanel: 5\n";

    assertEquals(expected, mockLog.toString());
  }

  @Test
  public void cmdShowPickupItemPanelReachMax() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModelReachMaxItem(mockLog, uniqCode);
    WorldCommand cmd = new PickupItemStep1Command();
    cmd.execute(m, v, 0);
    String expected =
            "isGameOver: 5\n"
                    + "getCurPlayerType: 5\n"
                    + "getCurPlayerItemNum: 5\n"
                    + "getMaxItemNumCarried: 5\n"
                    + "showWarning: 5,The player's item number has reached limit\n"
                    + "closePickupItemPanel: 5\n";
    assertEquals(expected, mockLog.toString());
  }

  @Test
  public void cmdPickupItemReachMax() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModelReachMaxItem(mockLog, uniqCode);
    WorldCommand cmd = new PickupItemStep2Command();
    cmd.execute(m, v, 3);
    String expected =
            "isGameOver: 5\n"
                    + "getCurPlayerType: 5\n"
                    + "getCurPlayerItemNum: 5\n"
                    + "getMaxItemNumCarried: 5\n"
                    + "showWarning: 5,The player's item number has reached limit\n"
                    + "closePickupItemPanel: 5\n";
    assertEquals(expected, mockLog.toString());
  }

  @Test
  public void cmdShowPickupItemPanel() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModelClickPlayerRoom(mockLog, uniqCode);
    WorldCommand cmd = new PickupItemStep1Command();
    cmd.execute(m, v, 1);
    String expected =
            "isGameOver: 5\n"
                    + "getCurPlayerType: 5\n"
                    + "getCurPlayerItemNum: 5\n"
                    + "getMaxItemNumCarried: 5\n"
                    + "getCurPlayerRoomIdx: 5\n"
                    + "getRoomItems: 5,2\n"
                    + "showPickupItemPanel: 5,[1]\n";
    assertEquals(expected, mockLog.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdPickupItemWrongTypeParam() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new PickupItemStep2Command();
    cmd.execute(m, v, "3e");
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdPickupItemParamNegative() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new PickupItemStep2Command();
    cmd.execute(m, v, -2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdPickupItemParamOutRange() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModelClickPlayerRoom(mockLog, uniqCode);
    WorldCommand cmd = new PickupItemStep2Command();
    cmd.execute(m, v, 2);
  }

  @Test
  public void cmdPickupItem() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModelClickPlayerRoom(mockLog, uniqCode);
    WorldCommand cmd = new PickupItemStep2Command();
    cmd.execute(m, v, 0);
    String expected =
            "isGameOver: 5\n"
                    + "getCurPlayerType: 5\n"
                    + "getCurPlayerItemNum: 5\n"
                    + "getMaxItemNumCarried: 5\n"
                    + "getCurPlayerRoomIdx: 5\n"
                    + "getRoomItems: 5,2\n"
                    + "curHumanPlayerPickupItem: 5,1\n"
                    + "closePickupItemPanel: 5\n"
                    + "resetAllInfo: 5\n"
                    + "getLastTurnMsg: 5\n"
                    + "showTurnResult: 5,null\n"
                    + "isGameOver: 5\n"
                    + "getTargetCharacterRoomIdx: 5\n"
                    + "moveTargetCharacterTo: 5,0\n"
                    + "setViewLock: 5,false,true\n"
                    + "showPrompt: 5,Press 'C' to continue game\n"
                    + "\n";
    assertEquals(expected, mockLog.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdLookAroundNullModel() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new LookAroundCommand();
    cmd.execute(null, v, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdLookAroundNullView() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new LookAroundCommand();
    cmd.execute(m, null, null);
  }

  @Test
  public void cmdLookAround() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new LookAroundCommand();
    cmd.execute(m, v, null);
    String expected =
            "isGameOver: 5\n"
                    + "getCurPlayerType: 5\n"
                    + "curHumanPlayerDisplayNeighborRooms: 5\n"
                    + "resetAllInfo: 5\n"
                    + "getLastTurnMsg: 5\n"
                    + "showTurnResult: 5,null\n"
                    + "isGameOver: 5\n"
                    + "getTargetCharacterRoomIdx: 5\n"
                    + "moveTargetCharacterTo: 5,0\n"
                    + "setViewLock: 5,false,true\n"
                    + "showPrompt: 5,Press 'C' to continue game\n"
                    + "\n";
    assertEquals(expected, mockLog.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdMovePetNullModel() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new MovePetStep1Command();
    cmd.execute(null, v, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdMovePetNullView() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new MovePetStep1Command();
    cmd.execute(m, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdMovePet2NullModel() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new MovePetStep2Command();
    cmd.execute(null, v, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdMovePet2NullView() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new MovePetStep2Command();
    cmd.execute(m, null, null);
  }

  @Test
  public void cmdShowMovePetPanelCurAi() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModelCurPlayerAi(mockLog, uniqCode);
    WorldCommand cmd = new MovePetStep1Command();
    cmd.execute(m, v, 0);
    String expected =
            "isGameOver: 5\n"
                    + "getCurPlayerType: 5\n"
                    + "closeMovePetPanel: 5\n";
    assertEquals(expected, mockLog.toString());

    mockLog = new StringBuilder();
    uniqCode = 5;
    v = new MockView(mockLog, uniqCode);
    m = new MockModelCurPlayerAi(mockLog, uniqCode);
    cmd = new MovePetStep2Command();
    cmd.execute(m, v, 0);
    expected =
            "isGameOver: 5\n"
                    + "getCurPlayerType: 5\n"
                    + "closeMovePetPanel: 5\n";

    assertEquals(expected, mockLog.toString());
  }

  @Test
  public void cmdShowMovePetPanel() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new MovePetStep1Command();
    cmd.execute(m, v, 0);
    String expected =
            "isGameOver: 5\n"
                    + "getCurPlayerType: 5\n"
                    + "showMovePetPanel: 5\n";
    assertEquals(expected, mockLog.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdMovePetWrongTypeParam() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new MovePetStep2Command();
    cmd.execute(m, v, "0");
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdMovePetParamOutRange() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModelClickPlayerRoom(mockLog, uniqCode);
    WorldCommand cmd = new MovePetStep2Command();
    cmd.execute(m, v, 32);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdMovePetParamNegative() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModelClickPlayerRoom(mockLog, uniqCode);
    WorldCommand cmd = new MovePetStep2Command();
    cmd.execute(m, v, -4);
  }

  @Test
  public void cmdMovePet() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModelClickPlayerRoom(mockLog, uniqCode);
    WorldCommand cmd = new MovePetStep2Command();
    cmd.execute(m, v, 4);
    String expected =
            "isGameOver: 5\n"
                    + "getCurPlayerType: 5\n"
                    + "getRoomNumber: 5\n"
                    + "curHumanPlayerMovePet: 5,4\n"
                    + "closeMovePetPanel: 5\n"
                    + "resetAllInfo: 5\n"
                    + "getLastTurnMsg: 5\n"
                    + "showTurnResult: 5,null\n"
                    + "isGameOver: 5\n"
                    + "getTargetCharacterRoomIdx: 5\n"
                    + "moveTargetCharacterTo: 5,0\n"
                    + "setViewLock: 5,false,true\n"
                    + "showPrompt: 5,Press 'C' to continue game\n"
                    + "\n";
    assertEquals(expected, mockLog.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdAttackNullModel() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new AttemptAttackStep1Command();
    cmd.execute(null, v, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdAttackNullView() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new AttemptAttackStep1Command();
    cmd.execute(m, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdAttack2NullModel() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new AttemptAttackStep2Command();
    cmd.execute(null, v, 4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdAttack2NullView() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new AttemptAttackStep2Command();
    cmd.execute(m, null, 4);
  }

  @Test
  public void cmdAttackCurAi() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModelCurPlayerAi(mockLog, uniqCode);
    WorldCommand cmd = new AttemptAttackStep1Command();
    cmd.execute(m, v, null);
    String expected =
            "isGameOver: 5\n"
                    + "getCurPlayerType: 5\n"
                    + "closeUseItemPanel: 5\n";
    assertEquals(expected, mockLog.toString());

    mockLog = new StringBuilder();
    uniqCode = 5;
    v = new MockView(mockLog, uniqCode);
    m = new MockModelCurPlayerAi(mockLog, uniqCode);
    cmd = new AttemptAttackStep2Command();
    cmd.execute(m, v, 0);
    expected =
            "isGameOver: 5\n"
                    + "getCurPlayerType: 5\n"
                    + "closeUseItemPanel: 5\n";

    assertEquals(expected, mockLog.toString());
  }

  @Test
  public void cmdShowAttackPanelNotInSameRoom() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModelClickPlayerRoom(mockLog, uniqCode);
    WorldCommand cmd = new AttemptAttackStep1Command();
    cmd.execute(m, v, null);
    String expected =
            "isGameOver: 5\n"
                    + "getCurPlayerType: 5\n"
                    + "getCurPlayerRoomIdx: 5\n"
                    + "getTargetCharacterRoomIdx: 5\n"
                    + "showWarning: 5,current player isn't in the same room with target character\n"
                    + "closeUseItemPanel: 5\n";
    assertEquals(expected, mockLog.toString());
  }

  @Test
  public void cmdAttackNoItemCarried() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new AttemptAttackStep1Command();
    cmd.execute(m, v, null);
    String expected =
            "isGameOver: 5\n"
                    + "getCurPlayerType: 5\n"
                    + "getCurPlayerRoomIdx: 5\n"
                    + "getTargetCharacterRoomIdx: 5\n"
                    + "getCurPlayerItemNum: 5\n"
                    + "curHumanPlayerAttackTargetCharacter: 5,-1\n"
                    + "closeUseItemPanel: 5\n"
                    + "resetAllInfo: 5\n"
                    + "getLastTurnMsg: 5\n"
                    + "showTurnResult: 5,null\n"
                    + "isGameOver: 5\n"
                    + "getTargetCharacterRoomIdx: 5\n"
                    + "moveTargetCharacterTo: 5,0\n"
                    + "setViewLock: 5,false,true\n"
                    + "showPrompt: 5,Press 'C' to continue game\n"
                    + "\n";
    assertEquals(expected, mockLog.toString());
  }

  @Test
  public void cmdShowAttackPanelWithItemCarried() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModelReachMaxItem(mockLog, uniqCode);
    WorldCommand cmd = new AttemptAttackStep1Command();
    cmd.execute(m, v, null);
    String expected =
            "isGameOver: 5\n"
                    + "getCurPlayerType: 5\n"
                    + "getCurPlayerRoomIdx: 5\n"
                    + "getTargetCharacterRoomIdx: 5\n"
                    + "getCurPlayerItemNum: 5\n"
                    + "getCurPlayerCarryItems: 5\n"
                    + "showUseItemPanel: 5,[1, 5]\n";
    assertEquals(expected, mockLog.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdUseItemWrongTypeParam() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModel(mockLog, uniqCode);
    WorldCommand cmd = new AttemptAttackStep2Command();
    cmd.execute(m, v, "3e");
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdUseItemOutRange() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModelReachMaxItem(mockLog, uniqCode);
    WorldCommand cmd = new AttemptAttackStep2Command();
    cmd.execute(m, v, 2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void cmdUseItemNegative() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModelReachMaxItem(mockLog, uniqCode);
    WorldCommand cmd = new AttemptAttackStep2Command();
    cmd.execute(m, v, -2);
  }

  @Test
  public void cmdUseItem() {
    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModelReachMaxItem(mockLog, uniqCode);
    WorldCommand cmd = new AttemptAttackStep2Command();
    cmd.execute(m, v, 1);
    String expected =
            "isGameOver: 5\n"
                    + "getCurPlayerType: 5\n"
                    + "getCurPlayerCarryItems: 5\n"
                    + "curHumanPlayerAttackTargetCharacter: 5,5\n"
                    + "closeUseItemPanel: 5\n"
                    + "resetAllInfo: 5\n"
                    + "getLastTurnMsg: 5\n"
                    + "showTurnResult: 5,null\n"
                    + "isGameOver: 5\n"
                    + "getTargetCharacterRoomIdx: 5\n"
                    + "moveTargetCharacterTo: 5,0\n"
                    + "setViewLock: 5,false,true\n"
                    + "showPrompt: 5,Press 'C' to continue game\n"
                    + "\n";
    assertEquals(expected, mockLog.toString());
  }

  @Test
  public void cmdTurnRelatedGameOver() {

    StringBuilder mockLog = new StringBuilder();
    int uniqCode = 5;
    WorldView v = new MockView(mockLog, uniqCode);
    WorldModel m = new MockModelGameOver(mockLog, uniqCode);
    WorldCommand cmd = new PickupItemStep1Command();
    cmd.execute(m, v, null);
    String expected = "isGameOver: 5\n";
    assertEquals(expected, mockLog.toString());

    mockLog = new StringBuilder();
    uniqCode++;
    v = new MockView(mockLog, uniqCode);
    m = new MockModelGameOver(mockLog, uniqCode);
    cmd = new PickupItemStep2Command();
    cmd.execute(m, v, null);
    expected = "isGameOver: 6\n";
    assertEquals(expected, mockLog.toString());

    mockLog = new StringBuilder();
    uniqCode++;
    v = new MockView(mockLog, uniqCode);
    m = new MockModelGameOver(mockLog, uniqCode);
    cmd = new ClickRoomCommand();
    cmd.execute(m, v, null);
    expected = "isGameOver: 7\n";
    assertEquals(expected, mockLog.toString());

    mockLog = new StringBuilder();
    uniqCode++;
    v = new MockView(mockLog, uniqCode);
    m = new MockModelGameOver(mockLog, uniqCode);
    cmd = new LookAroundCommand();
    cmd.execute(m, v, null);
    expected = "isGameOver: 8\n";
    assertEquals(expected, mockLog.toString());

    mockLog = new StringBuilder();
    uniqCode++;
    v = new MockView(mockLog, uniqCode);
    m = new MockModelGameOver(mockLog, uniqCode);
    cmd = new MovePetStep1Command();
    cmd.execute(m, v, null);
    expected = "isGameOver: 9\n";
    assertEquals(expected, mockLog.toString());

    mockLog = new StringBuilder();
    uniqCode++;
    v = new MockView(mockLog, uniqCode);
    m = new MockModelGameOver(mockLog, uniqCode);
    cmd = new MovePetStep2Command();
    cmd.execute(m, v, null);
    expected = "isGameOver: 10\n";
    assertEquals(expected, mockLog.toString());

    mockLog = new StringBuilder();
    uniqCode++;
    v = new MockView(mockLog, uniqCode);
    m = new MockModelGameOver(mockLog, uniqCode);
    cmd = new AttemptAttackStep1Command();
    cmd.execute(m, v, null);
    expected = "isGameOver: 11\n";
    assertEquals(expected, mockLog.toString());

    mockLog = new StringBuilder();
    uniqCode++;
    v = new MockView(mockLog, uniqCode);
    m = new MockModelGameOver(mockLog, uniqCode);
    cmd = new AttemptAttackStep2Command();
    cmd.execute(m, v, null);
    expected = "isGameOver: 12\n";
    assertEquals(expected, mockLog.toString());
  }

}