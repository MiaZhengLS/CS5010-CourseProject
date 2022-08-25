package controller;

import model.WorldModel;
import view.WorldView;

/**
 * Command for exiting the application.
 */
public class ExitGameCommand implements WorldCommand {

  /**
   * Execute command.
   *
   * @param world is ignored here
   * @param view  is ignored here
   * @param param is ignored here
   */
  @Override
  public void execute(WorldModel world, WorldView view, Object param) {
    System.exit(0);
  }

  @Override
  public String toString() {
    return "ExitGameCommand";
  }
}
