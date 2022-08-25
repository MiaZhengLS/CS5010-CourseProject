package controller;

import model.WorldModel;
import view.WorldView;

/**
 * Command for finishing a turn.
 * This is called after any turn-related command is finished.
 */
public class TurnFinishCommand implements WorldCommand {

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
    view.resetAllInfo();
    view.showTurnResult(world.getLastTurnMsg());
    if (world.isGameOver()) {
      view.setViewLock(true, true);
      view.showGameResult(world.getGameResultMsg());
    } else {
      view.moveTargetCharacterTo(world.getTargetCharacterRoomIdx());
      view.setViewLock(false, true);
      view.showPrompt(CommandPromptType.USER_PROMPT.toString());
    }
  }

  @Override
  public String toString() {
    return "TurnFinishCommand";
  }
}
