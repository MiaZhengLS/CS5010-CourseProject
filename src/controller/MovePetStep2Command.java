package controller;

import model.PlayerType;
import model.WorldModel;
import view.WorldView;

/**
 * Command for confirm move pet to selected room.
 */
public class MovePetStep2Command implements WorldCommand {

  /**
   * Execute command.
   * If game is over or current player is AI, the command execution request will be ignored.
   *
   * @param world model for executing
   * @param view  view for display
   * @param param is the index of the room the pet will be moved to
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
      view.closeMovePetPanel();
      return;
    }
    if (!(param instanceof Integer)) {
      throw new IllegalArgumentException("param should be integer");
    }
    int selectIdx = (int) param;
    if (selectIdx >= 0 && selectIdx < world.getRoomNumber()) {
      world.curHumanPlayerMovePet(selectIdx);
      view.closeMovePetPanel();
      WorldCommand cmd = new TurnFinishCommand();
      cmd.execute(world, view, null);
    } else {
      throw new IllegalArgumentException("param invalid");
    }
  }

  @Override
  public String toString() {
    return "MovePetStep2Command";
  }
}
