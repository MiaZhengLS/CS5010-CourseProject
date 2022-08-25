package model;

/**
 * Represents a player in the game.
 */
public interface Player {

  /**
   * Get name of the player.
   *
   * @return name of the player
   */
  String getName();

  /**
   * Get room index of the player.
   *
   * @return room index of the player
   */
  int getRoomIdx();

  /**
   * Move the player to a room specified.
   *
   * @param roomIdx index of the room
   */
  void moveTo(int roomIdx);

  /**
   * Whether the player is human or AI.
   *
   * @return Human if player is human. AI if player is AI.
   */
  PlayerType getPlayerType();
}
