package controller;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import model.WorldModel;
import view.WorldView;

/**
 * Command for starting a new game.
 */
public class NewGameCommand implements WorldCommand {

  /**
   * Execute command.
   * If game is over or current player is AI, the command execution request will be ignored.
   *
   * @param world model for executing
   * @param view  view for display
   * @param param null means using existing world specification, else should be the file chosen as
   *              the new world specification
   * @throws IllegalArgumentException model or view is null, or the param isn't of the right type
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
    if (param == null) {
      int maxTurn = world.getMaxTurn();
      world.reinitializeWithCurrentConfig();
      world.setMaxTurn(maxTurn);
      view.showInitPlayerPanel(10);
    } else {
      if (param instanceof File) {
        File file = (File) param;
        try {
          FileReader fileReader = new FileReader(file);
          world.reinitializeWithNewConfig(fileReader);
          view.showInitMaxTurnPanel();
        } catch (IOException | IllegalStateException | IllegalArgumentException ise) {
          view.showWarning(ise.getMessage());
          return;
        }
      } else {
        throw new IllegalArgumentException("param should be file type");
      }
    }
  }

  @Override
  public String toString() {
    return "NewGameCommand";
  }
}
