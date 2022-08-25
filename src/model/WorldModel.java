package model;

import java.io.UncheckedIOException;
import java.util.List;


/**
 * Represents a single game of World.
 * The world consists of rooms, items, players and target player.
 */
public interface WorldModel extends WorldViewModel {

  /**
   * Reinitialize the model. This will use previous cached config file path.
   *
   * @throws IllegalStateException not initialized yet
   */
  void reinitializeWithCurrentConfig() throws IllegalStateException;

  /**
   * Reinitialize the model with given file path.
   *
   * @param readable a readable object which contains necessary information to construct a world
   * @throws IllegalArgumentException any information within the config file is invalid
   */
  void reinitializeWithNewConfig(Readable readable) throws IllegalArgumentException;

  /**
   * Set the players' information.
   *
   * @param name    name list of players
   * @param roomIdx room list of players
   * @param isAi    list indicates whether players are AIs
   * @throws IllegalArgumentException any list is null,
   *                                  or empty,
   *                                  or the lists are of different size.
   *                                  or any room's index is out of range
   */
  void setPlayers(List<String> name, List<Integer> roomIdx, List<Boolean> isAi)
          throws IllegalArgumentException;

  /**
   * Set the players' information with players created.
   *
   * @param players pre-created players
   * @throws IllegalArgumentException players is null or empty
   */
  void setPlayers(List<Player> players) throws IllegalArgumentException;

  /**
   * Set the max turn for the game.
   * If the turn reaches max turn, the game will end.
   *
   * @param turn max turn number
   * @throws IllegalArgumentException turn is less than one
   */
  void setMaxTurn(int turn) throws IllegalArgumentException;

  /**
   * Set the max item each player can carry.
   * If the player's item number reaches this number,
   * the player cannot pick up any item anymore.
   *
   * @param maxItemCarried max turn number
   * @throws IllegalArgumentException maxItemCarried is less than one
   */
  void setMaxItemCarried(int maxItemCarried) throws IllegalArgumentException;

  /**
   * Move a pet to a given room.µ
   *
   * @param roomIdx index of the given room
   * @throws IllegalStateException    game ends or current player isn't a human player
   * @throws IllegalArgumentException roomIdx is negative or exceeds max room index
   */
  void curHumanPlayerMovePet(int roomIdx) throws IllegalStateException, IllegalArgumentException;

  /**
   * Attempt an attack on the target character.
   * If the current player carries items, then it will choose
   * the one with the highest attack.
   * Else the current player has to poke eyes.
   * An attack can be successfully done only if the player isn't seen
   * by any other player.
   *
   * @param itemIdx the item the player chooses to use
   * @throws IllegalStateException    game ends or current player isn't a human player
   * @throws IllegalArgumentException the item isn't held by this player,
   *                                  or has been used
   */
  void curHumanPlayerAttackTargetCharacter(int itemIdx)
          throws IllegalStateException, IllegalArgumentException;

  /**
   * Get action of current AI player.
   *
   * @throws IllegalStateException if current player isn't AI
   */
  AiActionParam aiPlayerDoAction() throws IllegalStateException;

  /**
   * Moves current human player.
   *
   * @param roomIdx index of the room that current player will move to
   * @throws IllegalArgumentException roomIdx isn't neighbor of current room of current player,
   *                                  roomIdx is negative or exceeds max room index,
   *                                  or is equal to current room index
   * @throws IllegalStateException    game ends or current player isn't human player
   */
  void curHumanPlayerMove(int roomIdx) throws IllegalArgumentException, IllegalStateException;

  /**
   * Display information of neighbor rooms of the room current player is in.
   *
   * @throws IllegalStateException game ends
   */
  void curHumanPlayerDisplayNeighborRooms() throws IllegalStateException;

  /**
   * Current human player picks up the item of given index.
   *
   * @param itemIdx index of the item
   * @throws IllegalArgumentException itemIdx isn't in the room current player is in
   * @throws IllegalStateException    game ends, or current player isn't human,
   *                                  or the player's item number has reached the limit
   */
  void curHumanPlayerPickupItem(int itemIdx) throws IllegalArgumentException, IllegalStateException;

  /**
   * Output the map of the rooms to a given image file.
   *
   * @param imgScale    how much should the map be scaled, expect an even integer
   * @param imgFilePath file path of the image
   * @throws IllegalArgumentException imgScale is less than one
   * @throws UncheckedIOException     deleting old file fails, or writing image to file fails
   */
  void outputMapImage(String imgFilePath, int imgScale)
          throws IllegalArgumentException, UncheckedIOException;

}
