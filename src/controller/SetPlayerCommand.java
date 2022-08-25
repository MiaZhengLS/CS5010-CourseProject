package controller;

import java.util.ArrayList;
import java.util.List;
import model.WorldModel;
import view.WorldView;

/**
 * Command for setting the players' information.
 */
public class SetPlayerCommand implements WorldCommand {

  private final List<String> names;
  private final List<Integer> rooms;
  private final List<Boolean> isAi;

  /**
   * Constructor of the SetPlayerCommand.
   *
   * @param names names of the players
   * @param rooms rooms of the players
   * @param isAi  types of the players
   * @throws IllegalArgumentException any of these parameters is null,
   *                                  or three lists are not the same size, or lists are empty
   */
  public SetPlayerCommand(List<String> names, List<Integer> rooms, List<Boolean> isAi)
          throws IllegalArgumentException {
    if (names == null || rooms == null || isAi == null) {
      throw new IllegalArgumentException("parameter is null");
    }
    if (names.size() != rooms.size() || rooms.size() != isAi.size()) {
      throw new IllegalArgumentException("list isn't of the same size");
    }
    if (names.isEmpty()) {
      throw new IllegalArgumentException("list is empty");
    }
    this.names = new ArrayList<>(names);
    this.rooms = new ArrayList<>(rooms);
    this.isAi = new ArrayList<>(isAi);
  }

  /**
   * Execute command.
   * After player information is set, the dialog for setting max item will be shown.
   *
   * @param world model for executing
   * @param view  view for display
   * @param param is ignored here
   * @throws IllegalArgumentException model or view is null, or all players are AIs
   */
  @Override
  public void execute(WorldModel world, WorldView view, Object param)
          throws IllegalArgumentException {
    if (world == null) {
      throw new IllegalArgumentException("model is null");
    }
    if (view == null) {
      throw new IllegalArgumentException("view is null");
    }
    boolean checkAllAi = true;
    for (boolean ai : isAi) {
      if (!ai) {
        checkAllAi = false;
        break;
      }
    }
    if (checkAllAi) {
      view.showWarning("There must be at least one human player");
      return;
    }
    try {
      world.setPlayers(names, rooms, isAi);
      //System.out.println("#1");
      view.closeInitPlayerPanel();
      view.showInitMaxItemCarriedPanel();
    } catch (IllegalArgumentException iae) {
      view.showWarning(iae.getMessage());
      return;
    }
  }

  @Override
  public String toString() {
    return "SetPlayerCommand";
  }
}
