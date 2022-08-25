package model;

/**
 * Represents a room in the world.
 * Implements Space interface.
 */
public interface Room {

  /**
   * Get the index of the room.
   * The index is defined in the room list of the world.
   *
   * @return the index of the room
   */
  public int getIndex();

  /**
   * Get the width of the room.
   *
   * @return the width of the room
   */
  public int getWidth();

  /**
   * Get the height of the room.
   *
   * @return the height of the room
   */
  public int getHeight();

  /**
   * Get the name of the room.
   *
   * @return the name of the room
   */
  public String getName();

  /**
   * Get the left-top corner in form {row,column} of the room.
   *
   * @return the left-top corner of the room
   */
  public int[] getLeftTopCorner();

  /**
   * Get the right-bottom corner in form {row,column} of the room.
   *
   * @return the right-bottom corner of the room
   */
  public int[] getRightBottomCorner();
}
