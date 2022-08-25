package controller;

import model.PlayerType;
import model.WorldModel;
import view.WorldView;

/**
 * Command for clicking the current human player's graphical representation.
 */
public class ClickPlayerCommand implements WorldCommand {

  /**
   * Execute command.
   * If game is over or current player is AI, the command execution request will be ignored.
   *
   * @param world model for executing
   * @param view  view for display
   * @param param is the index of the player
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
    int playerIdx = (int) param;
    if (world.getCurPlayerIdx() == playerIdx && world.getCurPlayerType() == PlayerType.HUMAN) {
      view.showPlayerInfo(world.printPlayerInfo(playerIdx));
    }
  }

  @Override
  public String toString() {
    return "ClickPlayerCommand";
  }
}
