package model;

/**
 * Represents the target character of the game.Able to move from room to room in order.
 * Implements Character interface.
 */
public interface TargetCharacter {
  /**
   * Get the health of the target character.
   *
   * @return the health of the target character
   */
  public int getHealth();

  /**
   * Attacked and health reduced.
   *
   * @param healthReduce how much will health of the target character be reduced
   * @throws IllegalArgumentException the parameter is negative
   */
  public void attacked(int healthReduce) throws IllegalArgumentException;

  /**
   * Move the target character to given room.
   * Unable to move when health is zero.
   *
   * @param roomIdx the index of the target room
   * @throws IllegalArgumentException roomIdx is negative
   */
  public void moveTo(int roomIdx) throws IllegalArgumentException;

  /**
   * Get the name of the target character.
   *
   * @return target character's name
   */
  public String getName();

  /**
   * Get the room index where the target character is in.
   *
   * @return index of the room
   */
  public int getRoomIdx();
}
