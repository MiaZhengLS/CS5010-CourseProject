package model;

/**
 * Represents the target character of the game.Able to move from room to room in order.
 * Implements Character interface.
 */
public class TargetCharacterImpl implements TargetCharacter {

  private final String name;
  private int roomIdx;
  private int health;

  /**
   * Constructor of the TargetCharacter class.
   *
   * @param name    name of the target character
   * @param health  name of the target character
   * @param roomIdx the index of the room the target character is in.
   * @throws IllegalArgumentException index or roomIdx are negative, or attack isn't positive,
   *                                  or name is null or empty
   */
  public TargetCharacterImpl(String name, int health, int roomIdx) throws IllegalArgumentException {
    if (roomIdx < 0) {
      throw new IllegalArgumentException("roomIdx shouldn't be negative!");
    }
    if (name == null || name.isEmpty() || name.trim().isEmpty()) {
      throw new IllegalArgumentException("name isn't valid!");
    }
    if (health <= 0) {
      throw new IllegalArgumentException("health should be positive!");
    }
    this.name = name.trim();
    this.health = health;
    this.roomIdx = roomIdx;
  }

  /**
   * Get the health of the target character.
   *
   * @return the health of the target character
   */
  @Override
  public int getHealth() {
    return health;
  }

  /**
   * Attacked and health reduced.
   *
   * @param healthReduce how much will health of the target character be reduced
   * @throws IllegalArgumentException the parameter is negative
   */
  @Override
  public void attacked(int healthReduce) throws IllegalArgumentException {
    if (healthReduce < 0) {
      throw new IllegalArgumentException("health reduce shouldn't be negative.");
    }
    if (health <= healthReduce) {
      health = 0;
    } else {
      health -= healthReduce;
    }
  }

  /**
   * Move the target character to given room.
   * Unable to move when health is zero.
   *
   * @param roomIdx the index of the target room
   * @throws IllegalArgumentException roomIdx is negative
   */
  public void moveTo(int roomIdx) throws IllegalArgumentException, IllegalStateException {
    if (roomIdx < 0) {
      throw new IllegalArgumentException("roomIdx shouldn't be negative!");
    }
    if (health == 0) {
      throw new IllegalStateException("health is zero!");
    }
    this.roomIdx = roomIdx;
  }

  /**
   * Get the name of the target character.
   *
   * @return target character's name
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * Get the room index where the target character is in.
   *
   * @return index of the room
   */
  @Override
  public int getRoomIdx() {
    return roomIdx;
  }

  /**
   * Get the string representation of the target character.
   *
   * @return the string representation of the target character
   */
  @Override
  public String toString() {
    return String.format("Name:%s. RoomIdx:%d. Health:%d.\n", name, roomIdx, health);
  }
}
