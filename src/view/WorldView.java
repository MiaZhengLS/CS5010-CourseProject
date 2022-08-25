package view;

import controller.WorldFeatureController;
import java.awt.Rectangle;
import java.util.List;
import model.WorldViewModel;

/**
 * This is the interface for the view in the game.
 * It offers GUI-related functions to offer necessary information to the user.
 */
public interface WorldView {

  /**
   * This is used to show turn information to the user.
   *
   * @param str information to be shown
   * @throws IllegalArgumentException str is null
   */
  void showTurnInfo(String str) throws IllegalArgumentException;

  /**
   * This is used to show turn result to the user.
   *
   * @param str information to be shown
   * @throws IllegalArgumentException str is null
   */
  void showTurnResult(String str) throws IllegalArgumentException;

  /**
   * This is used to show prompt to the user.
   *
   * @param str information to be shown
   * @throws IllegalArgumentException str is null
   */
  void showPrompt(String str) throws IllegalArgumentException;

  /**
   * This is used to show warning to the user.
   *
   * @param str information to be shown
   * @throws IllegalArgumentException str is null
   */
  void showWarning(String str) throws IllegalArgumentException;

  /**
   * This is used to show game result to the user.
   *
   * @param str information to be shown
   * @throws IllegalArgumentException str is null
   */
  void showGameResult(String str) throws IllegalArgumentException;

  /**
   * This is used to move the graphical player to a given room.
   * This should be called after initMap and initPlayers, or the view has no idea
   * about the bounds of rooms, and the player graphical representations.
   * The playerIdx and roomIdx are the same as the indexes when initializing
   * the lists of rooms' and players' graphical representations.
   *
   * @param playerIdx the index of the player in the player list
   * @param roomIdx   the index of the room in the room list
   * @throws IllegalArgumentException playerIdx or roomIdx is negative or
   *                                  exceeds the length of the list
   * @throws IllegalStateException    the graphical player isn't initialized
   */
  void movePlayerTo(int playerIdx, int roomIdx)
          throws IllegalArgumentException;

  /**
   * This is used to move the graphical target character to a given room.
   * This should be called after initMap and initTargetCharacter, or the view has no idea
   * about the bounds of rooms, and the player graphical representations.
   * The roomIdx is the same as the indexes when initializing
   * the lists of rooms' graphical representations.
   *
   * @param roomIdx the index of the room in the room list
   * @throws IllegalArgumentException roomIdx is negative or
   *                                  exceeds the length of the list
   * @throws IllegalStateException    the graphical target character isn't initialized
   */
  void moveTargetCharacterTo(int roomIdx)
          throws IllegalArgumentException, IllegalStateException;

  /**
   * Show current player's information.
   *
   * @param str information to be shown
   * @throws IllegalArgumentException str is null
   */
  void showPlayerInfo(String str) throws IllegalArgumentException;

  /**
   * Clear information shown on the right-side panel.
   * This will clear the turn information, player information and user prompts.
   */
  void resetAllInfo();

  /**
   * Reset all data cached to record view status.
   * This should be called when a new game is about to start.
   */
  void resetAllData();

  /**
   * This is used to initialize the world map as well as rooms' bounds in the map.
   * Since the room bounds won't change during a single round of game, this will be cached
   * in the view for future queries.
   *
   * @param mapImgFilePath the file path of the map image
   * @param roomBounds     the rooms' bounds in the map
   * @param playerIcons    file paths of the players' images
   * @param targetIcon     file path of the target character's image
   * @throws IllegalArgumentException mapImgFilePath is null or empty, or roomBounds
   *                                  is null, or playerIcons is null,
   *                                  or targetIcon is null or empty
   */
  void initMap(String mapImgFilePath, List<int[]> roomBounds,
               List<String> playerIcons, String targetIcon)
          throws IllegalArgumentException;

  /**
   * This is used to show the dialog that allows users to specify information of the players.
   *
   * @throws IllegalStateException WorldFeatureController isn't set
   */
  void showInitPlayerPanel(int maxPlayerNum) throws IllegalStateException;

  /**
   * This will close the player specification dialog if it is opened.
   */
  void closeInitPlayerPanel();

  /**
   * This is used to show the dialog that allows users to specify
   * the max item number carried by each player.
   *
   * @throws IllegalStateException WorldFeatureController isn't set
   */
  void showInitMaxItemCarriedPanel() throws IllegalStateException;

  /**
   * This will close the max item specification dialog if it is opened.
   */
  void closeInitMaxItemCarriedPanel();

  /**
   * This is used to show the dialog that allows users to specify the max turn,
   * which marks the end of the game.
   *
   * @throws IllegalStateException WorldFeatureController isn't set
   */
  void showInitMaxTurnPanel() throws IllegalStateException;

  /**
   * This will close the max turn specification dialog if it is opened.
   */
  void closeInitMaxTurnPanel();

  /**
   * This is used to show the dialog that allows users to select between given items to pick up.
   *
   * @param items the item list that allows for selection
   * @throws IllegalArgumentException items is null or empty
   * @throws IllegalStateException    WorldFeatureController isn't set
   */
  void showPickupItemPanel(List<Integer> items)
          throws IllegalArgumentException, IllegalStateException;

  /**
   * This will close the item selection dialog if it is opened.
   */
  void closePickupItemPanel();

  /**
   * This is used to show the dialog that allows users to select a room to move the pet to.
   *
   * @throws IllegalStateException WorldFeatureController isn't set
   */
  void showMovePetPanel() throws IllegalStateException;

  /**
   * This will close the room selection dialog if it is opened.
   */
  void closeMovePetPanel();

  /**
   * This is used to show the dialog that allows users to select between given items for attack.
   *
   * @param items the item list that allows for selection
   * @throws IllegalArgumentException items is null or empty
   * @throws IllegalStateException    WorldFeatureController isn't set
   */
  void showUseItemPanel(List<Integer> items) throws IllegalArgumentException, IllegalStateException;

  /**
   * This will close the item selection dialog if it is opened.
   */
  void closeUseItemPanel();

  /**
   * This is used to put 'lock' on user input.
   * The lockGameInput can be false only when game can be continued, otherwise it will be false,
   * which means the command for 'Continue game' won't be executed.
   * The lockTurnInput can be false only when a new turn starts and user can choose an
   * action for the current player, otherwise it will be false, which means
   * the commands for the turn won't be executed.
   * So only when the lockGameInput is true will the lockTurnInput be checked.
   *
   * @param lockGameInput whether the game can be continued
   * @param lockTurnInput whether the current player can be manipulated
   */
  void setViewLock(boolean lockGameInput, boolean lockTurnInput);

  /**
   * This is used to set the view model for the view.
   * With view model, a view can access data in the model in an immutable manner for display.
   *
   * @param m view model
   * @throws IllegalArgumentException view model is null
   */
  void setViewModel(WorldViewModel m) throws IllegalArgumentException;

  /**
   * This is used to set the feature controller for the view.
   * The feature controller is responsible for executing commands thrown by the view.
   *
   * @param f feature controller
   * @throws IllegalArgumentException feature controller is null
   */
  void setFeatures(WorldFeatureController f) throws IllegalArgumentException;
}
