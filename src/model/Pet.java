package model;

/**
 * Pet of the target character.
 * It enters the game in the same space as the target character.
 * Any space that is occupied by the pet cannot be seen by its neighbors
 * making it virtually invisible to the user.
 * It can be moved by a player.
 */
public interface Pet {

  /**
   * Get the room index where the pet is in.
   *
   * @return index of the room
   */
  int getRoomIdx();

  /**
   * Get the name of the target character.
   *
   * @return pet's name
   */
  String getName();

  /**
   * Move the target character to a given room.
   *
   * @param roomIdx the index of the target room
   * @throws IllegalArgumentException roomIdx is negative
   */
  void moveTo(int roomIdx) throws IllegalArgumentException;
}
