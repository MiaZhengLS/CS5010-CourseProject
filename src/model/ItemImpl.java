package model;

import java.util.Objects;

/**
 * Represents items of the game. Unable to move(for now)
 * Implements Character interface.
 */
public class ItemImpl implements Item {
  private final int index;
  private final String name;
  private final int attack;
  private final int roomIdx;
  private boolean used;

  /**
   * Constructor of the Item class.
   *
   * @param index   the item's index in the list of items.
   * @param name    the item's name.
   * @param attack  the item's attack.
   * @param roomIdx the index of the room the item is in.
   * @throws IllegalArgumentException index or roomIdx are negative, or attack isn't positive,
   *                                  or name is null or empty
   */
  public ItemImpl(int index, String name, int attack, int roomIdx) throws IllegalArgumentException {
    if (index < 0) {
      throw new IllegalArgumentException("index shouldn't be negative!");
    }
    if (roomIdx < 0) {
      throw new IllegalArgumentException("roomIdx shouldn't be negative!");
    }
    if (name == null || name.isEmpty() || name.trim().isEmpty()) {
      throw new IllegalArgumentException("name isn't valid!");
    }
    if (attack <= 0) {
      throw new IllegalArgumentException("attack should be positive!");
    }
    this.index = index;
    this.name = name.trim();
    this.attack = attack;
    this.roomIdx = roomIdx;
    this.used = false;
  }

  @Override
  public void setUsed() {
    this.used = true;
  }

  @Override
  public boolean hasBeenUsed() {
    return used;
  }

  /**
   * Get the index of the item.
   *
   * @return the index of the item
   */
  @Override
  public int getIndex() {
    return index;
  }

  /**
   * Get the attack of the item.
   *
   * @return the attack of the item
   */
  @Override
  public int getAttack() {
    return attack;
  }

  /**
   * Get the name of the item.
   *
   * @return item's name
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * Get the room index where the item is in.
   *
   * @return index of the room
   */
  @Override
  public int getRoomIdx() {
    return roomIdx;
  }

  /**
   * Get the string representation of the item.
   *
   * @return the string representation of the item
   */
  @Override
  public String toString() {
    return String.format("Index:%d. Name:%s. RoomIdx:%d. Attack:%d.\n",
            index, name, roomIdx, attack);
  }

  /**
   * Determine if the other object is equal to this item.
   *
   * @param o the other object
   * @return true if the other object is equal to this item
   *         false if not equal
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    ItemImpl item = (ItemImpl) o;

    if (index != item.index) {
      return false;
    }
    if (attack != item.attack) {
      return false;
    }
    if (roomIdx != item.roomIdx) {
      return false;
    }
    return name.equals(item.name);
  }

  /**
   * If two items are equal, they have the same hashcode.
   *
   * @return hashcode of the item
   */
  @Override
  public int hashCode() {
    int result = index;
    result = 31 * result + Objects.hash(name);
    result = 31 * result + attack;
    result = 31 * result + roomIdx;
    return result;
  }
}
