package controller;

import model.AiActionParam;
import model.PlayerActionType;
import model.PlayerType;
import model.WorldModel;
import view.WorldView;

/**
 * Command for AI player to do their action.
 */
public class AiDoActionCommand implements WorldCommand {

  /**
   * Execute command.
   *
   * @param world model for executing
   * @param view  view for display
   * @param param is ignored here
   * @throws IllegalArgumentException model or view is null
   * @throws IllegalStateException    game is over, or current player isn't AI
   */
  @Override
  public void execute(WorldModel world, WorldView view, Object param)
          throws IllegalArgumentException, IllegalStateException {
    if (world == null) {
      throw new IllegalArgumentException("model is null");
    }
    if (view == null) {
      throw new IllegalArgumentException("view is null");
    }
    if (world.isGameOver()) {
      throw new IllegalStateException("Game is over");
    }
    if (world.getCurPlayerType() != PlayerType.AI) {
      throw new IllegalStateException("current player isn't AI");
    }
    view.setViewLock(true, true);
    int playerIdx = world.getCurPlayerIdx();
    AiActionParam ap = world.aiPlayerDoAction();
    if (ap.getActionType() == PlayerActionType.MOVE) {
      view.movePlayerTo(playerIdx, ap.getActionParameter());
    }
    WorldCommand cmd = new TurnFinishCommand();
    cmd.execute(world, view, null);
  }

  @Override
  public String toString() {
    return "AiDoActionCommand";
  }
}
