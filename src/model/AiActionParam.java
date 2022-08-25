package model;

/**
 * Helps store a pair of data: action type and its corresponding parameter.
 */
public interface AiActionParam {

  /**
   * Get the action type.
   *
   * @return action type
   */
  public PlayerActionType getActionType();

  /**
   * Get the action parameter.
   *
   * @return action parameter
   */
  public int getActionParameter();
}
