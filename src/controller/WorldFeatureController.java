package controller;

import view.WorldView;

/**
 * This is the feature controller for the game.
 * It offers features for the view to used in the ui callbacks.
 * It also offers an entrance for executing the commands the user issues via the ui.
 */
public interface WorldFeatureController {

  /**
   * Set the view which displays information to the user.
   *
   * @param v view for the game
   * @IllegalArgumentException v is null
   */
  void setView(WorldView v) throws IllegalArgumentException;

  /**
   * Execute commands.
   * This will be called by the view, and necessary parameter for the command will be passed in.
   *
   * @param cmd command thrown by the view
   * @param param parameter of the command, usually generated via user input, may be nullable
   * @throws IllegalArgumentException cmd is null
   */
  void executeCommand(WorldCommand cmd, Object param) throws IllegalArgumentException;
}
