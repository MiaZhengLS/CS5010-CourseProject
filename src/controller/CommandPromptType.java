package controller;

/**
 * This is an enumeration class for storing all messages that might be displayed
 * more than once during a game.
 * These messages can be user prompts or warnings.
 */
public enum CommandPromptType {
  START_GAME_PROMPT("Press 'C' to start game\n"),
  USER_PROMPT("Press 'C' to continue game\n"),
  PLAYER_ACTION(new StringBuilder()
          .append("Click a neighbor room to move to\n")
          .append("Press 'P' to pick up an item in the room\n")
          .append("Press 'L' to look around\n")
          .append("Press 'A' to attempt an attack on the target\n")
          .append("Press 'M' to move the pet\n")
          .append("Please make your choice\n").toString()),
  TURN_INFO("Current turn is %d\n"
          + "Current player is %s, order is %d\n"
          + "%s carries %d items, is in %d-indexed room %s\n"),
  GAME_ENDS("Game ends.\n"),
  GAME_OVER("Game Over.\n"),
  PICK_ITEM_INDEX("Enter the index of the item the player will pick:\n"),
  MOVE_ROOM_INDEX("Enter the index of the room the player will move to:\n"),
  MOVE_PET_ROOM_INDEX("Enter the index of the room the pet will move to:\n"),
  USE_ITEM_INDEX("Enter the index of the item the player will use:\n"),
  ITEM_CARRIED("Items carried:\n%s"),

  SETUP_CONFIG_PATH("Please enter the config file path:\n"),
  SETUP_PLAYER_NUM("Please enter the number of players:\n"),
  SETUP_PLAYER_NAME("Please enter the name of player %d:\n"),
  SETUP_PLAYER_POS("Please enter the initial room index of player %d:\n"),
  SETUP_PLAYER_TYPE("Is player %d a human player? y for yes, n for no:\n"),
  SETUP_MAX_TURN("Please enter the max turn of the game:\n"),
  SETUP_MAX_ITEM_NUM("Please enter the max item number a player can carry:\n"),
  SETUP_AI_CONTROL("Do you want to control the AI player?  y for yes, n for no:\n"),
  SETUP_AI_CONTROL_PARAM("Please enter action parameters:\n"),
  GAME_START("--Game start--\n"),
  WARN_ITEM_NOT_IN_ROOM("%d-indexed item isn't in %d-indexed room!\n"),
  WARN_ITEM_INDEX_INVALID("%d is negative or exceeds max item index\n"),
  WARN_ITEM_NOT_CARRIED("%d-indexed item is not carried by current player!\n"),
  WARN_ITEM_REACH_LIMIT("The number of items the player carries has reached limit %d!\n"),
  WARN_NO_ITEM_IN_ROOM("No item is in the %d-indexed room!\n"),
  WARN_NOT_NEIGHBOR("%d-indexed room isn't neighbor of %d-indexed room!\n"),
  WARN_NO_NEIGHBOR("The room the player is in has no neighbor!\n"),
  WARN_ROOM_INVALID("Room index can't be negative or exceed %d!\n"),
  WARN_MAX_ITEM_INVALID("Max item number must be positive, please try again!\n"),
  WARN_MAX_TURN_INVALID("Max turn must be positive, please try again!\n"),
  WARN_CMD_INVALID("Command invalid, please try again!\n"),
  WARN_ATTACK_NOT_SAME_ROOM("Not in the same room with target character!\n"),
  WARN_CONFIG_INVALID("Config file invalid, please enter a valid file path!\n");


  private final String str;

  CommandPromptType(String str) {
    this.str = str;
  }

  @Override
  public String toString() {
    return str;
  }
}
