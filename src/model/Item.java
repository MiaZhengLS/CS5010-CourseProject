package model;

/**
 * Represents items of the game. Unable to move(for now)
 * Implements Character interface.
 */
public interface Item {

  /**
   * Mark the item as used.
   */
  void setUsed();

  /**
   * Check if an item has been used by a player in an attack.
   *
   * @return true if the item has been used, false if not
   */
  boolean hasBeenUsed();

  /**
   * Get the index of the item.
   *
   * @return the index of the item
   */
  public int getIndex();

  /**
   * Get the attack of the item.
   *
   * @return the attack of the item
   */
  public int getAttack();

  /**
   * Get the name of the item.
   *
   * @return item's name
   */
  public String getName();

  /**
   * Get the room index where the item is in.
   *
   * @return index of the room
   */
  public int getRoomIdx();
}
