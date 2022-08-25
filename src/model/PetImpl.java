package model;

/**
 * This is an implementation class for Pet interface.
 * A pet has a name and a certain position represented as the index of the room
 * defined by the world rules.
 * A pet can be moved by any player to any place, and it can
 * also move during every turn in a dfs order.
 * Every time a pet is moved, all elements in its dfs record are erased.
 */
public class PetImpl implements Pet {

  private final String name;
  private int roomIdx;

  /**
   * Constructor.
   *
   * @param roomIdx the room where the pet is
   * @param name name of the pet
   * @throws IllegalArgumentException roomIdx is negative, or name is null or empty
   */
  public PetImpl(int roomIdx, String name) throws IllegalArgumentException {
    if (roomIdx < 0) {
      throw new IllegalArgumentException("roomIdx shouldn't be negative!");
    }
    if (name == null || name.isEmpty() || name.trim().isEmpty()) {
      throw new IllegalArgumentException("name isn't valid!");
    }
    this.roomIdx = roomIdx;
    this.name = name;
  }

  @Override
  public int getRoomIdx() {
    return roomIdx;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public void moveTo(int roomIdx) throws IllegalArgumentException {
    if (roomIdx < 0) {
      throw new IllegalArgumentException("roomIdx shouldn't be negative!");
    }
    this.roomIdx = roomIdx;
  }

  /**
   * Get the string representation of the pet.
   *
   * @return the string representation of the pet
   */
  @Override
  public String toString() {
    return String.format("Name:%s. RoomIdx:%d.\n", name, roomIdx);
  }
}
