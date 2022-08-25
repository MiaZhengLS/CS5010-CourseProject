package model;

import java.util.Collection;

/**
 * Represents an AI player in the game.
 */
public interface PlayerAi extends Player {

  /**
   * Get the action for the AI player.
   *
   * @return action for the AI player
   */
  PlayerActionType getRandomAction();

  /**
   * Get the next pre-defined action and its parameter
   * wrapped in a AiAction instance.
   *
   * @return next pre-defined action and its parameter
   */
  AiActionParam getPredefinedAction();

  /**
   * Get a random number in the given set.
   *
   * @param resultSet set for choosing a returned number
   * @return a random number in the given set
   * @throws IllegalArgumentException set is null or empty
   */
  int getRandomNumber(Collection<Integer> resultSet) throws IllegalArgumentException;

  /**
   * Get a random integer between the min integer(inclusive)
   * and the max integer(exclusive).
   *
   * @param min lower range edge
   * @param max higher range edge
   * @return a random integer within the range
   * @throws IllegalArgumentException max is smaller than min
   */
  int getRandomNumberBetween(int min, int max) throws IllegalArgumentException;

  /**
   * Whether the AI is using pre-defined actions.
   *
   * @return true for using pre-defined actions, false for not
   */
  boolean usePredefinedAction();
}
