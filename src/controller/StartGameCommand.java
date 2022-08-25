package controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import model.WorldModel;
import view.WorldView;

/**
 * Command for initializing the game panel.
 */
public class StartGameCommand implements WorldCommand {

  /**
   * Execute command.
   * This should be called after necessary information is set, or exceptions may be thrown.
   *
   * @param world model for executing
   * @param view  view for display
   * @param param is ignored here
   * @throws IllegalArgumentException model or view is null
   * @throws IllegalStateException    necessary information isn't set
   */
  @Override
  public void execute(WorldModel world, WorldView view, Object param)
          throws IllegalArgumentException, IllegalStateException {
    if (world == null) {
      throw new IllegalArgumentException("world is null!");
    }
    if (view == null) {
      throw new IllegalArgumentException("view is null");
    }
    if (world.isGameOver()) {
      return;
    }
    if (world.getPlayerNumber() == 0 || world.getMaxTurn() == 0
            || world.getMaxItemNumCarried() == 0) {
      throw new IllegalStateException("game information isn't complete");
    }
    try {
      Files.createDirectories(Paths.get("runtime_res"));
    } catch (IOException ioe) {
      view.showWarning(ioe.getMessage());
      return;
    }
    final String filePath = "runtime_res/map.png";
    final int scaleMapRatio = 40;
    world.outputMapImage(filePath, 40);
    view.resetAllData();

    List<int[]> roomBounds = new ArrayList<>();
    for (int i = 0; i < world.getRoomNumber(); ++i) {
      int[] ltCorner = world.getRoomLeftTopCorner(i);
      int[] rbCorner = world.getRoomRightBottomCorner(i);
      int width = scaleMapRatio * (rbCorner[1] - ltCorner[1] + 1);
      int height = scaleMapRatio * (rbCorner[0] - ltCorner[0] + 1);
      roomBounds.add(
              new int[]{(int) (scaleMapRatio * ltCorner[1] + scaleMapRatio * 0.5f),
                        (int) (scaleMapRatio * ltCorner[0] + scaleMapRatio * 0.5f), width, height});
    }
    List<String> playerIconPaths = new ArrayList<>();
    for (int i = 0; i < world.getPlayerNumber(); ++i) {
      playerIconPaths.add(String.format("runtime_res/character/%03d.png", (i + 1)));
    }
    view.initMap(filePath, roomBounds, playerIconPaths, "runtime_res/character/boss.png");
    view.showPrompt(CommandPromptType.START_GAME_PROMPT.toString());
    view.setViewLock(false, true);
  }

  @Override
  public String toString() {
    return "StartGameCommand";
  }
}
