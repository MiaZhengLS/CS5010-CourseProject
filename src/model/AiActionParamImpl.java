package model;

/**
 * Helps store a pair of data: action type and its corresponding parameter.
 */
public class AiActionParamImpl implements AiActionParam {
  private final PlayerActionType playerActionType;
  private final int parameter;

  /**
   * Constructor.
   *
   * @param playerActionType the action type
   * @param parameter the parameter related to that action
   */
  public AiActionParamImpl(PlayerActionType playerActionType, int parameter) {
    this.playerActionType = playerActionType;
    this.parameter = parameter;
  }

  /**
   * Get the action type.
   *
   * @return action type
   */
  @Override
  public PlayerActionType getActionType() {
    return playerActionType;
  }

  /**
   * Get the action parameter.
   *
   * @return action parameter
   */
  @Override
  public int getActionParameter() {
    return parameter;
  }
}
