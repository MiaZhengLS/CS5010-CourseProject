package controller;

import model.WorldModel;
import view.WorldView;

/**
 * The interface of various commands needed for the game.
 */
public interface WorldCommand {

  /**
   * Execute command.
   *
   * @param world model for executing
   * @param view  view for display
   * @param param the type depends on the type of commands
   */
  void execute(WorldModel world, WorldView view, Object param);
}
