package controller;

import model.PlayerType;
import model.WorldModel;
import view.WorldView;

/**
 * Command for displaying neighbor information of current human player.
 */
public class LookAroundCommand implements WorldCommand {

  /**
   * Execute command.
   * If game is over or current player is AI, the command execution request will be ignored.
   *
   * @param world model for executing
   * @param view  view for display
   * @param param is ignored here
   * @throws IllegalArgumentException model or view is null
   */
  @Override
  public void execute(WorldModel world, WorldView view, Object param)
          throws IllegalArgumentException, IllegalStateException {
    if (world == null) {
      throw new IllegalArgumentException("world is null!");
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
    try {
      world.curHumanPlayerDisplayNeighborRooms();
      WorldCommand cmd = new TurnFinishCommand();
      cmd.execute(world, view, null);
    } catch (IllegalStateException ise) {
      view.showWarning(ise.getMessage());
      return;
    }
  }

  @Override
  public String toString() {
    return "LookAroundCommand";
  }
}
