package model;

import java.util.Collection;

/**
 * Controller for AI actions.
 */
public interface AiActionController {

  /**
   * Get the next action of AI.
   *
   * @return next action of AI
   */
  PlayerActionType getRandomAction();

  /**
   * Get a random number in the given set.
   *
   * @param resultSet set for choosing a returned number
   * @return a random number in the given set
   * @throws IllegalArgumentException set is null or empty
   * @throws IllegalStateException the instance doesn't use random
   */
  int getRandomNumber(Collection<Integer> resultSet)
          throws IllegalArgumentException, IllegalStateException;

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
   * Get the next pre-defined action and its parameter
   * wrapped in a AiAction instance.
   *
   * @return next pre-defined action and its parameter
   */
  AiActionParam getPredefinedAction();

  /**
   * Whether the AI is using pre-defined actions.
   *
   * @return true for using pre-defined actions, false for not
   */
  boolean usePredefinedAction();
}
