package model;

import java.util.Objects;

/**
 * Represents a room in the world.
 * Implements Space interface.
 */
public class RoomImpl implements Room {

  private final int index;
  private final String name;
  private final int[] leftTopCorner;
  private final int[] rightBottomCorner;
  private final int width;
  private final int height;
  private String stringForm;

  /**
   * Constructor of the Room class.
   *
   * @param index             index of the room
   * @param name              name of the room
   * @param leftTopCorner     left-top corner of the room,in form {row,column}
   * @param rightBottomCorner right-bottom corner of the room,in form {row,column}
   * @throws IllegalArgumentException numeric parameters are negative, or left-top corner's
   *                                  row or column is less than that of right-bottom corner,
   *                                  or name is null or empty
   */
  public RoomImpl(int index, String name, int[] leftTopCorner, int[] rightBottomCorner)
          throws IllegalArgumentException {
    if (index < 0) {
      throw new IllegalArgumentException("index shouldn't be negative!");
    }
    if (name == null || name.isEmpty() || name.trim().isEmpty()) {
      throw new IllegalArgumentException("name isn't valid!");
    }
    if (leftTopCorner == null || rightBottomCorner == null) {
      throw new IllegalArgumentException("leftTopCorner and rightBottomCorner can't be null");
    }
    if (leftTopCorner.length < 2 || rightBottomCorner.length < 2) {
      throw new IllegalArgumentException("corner array should have at least two elements!");
    }
    if (leftTopCorner[0] < 0 || leftTopCorner[1] < 0
            || rightBottomCorner[0] < 0 || rightBottomCorner[1] < 0) {
      throw new IllegalArgumentException("row and column shouldn't be negative!");
    }
    if (leftTopCorner[0] >= rightBottomCorner[0]
            || leftTopCorner[1] >= rightBottomCorner[1]) {
      throw new IllegalArgumentException(String.format("left-top corner's row and column "
              + "should be less than that of right-bottom corner. room's index is:", index));
    }
    this.index = index;
    this.name = name.trim();
    this.leftTopCorner = new int[]{leftTopCorner[0], leftTopCorner[1]};
    this.rightBottomCorner = new int[]{rightBottomCorner[0], rightBottomCorner[1]};
    this.width = rightBottomCorner[1] - leftTopCorner[1] + 1;
    this.height = rightBottomCorner[0] - leftTopCorner[0] + 1;
    stringForm = "";
  }

  /**
   * Check if two rooms share a wall.
   *
   * @param r1            room to be checked
   * @param r2            the other room to be checked
   * @param wallThickness the unit offset between neighbor rooms
   * @return true if two rooms are adjacent, false if not
   * @throws IllegalArgumentException r1 or r2 is null, or wallThickness is negative
   */
  public static boolean isRoomAdjacent(Room r1, Room r2, int wallThickness)
          throws IllegalArgumentException {
    if (r1 == null || r2 == null) {
      throw new IllegalArgumentException("parameter shouldn't be null!");
    }
    if (wallThickness < 0) {
      throw new IllegalArgumentException("wallThickness shouldn't be negative!");
    }
    int[] lt1 = r1.getLeftTopCorner();
    int[] rb1 = r1.getRightBottomCorner();
    int[] lt2 = r2.getLeftTopCorner();
    int[] rb2 = r2.getRightBottomCorner();
    int left1 = lt1[1];
    int right1 = rb1[1];
    int left2 = lt2[1];
    int right2 = rb2[1];

    int top1 = lt1[0];
    int btm1 = rb1[0];
    int top2 = lt2[0];
    int btm2 = rb2[0];

    if ((left1 - wallThickness == right2 || left2 - wallThickness == right1)
            && top1 <= btm2 && btm1 >= top2) {
      return true;
    }
    if ((top1 - wallThickness == btm2 || top2 - wallThickness == btm1)
            && left1 <= right2 && right1 >= left2) {
      return true;
    }
    return false;
  }

  /**
   * Check if two rooms overlap.
   *
   * @param r1 room to be checked
   * @param r2 the other room to be checked
   * @return true if two rooms overlap, false if not
   * @throws IllegalArgumentException r1 or r2 is null
   */
  public static boolean isRoomOverlapped(Room r1, Room r2) throws IllegalArgumentException {
    if (r1 == null || r2 == null) {
      throw new IllegalArgumentException("parameter shouldn't be null!");
    }
    int[] lt1 = r1.getLeftTopCorner();
    int[] rb1 = r1.getRightBottomCorner();
    int[] lt2 = r2.getLeftTopCorner();
    int[] rb2 = r2.getRightBottomCorner();
    int left1 = lt1[1];
    int right1 = rb1[1];
    int left2 = lt2[1];
    int right2 = rb2[1];

    int top1 = lt1[0];
    int btm1 = rb1[0];
    int top2 = lt2[0];
    int btm2 = rb2[0];

    if (btm2 > top1 && top2 < btm1 && right2 > left1 && left2 < right1) {
      return true;
    }
    return false;
  }

  /**
   * Get the index of the room.
   * The index is defined in the room list of the world.
   *
   * @return the index of the room
   */
  @Override
  public int getIndex() {
    return index;
  }

  /**
   * Get the width of the room.
   *
   * @return the width of the room
   */
  @Override
  public int getWidth() {
    return width;
  }

  /**
   * Get the height of the room.
   *
   * @return the height of the room
   */
  @Override
  public int getHeight() {
    return height;
  }

  /**
   * Get the name of the room.
   *
   * @return the name of the room
   */
  @Override
  public String getName() {
    return name;
  }

  /**
   * Get the left-top corner in form {row,column} of the room.
   *
   * @return the left-top corner of the room
   */
  @Override
  public int[] getLeftTopCorner() {
    return new int[]{leftTopCorner[0], leftTopCorner[1]};
  }

  /**
   * Get the right-bottom corner in form {row,column} of the room.
   *
   * @return the right-bottom corner of the room
   */
  @Override
  public int[] getRightBottomCorner() {
    return new int[]{rightBottomCorner[0], rightBottomCorner[1]};
  }

  /**
   * Determine if the other object is equal to this room.
   * If two room have the same extent and name, then they are equal.
   *
   * @param o the other object
   * @return true if the other object is equal to this room,false if not equal
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    RoomImpl item = (RoomImpl) o;

    int[] lt = item.getLeftTopCorner();
    int[] rb = item.getRightBottomCorner();
    if (leftTopCorner[0] != lt[0] || leftTopCorner[1] != lt[1]
            || rightBottomCorner[0] != rb[0] || rightBottomCorner[1] != rb[1]) {
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
    int result = Objects.hash(name);
    result = 31 * result + leftTopCorner[0];
    result = 31 * result + leftTopCorner[1];
    result = 31 * result + rightBottomCorner[0];
    result = 31 * result + rightBottomCorner[1];
    return result;
  }

  /**
   * Get the string representation of the room.
   *
   * @return the string representation of the room
   */
  @Override
  public String toString() {
    if (stringForm == null || stringForm.isEmpty()) {
      stringForm = String.format("Room index:%d. Name:%s. Left-top:{%d,%d}. "
                      + "Right-bottom:{%d,%d}. Width:%d. Height:%d.\n",
              index, name, leftTopCorner[0], leftTopCorner[1],
              rightBottomCorner[0], rightBottomCorner[1], width, height);
    }
    return stringForm;
  }
}
