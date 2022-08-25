package controller;

import model.PlayerType;
import model.WorldModel;
import view.WorldView;

/**
 * Command for finishing a turn.
 * This is called when user confirms continuing game.
 */
public class TurnStartCommand implements WorldCommand {

  /**
   * Execute command.
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
      throw new IllegalArgumentException("model is null");
    }
    if (view == null) {
      throw new IllegalArgumentException("view is null");
    }
    if (world.isGameOver()) {
      view.setViewLock(true, true);
      view.showGameResult(world.getGameResultMsg());
    } else {
      if (world.getCurPlayerType() == PlayerType.AI) {
        WorldCommand cmd = new AiDoActionCommand();
        cmd.execute(world, view, null);
      } else {
        view.setViewLock(false, false);
        view.resetAllInfo();
        view.showTurnInfo(world.getCurTurnInfo());
        view.showPrompt(CommandPromptType.PLAYER_ACTION.toString());
      }
    }
  }

  @Override
  public String toString() {
    return "TurnStartCommand";
  }
}
