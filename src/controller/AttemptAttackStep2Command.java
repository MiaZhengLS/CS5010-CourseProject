package controller;

import java.util.List;
import model.PlayerType;
import model.WorldModel;
import view.WorldView;

/**
 * Command for confirm attack with the selected item.
 */
public class AttemptAttackStep2Command implements WorldCommand {

  /**
   * Execute command.
   * If game is over or current player is AI, the command execution request will be ignored.
   *
   * @param world model for executing
   * @param view  view for display
   * @param param is the index of the item in the list of the items the current player carries
   * @throws IllegalArgumentException model or view is null, or the param isn't of the right type
   */
  @Override
  public void execute(WorldModel world, WorldView view, Object param)
          throws IllegalArgumentException {
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
      view.closeUseItemPanel();
      return;
    }
    if (!(param instanceof Integer)) {
      throw new IllegalArgumentException("param should be integer");
    }
    int selectIdx = (int) param;
    List<Integer> items = world.getCurPlayerCarryItems();
    if (selectIdx < 0 || selectIdx >= items.size()) {
      throw new IllegalArgumentException("param invalid");
    } else {
      try {
        world.curHumanPlayerAttackTargetCharacter(items.get(selectIdx));
        view.closeUseItemPanel();
        WorldCommand cmd = new TurnFinishCommand();
        cmd.execute(world, view, null);
      } catch (IllegalStateException ise) {
        view.showWarning(ise.getMessage());
        return;
      }
    }
  }

  @Override
  public String toString() {
    return "AttemptAttackStep2Command";
  }
}
