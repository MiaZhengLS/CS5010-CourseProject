package controller;

import model.PlayerType;
import model.WorldModel;
import view.WorldView;

/**
 * Command for checking whether moving pet can be executed.
 */
public class MovePetStep1Command implements WorldCommand {

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
    view.showMovePetPanel();
  }

  @Override
  public String toString() {
    return "MovePetStep1Command";
  }
}
