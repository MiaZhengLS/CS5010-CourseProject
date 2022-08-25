package model;

/**
 * Represents a human player in the game.
 */
public class PlayerHumanImpl implements Player {

  private final String name;
  private int roomIdx;

  /**
   * Constructor.
   *
   * @param name    name of the player
   * @param roomIdx room index of the player
   * @throws IllegalArgumentException roomIdx is negative, or name is invalid,
   */
  public PlayerHumanImpl(String name, int roomIdx) throws IllegalArgumentException {
    if (roomIdx < 0) {
      throw new IllegalArgumentException("roomIdx shouldn't be negative!");
    }
    if (name == null || name.isEmpty() || name.trim().isEmpty()) {
      throw new IllegalArgumentException("name isn't valid!");
    }
    this.name = name;
    this.roomIdx = roomIdx;
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
   * @return Human for Human player
   */
  @Override
  public PlayerType getPlayerType() {
    return PlayerType.HUMAN;
  }

  /**
   * Get the information of the player in string form.
   *
   * @return information of the player in string form
   */
  @Override
  public String toString() {
    return String.format("Player name:%s.\nRoom index:%d.\nType: %s\n", name, roomIdx,
            getPlayerType().toString());
  }
}
