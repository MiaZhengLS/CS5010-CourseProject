package controller;

import model.WorldModel;
import view.WorldView;

/**
 * Command for setting the max item each player can carry.
 */
public class SetMaxItemCarriedCommand implements WorldCommand {

  /**
   * Execute command.
   *
   * @param world model for executing
   * @param view  view for display
   * @param param is the expected max item each player can carry
   * @throws IllegalArgumentException model or view is null, or the param isn't of the right type
   *                                  or the param is less than one
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
    if (!(param instanceof Integer)) {
      throw new IllegalArgumentException("param should be integer");
    }
    int intParam = (int) param;
    if (intParam < 1) {
      throw new IllegalArgumentException("param should be bigger than 1");
    }
    try {
      world.setMaxItemCarried(intParam);
      view.closeInitMaxItemCarriedPanel();
      WorldCommand cmd = new StartGameCommand();
      cmd.execute(world, view, null);
    } catch (IllegalArgumentException iae) {
      view.showWarning(iae.getMessage());
      return;
    }
  }

  @Override
  public String toString() {
    return "SetMaxItemCarriedCommand";
  }
}
