package model;

import java.util.Collection;

/**
 * Represents an AI player in the game.
 */
public class PlayerAiImpl implements PlayerAi {

  private final String name;
  private int roomIdx;
  private final AiActionController actionController;

  /**
   * Constructor.
   * actionController can be pre-defined or random.
   *
   * @param name             name of the player
   * @param roomIdx          room index of the player
   * @param actionController for controlling the AI player
   * @throws IllegalArgumentException roomIdx is negative, or name is invalid,
   *                                  or actionController is null
   */
  public PlayerAiImpl(String name, int roomIdx, AiActionController actionController)
          throws IllegalArgumentException {
    if (roomIdx < 0) {
      throw new IllegalArgumentException("roomIdx shouldn't be negative!");
    }
    if (name == null || name.isEmpty() || name.trim().isEmpty()) {
      throw new IllegalArgumentException("name isn't valid!");
    }
    if (actionController == null) {
      throw new IllegalArgumentException("actionController == null!");
    }
    this.name = name;
    this.roomIdx = roomIdx;
    this.actionController = actionController;
  }

  /**
   * Get name of the player.
   *
   * @return name of the player
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * Get room index of the player.
   *
   * @return room index of the player
   */
  @Override
  public int getRoomIdx() {
    return roomIdx;
  }

  /**
   * Move the player to a room specified.
   *
   * @param roomIdx index of the room
   */
  @Override
  public void moveTo(int roomIdx) throws IllegalArgumentException {
    if (roomIdx < 0) {
      throw new IllegalArgumentException("room index can't be negative!");
    }
    this.roomIdx = roomIdx;
  }

  /**
   * Whether the player is human or AI.
   *
   * @return AI for AI player
   */
  @Override
  public PlayerType getPlayerType() {
    return PlayerType.AI;
  }

  /**
   * Get the action for the AI player.
   *
   * @return action for the AI player
   */
  @Override
  public PlayerActionType getRandomAction() {
    return actionController.getRandomAction();
  }

  /**
   * Get the next pre-defined action and its parameter
   * wrapped in a AiAction instance.
   *
   * @return next pre-defined action and its parameter
   */
  @Override
  public AiActionParam getPredefinedAction() {
    return actionController.getPredefinedAction();
  }

  /**
   * Whether the AI is using pre-defined actions.
   *
   * @return true for using pre-defined actions, false for not
   */
  @Override
  public boolean usePredefinedAction() {
    return actionController.usePredefinedAction();
  }

  /**
   * Get a random number in the given set.
   *
   * @param resultSet set for choosing a returned number
   * @return a random number in the given set
   * @throws IllegalArgumentException set is null or empty
   */
  @Override
  public int getRandomNumber(Collection<Integer> resultSet) throws IllegalArgumentException {
    return actionController.getRandomNumber(resultSet);
  }

  @Override
  public int getRandomNumberBetween(int min, int max) throws IllegalArgumentException {
    return actionController.getRandomNumberBetween(min, max);
  }

  /**
   * Get the information of the player in string form.
   *
   * @return information of the player in string form
   */
  @Override
  public String toString() {
    return String.format("Player name:%s. Room index:%d. Type: %s\n", name, roomIdx,
            getPlayerType().toString());
  }
}
