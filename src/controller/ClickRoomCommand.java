package controller;

import java.util.List;
import model.PlayerType;
import model.WorldModel;
import view.WorldView;

/**
 * Command for clicking a room's graphical representation.
 */
public class ClickRoomCommand implements WorldCommand {

  /**
   * Execute command.
   * If game is over or current player is AI, the command execution request will be ignored.
   *
   * @param world model for executing
   * @param view  view for display
   * @param param is the index of the room
   * @throws IllegalArgumentException model or view is null, or the param isn't of the right type
   */
  @Override
  public void execute(WorldModel world, WorldView view, Object param)
          throws IllegalArgumentException {
    if (world == null) {
      throw new IllegalArgumentException("model is null");
    }
    if (view == null) {
      throw new IllegalArgumentException("view is null");
    }
    if (world.isGameOver()) {
      return;
    }
    if (world.getCurPlayerType() == PlayerType.AI) {
      return;
    }
    if (!(param instanceof Integer)) {
      throw new IllegalArgumentException("param should be integer");
    }
    int roomIdx = (int) param;
    if (roomIdx < 0 || roomIdx >= world.getRoomNumber()) {
      throw new IllegalArgumentException("roomIdx is invalid");
    }
    List<Integer> neighbors = world.getRoomNeighbors(world.getCurPlayerRoomIdx());
    int curPlayerIdx = world.getCurPlayerIdx();
    if (neighbors != null && neighbors.contains(roomIdx)) {
      try {
        world.curHumanPlayerMove(roomIdx);
        view.movePlayerTo(curPlayerIdx, roomIdx);
        WorldCommand cmd = new TurnFinishCommand();
        cmd.execute(world, view, null);
      } catch (IllegalStateException | IllegalArgumentException ise) {
        view.showWarning(ise.getMessage());
        return;
      }
    }
  }

  @Override
  public String toString() {
    return "ClickRoomCommand";
  }
}
