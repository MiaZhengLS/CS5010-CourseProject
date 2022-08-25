package model;

/**
 * Display possible action types for player.
 */
public enum PlayerActionType {
  MOVE,
  PICKUP_ITEM,
  LOOK_AROUND,
  ATTACK,
  MOVE_PET;

  private static final int length;

  static {
    length = values().length;
  }

  /**
   * Get the amount of action types.
   *
   * @return amount of action types
   */
  public static int getLength() {
    return length;
  }
}
