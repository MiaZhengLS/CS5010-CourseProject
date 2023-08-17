# CS5010 Course Project

This repo represents the coursework for CS 5010, the Fall 2022 Edition!

**Author:** Mia Zheng


### About/Overview
This program provides a GUI for users to play the game. 
In this game, every player can choose an action from 5 actions which forms a turn. 
The game ends when the target character is killed, or max turn is reached.
The 5 actions are: 
1. Pick up an item in the room
2. Move to a neighbor room
3. Display information of neighbor rooms
4. Attack the target character
5. Move the pet


### List of Features
1. Set up a world with current/new config file. 
2. Set max turn and max item carried, add human and AI players to the game. 
3. Move the player to a neighbor room. 
4. Let a player pick up an item in the room. 
5. Let a player look around by displaying information of neighbor rooms.
6. Let a player attempt an attack on the target character.
7. Let a player move the pet to any room.
8. Pet itself moves in every turn after player's action in a dfs order.
9. Pet hides information of a room, which helps players within it to attempt an attack.
10. Show current player's information by clicking the player's graphical representation.
11. Quit the game.


### How to Run
Windows: 
Open 'CMD' window, navigate to the directory of the .jar file(res\jar).
Then enter 'java -jar cs5010-project-YantingZheng.jar "..\configs\GravityFalls.txt" 20'.

Mac:
Open 'Terminal' window, navigate to the directory of the .jar file(res/jar).
Then enter 'java -jar cs5010-project-YantingZheng.jar ../configs/GravityFalls.txt 20'.

The first parameter of the executable is the path of config file. There are 4 config files under res/jarfile directory. Use any that suits your need. 
The second parameter is the max turn. You can set it to any integer bigger than zero.


### How to Use the Program
First you will be asked to set up the world.
You need to enter the path of the config file and max turn as parameter to run the .jar

Then you will see the GUI. The initial window provides information about the author. 
On the left top you will see menu. In the menu, you can choose from:
1. New Game with current config
2. New Game with new config
3. Quit Game
If you choose the first, you will be asked to provide information of players, and max item number a player can carry.
If you choose the second, you need to provide a valid world specification file and max turn before setting the information mentioned in the first.
If you choose to quit, then the application will exit.

After the game starts, you will see the map, players, target character and information on the right.
Before every turn, you will be offered this command:
Press 'C' to start game

If you press 'C' on your keyboard, you intend to start a new turn. You will see current turn's player's information on the right. If current turn's player is an AI, then you will only see the turn result since AIs aren't controlled by the user.

If current player is human, you will be offered these commands for this turn:
1. Click a neighbor room to move to
2. Press 'P' to pick up an item in the room
3. Press 'L' to look around
4. Press 'A' to attempt an attack on the target
5. Press 'M' to move the pet
2, 3, 5 may need further input. So after you make a choice, there may be a dialog that demands further data to finish the command.

If the game ends after the last turn ends, there will be a popup showing the result of the game.
You can start a new game any time.

### Example Runs


### Design/Model Changes
Change model's contructor to make it accept a Readable object as the parameter.
Add reinitialize to the model interface to make it reinitialize with new parameter without creating a new model instance.
Add some getter functions to make sure there is enough information to build a GUI.

### Assumptions
Users need to re-input the max turn if they choose to start a game with a new config file.


### Limitations
Currently my application can meet the requirements of Milestone4.


### Citations
For milestone4, my main references come from Oracle's java swing tutorials and stackoverflow.com.
https://docs.oracle.com/javase/tutorial/uiswing/index.html
https://stackoverflow.com/
The images used for players and target character come from
https://www.figma.com/community/file/874561187582459048


