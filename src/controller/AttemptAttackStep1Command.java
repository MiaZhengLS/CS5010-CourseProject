package controller;

import java.util.List;
import model.PlayerType;
import model.WorldModel;
import view.WorldView;

/**
 * Command for checking whether an attack can be executed.
 */
public class AttemptAttackStep1Command implements WorldCommand {

  /**
   * Execute command.
   * If game is over, or current player is AI, or current player isn't in the same room with
   * the target character, the command execution request will be ignored.
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
    if (world.getCurPlayerRoomIdx() != world.getTargetCharacterRoomIdx()) {
      view.showWarning("current player isn't in the same room with target character");
      view.closeUseItemPanel();
      return;
    }
    if (world.getCurPlayerItemNum() == 0) {
      world.curHumanPlayerAttackTargetCharacter(-1);
      view.closeUseItemPanel();
      WorldCommand cmd = new TurnFinishCommand();
      cmd.execute(world, view, null);
    } else {
      List<Integer> items = world.getCurPlayerCarryItems();
      view.showUseItemPanel(items);
    }
  }

  @Override
  public String toString() {
    return "AttemptAttackStep1Command";
  }
}
