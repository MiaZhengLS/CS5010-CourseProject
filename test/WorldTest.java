import static org.junit.Assert.assertEquals;

import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import model.AiActionControllerImpl;
import model.AiActionParam;
import model.Player;
import model.PlayerActionType;
import model.PlayerAiImpl;
import model.PlayerHumanImpl;
import model.PlayerType;
import model.WorldModel;
import model.WorldModelImpl;
import org.junit.Before;
import org.junit.Test;


/**
 * Test for class World.
 */
public class WorldTest {

  private String filePath;
  private String filePath2;
  private int firstPlayerRoomIdx;
  private String firstPlayerName;
  private int secondPlayerRoomIdx;
  private String secondPlayerName;
  private int thirdPlayerRoomIdx;
  private String thirdPlayerName;
  private int maxTurn;
  private int maxItemCarried;

  /**
   * Build necessary instance variables for testing.
   */
  @Before
  public void setUp() {
    filePath = "GravityFalls.txt";
    filePath2 = "GravityFalls2.txt";

    firstPlayerRoomIdx = 1;
    secondPlayerRoomIdx = 2;
    thirdPlayerRoomIdx = 13;
    firstPlayerName = "Phi";
    secondPlayerName = "Wil";
    thirdPlayerName = "Bak";
    maxTurn = 20;
    maxItemCarried = 2;
  }

  @Test
  public void testConstructorAndGetters() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    assertEquals(world.getName(), "Gravity Falls");
    assertEquals(world.getWidth(), 29);
    assertEquals(world.getHeight(), 21);
    assertEquals(world.getRoomNumber(), 20);
    assertEquals(world.getItemNumber(), 20);
    assertEquals(world.getRoomName(0), "Tombstone");
    assertEquals(world.getRoomName(1), "Hell's Kitchen");
    assertEquals(world.isGameOver(), false);
    assertEquals(world.getMaxTurn(), 0);
    assertEquals(world.getMaxItemNumCarried(), 0);
    assertEquals(world.getCurPlayerIdx(), 0);
    assertEquals(world.getPlayerNumber(), 0);
    assertEquals(world.getTargetCharacterRoomIdx(), 0);
    assertEquals(world.getItemAttack(0), 3);
    assertEquals(world.getItemName(0), "Crepe Pan");

    world.setMaxTurn(20);
    world.setMaxItemCarried(2);
    assertEquals(world.getMaxTurn(), 20);
    assertEquals(world.getMaxItemNumCarried(), 2);

    List<String> names = new ArrayList<>();
    names.add("aaa");
    names.add("bb");
    List<Integer> rooms = new ArrayList<>();
    rooms.add(12);
    rooms.add(4);

    List<Boolean> isAi = new ArrayList<>();
    isAi.add(false);
    isAi.add(true);
    world.setPlayers(names, rooms, isAi);
    assertEquals(world.getPlayerNumber(), 2);
    assertEquals(world.getPlayerRoomIdx(0), 12);
    assertEquals(world.getPlayerRoomIdx(1), 4);
    assertEquals(world.getCurPlayerType(), PlayerType.HUMAN);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testNullReadable() {
    WorldModel world = new WorldModelImpl(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEmptyContent() {
    WorldModel world = new WorldModelImpl(new StringReader("  "));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testOverlappedRoom() {
    StringReader sr = new StringReader(
            "20 28 Gravity Falls\n"
                    + "50 Bravo Mabel\n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + " 0  0  3  3 Tombstone\n"
                    + " 0  0  4  4 Hell's Kitchen\n"
                    + "2\n"
                    + "0 3 Crepe Pan\n"
                    + "1 2 Letter Opener\n");
    WorldModel world = new WorldModelImpl(sr);
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidConstructorNoHeight() {
    StringReader sr = new StringReader(
            " 28 Gravity Falls\n"
                    + "50 Bravo Mabel\n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + " 0  0  3  3 Tombstone\n"
                    + " 0  4  5  7 Hell's Kitchen\n"
                    + "2\n"
                    + "0 3 Crepe Pan\n"
                    + "1 2 Letter Opener\n");
    WorldModel world = new WorldModelImpl(sr);
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidConstructorNoTarget() {
    StringReader sr = new StringReader(
            " 28 Gravity Falls\n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + " 0  0  3  3 Tombstone\n"
                    + " 0  4  5  7 Hell's Kitchen\n"
                    + "2\n"
                    + "0 3 Crepe Pan\n"
                    + "1 2 Letter Opener\n");
    WorldModel world = new WorldModelImpl(sr);
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidConstructorNoRoom() {
    StringReader sr = new StringReader(
            " 28 Gravity Falls\n"
                    + "50 Bravo Mabel\n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + "0 3 Crepe Pan\n"
                    + "4 2 Letter Opener\n");
    WorldModel world = new WorldModelImpl(sr);
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidConstructorNoItem() {
    StringReader sr = new StringReader(
            " 28 Gravity Falls\n"
                    + "50 Bravo Mabel\n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + " 0  0  3  3 Tombstone\n"
                    + " 0  4  5  7 Hell's Kitchen\n");
    WorldModel world = new WorldModelImpl(sr);
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidConstructorItemRoomIndexError() {
    StringReader sr = new StringReader(
            " 28 Gravity Falls\n"
                    + "50 Bravo Mabel\n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + " 0  0  3  3 Tombstone\n"
                    + " 0  4  5  7 Hell's Kitchen\n"
                    + "2\n"
                    + "3 3 Crepe Pan\n"
                    + "4 2 Letter Opener\n");
    WorldModel world = new WorldModelImpl(sr);
  }

  @Test(expected = IllegalStateException.class)
  public void testInvalidPlayerRoomIndexError() {
    StringReader sr = new StringReader(
            " 28 Gravity Falls\n"
                    + "50 Bravo Mabel\n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + " 0  0  3  3 Tombstone\n"
                    + " 0  4  5  7 Hell's Kitchen\n"
                    + "2\n"
                    + "0 3 Crepe Pan\n"
                    + "1 2 Letter Opener\n");
    WorldModel world = new WorldModelImpl(sr);
    List<String> names = new ArrayList<>();
    List<Integer> rooms = new ArrayList<>();
    List<Boolean> ais = new ArrayList<>();
    names.add("aa");
    rooms.add(3);
    ais.add(false);
    world.setPlayers(names, rooms, ais);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setWorldWidthNegative() {
    StringReader sr = new StringReader(
            "20 -28 Gravity Falls\n"
                    + "50 Bravo Mabel\n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + " 0  0  3  3 Tombstone\n"
                    + " 0  4  5  7 Hell's Kitchen\n"
                    + "2\n"
                    + "0 3 Crepe Pan\n"
                    + "1 2 Letter Opener\n");
    WorldModel world = new WorldModelImpl(sr);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setWorldWidthZero() {
    StringReader sr = new StringReader(
            "20 0 Gravity Falls\n"
                    + "50 Bravo Mabel\n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + " 0  0  3  3 Tombstone\n"
                    + " 0  4  5  7 Hell's Kitchen\n"
                    + "2\n"
                    + "0 3 Crepe Pan\n"
                    + "1 2 Letter Opener\n");
    WorldModel world = new WorldModelImpl(sr);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setWorldHeightNegative() {
    StringReader sr = new StringReader(
            "-20 28 Gravity Falls\n"
                    + "50 Bravo Mabel\n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + " 0  0  3  3 Tombstone\n"
                    + " 0  4  5  7 Hell's Kitchen\n"
                    + "2\n"
                    + "0 3 Crepe Pan\n"
                    + "1 2 Letter Opener\n");
    WorldModel world = new WorldModelImpl(sr);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setWorldHeightZero() {
    StringReader sr = new StringReader(
            "0 28 Gravity Falls\n"
                    + "50 Bravo Mabel\n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + " 0  0  3  3 Tombstone\n"
                    + " 0  4  5  7 Hell's Kitchen\n"
                    + "2\n"
                    + "0 3 Crepe Pan\n"
                    + "1 2 Letter Opener\n");
    WorldModel world = new WorldModelImpl(sr);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setWorldNameEmpty() {
    StringReader sr = new StringReader(
            "20 28 \n"
                    + "50 Bravo Mabel\n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + " 0  0  3  3 Tombstone\n"
                    + " 0  4  5  7 Hell's Kitchen\n"
                    + "2\n"
                    + "0 3 Crepe Pan\n"
                    + "1 2 Letter Opener\n");
    WorldModel world = new WorldModelImpl(sr);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setWorldNameWithSpace() {
    StringReader sr = new StringReader(
            "20 28    \n"
                    + "50 Bravo Mabel\n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + " 0  0  3  3 Tombstone\n"
                    + " 0  4  5  7 Hell's Kitchen\n"
                    + "2\n"
                    + "0 3 Crepe Pan\n"
                    + "1 2 Letter Opener\n");
    WorldModel world = new WorldModelImpl(sr);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setTargetCharacterHealthNegative() {
    StringReader sr = new StringReader(
            "20 28 Gravity Falls\n"
                    + "-3 Bravo Mabel\n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + " 0  0  3  3 Tombstone\n"
                    + " 0  4  5  7 Hell's Kitchen\n"
                    + "2\n"
                    + "0 3 Crepe Pan\n"
                    + "1 2 Letter Opener\n");
    WorldModel world = new WorldModelImpl(sr);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setTargetCharacterHealthZero() {
    StringReader sr = new StringReader(
            "20 28 Gravity Falls\n"
                    + "0 Bravo Mabel\n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + " 0  0  3  3 Tombstone\n"
                    + " 0  4  5  7 Hell's Kitchen\n"
                    + "2\n"
                    + "0 3 Crepe Pan\n"
                    + "1 2 Letter Opener\n");
    WorldModel world = new WorldModelImpl(sr);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setTargetCharacterNameEmpty() {
    StringReader sr = new StringReader(
            "20 28 Gravity Falls\n"
                    + "50 \n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + " 0  0  3  3 Tombstone\n"
                    + " 0  4  5  7 Hell's Kitchen\n"
                    + "2\n"
                    + "0 3 Crepe Pan\n"
                    + "1 2 Letter Opener\n");
    WorldModel world = new WorldModelImpl(sr);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setTargetCharacterNameWithSpace() {
    StringReader sr = new StringReader(
            "20 28 Gravity Falls\n"
                    + "50  \n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + " 0  0  3  3 Tombstone\n"
                    + " 0  4  5  7 Hell's Kitchen\n"
                    + "2\n"
                    + "0 3 Crepe Pan\n"
                    + "1 2 Letter Opener\n");
    WorldModel world = new WorldModelImpl(sr);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setMaxTurnNegative() {
    StringReader sr = new StringReader(
            "20 28 Gravity Falls\n"
                    + "50 Bravo Mabel\n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + " 0  0  3  3 Tombstone\n"
                    + " 0  4  5  7 Hell's Kitchen\n"
                    + "2\n"
                    + "0 3 Crepe Pan\n"
                    + "1 2 Letter Opener\n");
    WorldModel world = new WorldModelImpl(sr);
    world.setMaxTurn(-2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setMaxTurnZero() {
    StringReader sr = new StringReader(
            "20 28 Gravity Falls\n"
                    + "50 Bravo Mabel\n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + " 0  0  3  3 Tombstone\n"
                    + " 0  4  5  7 Hell's Kitchen\n"
                    + "2\n"
                    + "0 3 Crepe Pan\n"
                    + "1 2 Letter Opener\n");
    WorldModel world = new WorldModelImpl(sr);
    world.setMaxTurn(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setMaxItemCarriedNegative() {
    StringReader sr = new StringReader(
            "20 28 Gravity Falls\n"
                    + "50 Bravo Mabel\n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + " 0  0  3  3 Tombstone\n"
                    + " 0  4  5  7 Hell's Kitchen\n"
                    + "2\n"
                    + "0 3 Crepe Pan\n"
                    + "1 2 Letter Opener\n");
    WorldModel world = new WorldModelImpl(sr);
    world.setMaxItemCarried(-2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setMaxItemCarriedZero() {
    StringReader sr = new StringReader(
            "20 28 Gravity Falls\n"
                    + "50 Bravo Mabel\n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + " 0  0  3  3 Tombstone\n"
                    + " 0  4  5  7 Hell's Kitchen\n"
                    + "2\n"
                    + "0 3 Crepe Pan\n"
                    + "1 2 Letter Opener\n");
    WorldModel world = new WorldModelImpl(sr);
    world.setMaxItemCarried(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void addRoomIndexNegative() {
    StringReader sr = new StringReader(
            "20 28 Gravity Falls\n"
                    + "50 Bravo Mabel\n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + " 0  0  3  3 Tombstone\n"
                    + " 0  4  5  7 Hell's Kitchen\n"
                    + "2\n"
                    + "0 3 Crepe Pan\n"
                    + "1 2 Letter Opener\n");
    WorldModel world = new WorldModelImpl(sr);
  }

  @Test(expected = IllegalArgumentException.class)
  public void addRoomNameEmpty() {
    StringReader sr = new StringReader(
            "20 28 Gravity Falls\n"
                    + "50 Bravo Mabel\n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + " 0  0  3  3 \n"
                    + " 0  4  5  7 Hell's Kitchen\n"
                    + "2\n"
                    + "0 3 Crepe Pan\n"
                    + "1 2 Letter Opener\n");
    WorldModel world = new WorldModelImpl(sr);
  }

  @Test(expected = IllegalArgumentException.class)
  public void addRoomNameWithSpace() {
    StringReader sr = new StringReader(
            "20 28 Gravity Falls\n"
                    + "50 Bravo Mabel\n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + " 0  0  3  3   \n"
                    + " 0  4  5  7 Hell's Kitchen\n"
                    + "2\n"
                    + "0 3 Crepe Pan\n"
                    + "1 2 Letter Opener\n");
    WorldModel world = new WorldModelImpl(sr);
  }

  @Test(expected = IllegalArgumentException.class)
  public void addPlayerRoomIndexNegative() {
    StringReader sr = new StringReader(
            "20 28 Gravity Falls\n"
                    + "50 Bravo Mabel\n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + " 0  0  3  3 Tombstone\n"
                    + " 0  4  5  7 Hell's Kitchen\n"
                    + "2\n"
                    + "0 3 Crepe Pan\n"
                    + "1 2 Letter Opener\n");
    WorldModel world = new WorldModelImpl(sr);
    List<String> names = new ArrayList<>();
    List<Integer> rooms = new ArrayList<>();
    List<Boolean> ais = new ArrayList<>();
    names.add("aa");
    rooms.add(-3);
    ais.add(false);
    world.setPlayers(names, rooms, ais);
  }

  @Test(expected = IllegalArgumentException.class)
  public void addPlayerNameDuplicate() {
    StringReader sr = new StringReader(
            "20 28 Gravity Falls\n"
                    + "50 Bravo Mabel\n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + " 0  0  3  3 Tombstone\n"
                    + " 0  4  5  7 Hell's Kitchen\n"
                    + "2\n"
                    + "0 3 Crepe Pan\n"
                    + "1 2 Letter Opener\n");
    WorldModel world = new WorldModelImpl(sr);
    List<String> names = new ArrayList<>();
    List<Integer> rooms = new ArrayList<>();
    List<Boolean> ais = new ArrayList<>();
    names.add("aa");
    names.add("aa");
    rooms.add(0);
    rooms.add(0);
    ais.add(false);
    ais.add(false);
    world.setPlayers(names, rooms, ais);
  }

  @Test(expected = IllegalArgumentException.class)
  public void addRoomLeftCornerRowNegative() {
    StringReader sr = new StringReader(
            "20 28 Gravity Falls\n"
                    + "50 Bravo Mabel\n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + " 0  0  3  3 Tombstone\n"
                    + "-5  0  4  4 Hell's Kitchen\n"
                    + "2\n"
                    + "0 3 Crepe Pan\n"
                    + "1 2 Letter Opener\n");
    WorldModel world = new WorldModelImpl(sr);
  }

  @Test(expected = IllegalArgumentException.class)
  public void addRoomLeftCornerColNegative() {
    StringReader sr = new StringReader(
            "20 28 Gravity Falls\n"
                    + "50 Bravo Mabel\n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + " 0  0  3  3 Tombstone\n"
                    + " 0  -3  4  4 Hell's Kitchen\n"
                    + "2\n"
                    + "0 3 Crepe Pan\n"
                    + "1 2 Letter Opener\n");
    WorldModel world = new WorldModelImpl(sr);
  }

  @Test(expected = IllegalArgumentException.class)
  public void addRoomRightCornerRowNegative() {
    StringReader sr = new StringReader(
            "20 28 Gravity Falls\n"
                    + "50 Bravo Mabel\n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + " 0  0  -3  3 Tombstone\n"
                    + " 0  4  5  7 Hell's Kitchen\n"
                    + "2\n"
                    + "0 3 Crepe Pan\n"
                    + "1 2 Letter Opener\n");
    WorldModel world = new WorldModelImpl(sr);
  }

  @Test(expected = IllegalArgumentException.class)
  public void addRoomRightCornerColNegative() {
    StringReader sr = new StringReader(
            "20 28 Gravity Falls\n"
                    + "50 Bravo Mabel\n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + " 0  0  3  -3 Tombstone\n"
                    + " 0  4  5  7 Hell's Kitchen\n"
                    + "2\n"
                    + "0 3 Crepe Pan\n"
                    + "1 2 Letter Opener\n");
    WorldModel world = new WorldModelImpl(sr);
  }

  @Test(expected = IllegalArgumentException.class)
  public void addItemIndexNegative() {
    StringReader sr = new StringReader(
            "20 28 Gravity Falls\n"
                    + "50 Bravo Mabel\n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + " 0  0  3  3 Tombstone\n"
                    + " 0  4  5  7 Hell's Kitchen\n"
                    + "2\n"
                    + "0 3 Crepe Pan\n"
                    + "-1 2 Letter Opener\n");
    WorldModel world = new WorldModelImpl(sr);
  }

  @Test(expected = IllegalArgumentException.class)
  public void addItemNameEmpty() {
    StringReader sr = new StringReader(
            "20 28 Gravity Falls\n"
                    + "50 Bravo Mabel\n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + " 0  0  3  3 Tombstone\n"
                    + " 0  4  5  7 Hell's Kitchen\n"
                    + "2\n"
                    + "0 3 \n"
                    + "1 2 Letter Opener\n");
    WorldModel world = new WorldModelImpl(sr);
  }

  @Test(expected = IllegalArgumentException.class)
  public void addItemNameWithSpace() {
    StringReader sr = new StringReader(
            "20 28 Gravity Falls\n"
                    + "50 Bravo Mabel\n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + " 0  0  3  3 Tombstone\n"
                    + " 0  4  5  7 Hell's Kitchen\n"
                    + "2\n"
                    + "0 3   \n"
                    + "1 2 Letter Opener\n");
    WorldModel world = new WorldModelImpl(sr);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setItemAttackNegative() {
    StringReader sr = new StringReader(
            "20 28 Gravity Falls\n"
                    + "50 Bravo Mabel\n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + " 0  0  3  3 Tombstone\n"
                    + " 0  4  5  7 Hell's Kitchen\n"
                    + "2\n"
                    + "0 -3 Crepe Pan\n"
                    + "1 2 Letter Opener\n");
    WorldModel world = new WorldModelImpl(sr);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setItemAttackZero() {
    StringReader sr = new StringReader(
            "20 28 Gravity Falls\n"
                    + "50 Bravo Mabel\n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + " 0  0  3  3 Tombstone\n"
                    + " 0  4  5  7 Hell's Kitchen\n"
                    + "2\n"
                    + "0 0 Crepe Pan\n"
                    + "1 2 Letter Opener\n");
    WorldModel world = new WorldModelImpl(sr);
  }

  @Test
  public void getAdjacentRoomIdxMultiple() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    List<Integer> neighborRooms = world.getRoomNeighbors(1);
    List<Integer> expected = new ArrayList<>();
    expected.add(0);
    expected.add(2);
    expected.add(5);
    assertEquals(true, neighborRooms.containsAll(expected));
  }

  @Test
  public void getAdjacentRoomIdxOne() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    List<Integer> neighborRooms = world.getRoomNeighbors(0);
    List<Integer> expected = new ArrayList<>();
    expected.add(1);
    assertEquals(true, neighborRooms.containsAll(expected));
  }

  @Test
  public void getItemsInRoomMultiple() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    List<Integer> items = world.getRoomItems(2);
    List<Integer> expected = new ArrayList<>();
    expected.add(6);
    expected.add(11);
    assertEquals(true, items.containsAll(expected));
  }

  @Test
  public void getItemsInRoomOne() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    List<Integer> items = world.getRoomItems(1);
    List<Integer> expected = new ArrayList<>();
    expected.add(8);
    assertEquals(true, items.containsAll(expected));
  }

  @Test
  public void getItemsInRoomZero() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    List<Integer> items = world.getRoomItems(3);
    assertEquals(true, items.isEmpty());
  }

  //one item, neighbor
  //with target player in it
  @Test
  public void printRoomInfoWithOneItem() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    String info = world.printRoomInfo(0);
    String expected = "--1 items are in the room:\n"
            + "\tIndex:4. Name:Revolver. RoomIdx:0. Attack:3.\n"
            + "--Target character is in the room:\n"
            + "\tName:Bravo Mabel. RoomIdx:0. Health:50.\n"
            + "--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--1 adjacent rooms:\n"
            + "\tRoom index:1. Name:Hell's Kitchen.\n";
    assertEquals(expected, info);
  }

  //no item,player,target player
  @Test
  public void printRoomInfoWithZeroItem() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    String info = world.printRoomInfo(3);
    String expected = "--5 adjacent rooms:\n"
            + "\tRoom index:9. Name:Black Island.\n"
            + "\tRoom index:14. Name:Satan's Kingdom.\n"
            + "\tRoom index:15. Name:Devil's Den.\n"
            + "\tRoom index:16. Name:West Kill.\n"
            + "\tRoom index:17. Name:Black Creek.\n";
    assertEquals(expected, info);
  }

  //many player,items,neighbors
  @Test
  public void printRoomInfoWithManyPlayer() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    List<String> names = new ArrayList<>();
    List<Integer> rooms = new ArrayList<>();
    List<Boolean> ais = new ArrayList<>();
    names.add("aa");
    names.add("bb");
    names.add("cc");
    rooms.add(3);
    rooms.add(3);
    rooms.add(3);
    ais.add(false);
    ais.add(false);
    ais.add(false);
    world.setPlayers(names, rooms, ais);
    String info = world.printRoomInfo(3);
    String expected = "--3 players are in the room:\n"
            + "\taa\n"
            + "\tbb\n"
            + "\tcc\n"
            + "--5 adjacent rooms:\n"
            + "\tRoom index:9. Name:Black Island.\n"
            + "\tRoom index:14. Name:Satan's Kingdom.\n"
            + "\tRoom index:15. Name:Devil's Den.\n"
            + "\tRoom index:16. Name:West Kill.\n"
            + "\tRoom index:17. Name:Black Creek.\n";
    assertEquals(expected, info);
  }

  @Test
  public void printRoomInfoAfterItemOut() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    List<String> names = new ArrayList<>();
    List<Integer> rooms = new ArrayList<>();
    List<Boolean> ais = new ArrayList<>();
    names.add("aa");
    rooms.add(1);
    ais.add(false);
    world.setPlayers(names, rooms, ais);
    world.setMaxTurn(30);
    world.setMaxItemCarried(3);
    String info = world.printRoomInfo(1);
    String expected = "--1 items are in the room:\n"
            + "\tIndex:8. Name:Billiard Cue. RoomIdx:1. Attack:2.\n"
            + "--1 players are in the room:\n"
            + "\taa\n"
            + "--3 adjacent rooms:\n"
            + "\tRoom index:0. Name:Tombstone.\n"
            + "\tRoom index:2. Name:Kill Devil Hills.\n"
            + "\tRoom index:5. Name:Deadwood.\n";
    assertEquals(expected, info);
    world.curHumanPlayerPickupItem(8);
    info = world.printRoomInfo(1);
    expected = "--Target character is in the room:\n"
            + "\tName:Bravo Mabel. RoomIdx:1. Health:50.\n"
            + "--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--1 players are in the room:\n"
            + "\taa\n"
            + "--3 adjacent rooms:\n"
            + "\tRoom index:0. Name:Tombstone.\n"
            + "\tRoom index:2. Name:Kill Devil Hills.\n"
            + "\tRoom index:5. Name:Deadwood.\n";
    assertEquals(expected, info);
  }

  @Test
  public void printRoomInfoAfterPlayerIn() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    List<String> names = new ArrayList<>();
    List<Integer> rooms = new ArrayList<>();
    List<Boolean> ais = new ArrayList<>();
    names.add("aa");
    rooms.add(0);
    ais.add(false);
    world.setPlayers(names, rooms, ais);
    world.setMaxTurn(30);
    world.setMaxItemCarried(3);
    String info = world.printRoomInfo(1);
    String expected = "--1 items are in the room:\n"
            + "\tIndex:8. Name:Billiard Cue. RoomIdx:1. Attack:2.\n"
            + "--3 adjacent rooms:\n"
            + "\tRoom index:0. Name:Tombstone.\n"
            + "\tRoom index:2. Name:Kill Devil Hills.\n"
            + "\tRoom index:5. Name:Deadwood.\n";
    assertEquals(expected, info);
    world.curHumanPlayerMove(1);
    info = world.printRoomInfo(1);
    expected = "--1 items are in the room:\n"
            + "\tIndex:8. Name:Billiard Cue. RoomIdx:1. Attack:2.\n"
            + "--Target character is in the room:\n"
            + "\tName:Bravo Mabel. RoomIdx:1. Health:50.\n"
            + "--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--1 players are in the room:\n"
            + "\taa\n"
            + "--3 adjacent rooms:\n"
            + "\tRoom index:0. Name:Tombstone.\n"
            + "\tRoom index:2. Name:Kill Devil Hills.\n"
            + "\tRoom index:5. Name:Deadwood.\n";
    assertEquals(expected, info);
    info = world.printRoomInfo(0);
    expected = "--1 items are in the room:\n"
            + "\tIndex:4. Name:Revolver. RoomIdx:0. Attack:3.\n"
            + "--1 adjacent rooms:\n"
            + "\tRoom index:1. Name:Hell's Kitchen.\n";
    assertEquals(expected, info);
  }

  @Test
  public void printRoomInfoAfterPlayerOut() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    List<String> names = new ArrayList<>();
    List<Integer> rooms = new ArrayList<>();
    List<Boolean> ais = new ArrayList<>();
    names.add("aa");
    rooms.add(0);
    ais.add(false);
    world.setPlayers(names, rooms, ais);
    world.setMaxTurn(30);
    world.setMaxItemCarried(3);
    String info = world.printRoomInfo(0);
    String expected = "--1 items are in the room:\n"
            + "\tIndex:4. Name:Revolver. RoomIdx:0. Attack:3.\n"
            + "--Target character is in the room:\n"
            + "\tName:Bravo Mabel. RoomIdx:0. Health:50.\n"
            + "--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--1 players are in the room:\n"
            + "\taa\n"
            + "--1 adjacent rooms:\n"
            + "\tRoom index:1. Name:Hell's Kitchen.\n";
    assertEquals(expected, info);
    world.curHumanPlayerMove(1);
    info = world.printRoomInfo(0);
    expected = "--1 items are in the room:\n"
            + "\tIndex:4. Name:Revolver. RoomIdx:0. Attack:3.\n"
            + "--1 adjacent rooms:\n"
            + "\tRoom index:1. Name:Hell's Kitchen.\n";
    assertEquals(expected, info);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayerMoveToInvalidRoom() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    List<String> names = new ArrayList<>();
    List<Integer> rooms = new ArrayList<>();
    List<Boolean> ais = new ArrayList<>();
    names.add("aa");
    rooms.add(0);
    ais.add(false);
    world.setPlayers(names, rooms, ais);
    world.setMaxTurn(30);
    world.setMaxItemCarried(3);
    world.curHumanPlayerMove(3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayerPickItemNotInRoom() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    List<String> names = new ArrayList<>();
    List<Integer> rooms = new ArrayList<>();
    List<Boolean> ais = new ArrayList<>();
    names.add("aa");
    rooms.add(0);
    ais.add(false);
    world.setPlayers(names, rooms, ais);
    world.setMaxTurn(30);
    world.setMaxItemCarried(3);
    world.curHumanPlayerPickupItem(3);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayerPickItemOutRange() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    List<String> names = new ArrayList<>();
    List<Integer> rooms = new ArrayList<>();
    List<Boolean> ais = new ArrayList<>();
    names.add("aa");
    rooms.add(0);
    ais.add(false);
    world.setPlayers(names, rooms, ais);
    world.setMaxTurn(30);
    world.setMaxItemCarried(3);
    world.curHumanPlayerPickupItem(41);
  }

  @Test
  public void testPlayerLookAroundItems() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    List<String> names = new ArrayList<>();
    List<Integer> rooms = new ArrayList<>();
    List<Boolean> ais = new ArrayList<>();
    names.add("aa");
    rooms.add(3);
    ais.add(false);
    world.setPlayers(names, rooms, ais);
    world.setMaxTurn(30);
    world.setMaxItemCarried(3);
    world.curHumanPlayerDisplayNeighborRooms();
    String info = world.getLastTurnMsg();
    String expected = "The room current player is in:\n"
            + "Room index:3. Name:Skull Valley.\n"
            + "--1 players are in the room:\n"
            + "\taa\n"
            + "--5 adjacent rooms:\n"
            + "\tRoom index:9. Name:Black Island.\n"
            + "\tRoom index:14. Name:Satan's Kingdom.\n"
            + "\tRoom index:15. Name:Devil's Den.\n"
            + "\tRoom index:16. Name:West Kill.\n"
            + "\tRoom index:17. Name:Black Creek.\n"
            + "Neighbor rooms:\n"
            + "Room index:9. Name:Black Island.\n"
            + "--1 items are in the room:\n"
            + "\tIndex:18. Name:Silken Cord. RoomIdx:9. Attack:3.\n"
            + "--3 adjacent rooms:\n"
            + "\tRoom index:3. Name:Skull Valley.\n"
            + "\tRoom index:11. Name:Scarville.\n"
            + "\tRoom index:17. Name:Black Creek.\n"
            + "Room index:14. Name:Satan's Kingdom.\n"
            + "--4 adjacent rooms:\n"
            + "\tRoom index:2. Name:Kill Devil Hills.\n"
            + "\tRoom index:3. Name:Skull Valley.\n"
            + "\tRoom index:4. Name:Seven Devils.\n"
            + "\tRoom index:15. Name:Devil's Den.\n"
            + "Room index:15. Name:Devil's Den.\n"
            + "--1 items are in the room:\n"
            + "\tIndex:5. Name:Civil War Cannon. RoomIdx:15. Attack:3.\n"
            + "--3 adjacent rooms:\n"
            + "\tRoom index:3. Name:Skull Valley.\n"
            + "\tRoom index:14. Name:Satan's Kingdom.\n"
            + "\tRoom index:16. Name:West Kill.\n"
            + "Room index:16. Name:West Kill.\n"
            + "--1 items are in the room:\n"
            + "\tIndex:7. Name:Broom Stick. RoomIdx:16. Attack:2.\n"
            + "--2 adjacent rooms:\n"
            + "\tRoom index:3. Name:Skull Valley.\n"
            + "\tRoom index:15. Name:Devil's Den.\n"
            + "Room index:17. Name:Black Creek.\n"
            + "--3 adjacent rooms:\n"
            + "\tRoom index:3. Name:Skull Valley.\n"
            + "\tRoom index:9. Name:Black Island.\n"
            + "\tRoom index:19. Name:Vulture City.\n"
            + "Target player moves from 0-indexed room to 1-indexed room.\n"
            + "Round 1 finish.\n";
    assertEquals(expected, info);
    world.curHumanPlayerMove(15);
    world.curHumanPlayerDisplayNeighborRooms();
    info = world.getLastTurnMsg();
    expected = "The room current player is in:\n"
            + "Room index:15. Name:Devil's Den.\n"
            + "--1 items are in the room:\n"
            + "\tIndex:5. Name:Civil War Cannon. RoomIdx:15. Attack:3.\n"
            + "--1 players are in the room:\n"
            + "\taa\n"
            + "--3 adjacent rooms:\n"
            + "\tRoom index:3. Name:Skull Valley.\n"
            + "\tRoom index:14. Name:Satan's Kingdom.\n"
            + "\tRoom index:16. Name:West Kill.\n"
            + "Neighbor rooms:\n"
            + "Room index:3. Name:Skull Valley.\n"
            + "--5 adjacent rooms:\n"
            + "\tRoom index:9. Name:Black Island.\n"
            + "\tRoom index:14. Name:Satan's Kingdom.\n"
            + "\tRoom index:15. Name:Devil's Den.\n"
            + "\tRoom index:16. Name:West Kill.\n"
            + "\tRoom index:17. Name:Black Creek.\n"
            + "Room index:14. Name:Satan's Kingdom.\n"
            + "--4 adjacent rooms:\n"
            + "\tRoom index:2. Name:Kill Devil Hills.\n"
            + "\tRoom index:3. Name:Skull Valley.\n"
            + "\tRoom index:4. Name:Seven Devils.\n"
            + "\tRoom index:15. Name:Devil's Den.\n"
            + "Room index:16. Name:West Kill.\n"
            + "--1 items are in the room:\n"
            + "\tIndex:7. Name:Broom Stick. RoomIdx:16. Attack:2.\n"
            + "--2 adjacent rooms:\n"
            + "\tRoom index:3. Name:Skull Valley.\n"
            + "\tRoom index:15. Name:Devil's Den.\n"
            + "Target player moves from 2-indexed room to 3-indexed room.\n"
            + "Round 3 finish.\n";
    assertEquals(expected, info);
  }

  @Test
  public void testPlayerLookAroundTarget() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    List<String> names = new ArrayList<>();
    List<Integer> rooms = new ArrayList<>();
    List<Boolean> ais = new ArrayList<>();
    names.add("aa");
    rooms.add(0);
    ais.add(false);
    world.setPlayers(names, rooms, ais);
    world.setMaxTurn(30);
    world.setMaxItemCarried(3);
    world.curHumanPlayerDisplayNeighborRooms();
    String info = world.getLastTurnMsg();
    String expected = "The room current player is in:\n"
            + "Room index:0. Name:Tombstone.\n"
            + "--1 items are in the room:\n"
            + "\tIndex:4. Name:Revolver. RoomIdx:0. Attack:3.\n"
            + "--Target character is in the room:\n"
            + "\tName:Bravo Mabel. RoomIdx:0. Health:50.\n"
            + "--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--1 players are in the room:\n"
            + "\taa\n"
            + "--1 adjacent rooms:\n"
            + "\tRoom index:1. Name:Hell's Kitchen.\n"
            + "Neighbor rooms:\n"
            + "Room index:1. Name:Hell's Kitchen.\n"
            + "--1 items are in the room:\n"
            + "\tIndex:8. Name:Billiard Cue. RoomIdx:1. Attack:2.\n"
            + "--3 adjacent rooms:\n"
            + "\tRoom index:0. Name:Tombstone.\n"
            + "\tRoom index:2. Name:Kill Devil Hills.\n"
            + "\tRoom index:5. Name:Deadwood.\n"
            + "Target player moves from 0-indexed room to 1-indexed room.\n"
            + "Round 1 finish.\n";
    assertEquals(expected, info);
    world.curHumanPlayerDisplayNeighborRooms();
    info = world.getLastTurnMsg();
    expected = "The room current player is in:\n"
            + "Room index:0. Name:Tombstone.\n"
            + "--1 items are in the room:\n"
            + "\tIndex:4. Name:Revolver. RoomIdx:0. Attack:3.\n"
            + "--1 players are in the room:\n"
            + "\taa\n"
            + "--1 adjacent rooms:\n"
            + "\tRoom index:1. Name:Hell's Kitchen.\n"
            + "Neighbor rooms:\n"
            + "Room index:1. Name:Hell's Kitchen.\n"
            + "Target player moves from 1-indexed room to 2-indexed room.\n"
            + "Round 2 finish.\n";
    assertEquals(expected, info);
  }

  @Test
  public void testPlayerLookAroundPlayer() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    List<String> names = new ArrayList<>();
    List<Integer> rooms = new ArrayList<>();
    List<Boolean> ais = new ArrayList<>();
    names.add("aa");
    names.add("bb");
    rooms.add(0);
    rooms.add(0);
    ais.add(false);
    ais.add(false);
    world.setPlayers(names, rooms, ais);
    world.setMaxTurn(30);
    world.setMaxItemCarried(3);
    world.curHumanPlayerDisplayNeighborRooms();
    String info = world.getLastTurnMsg();
    String expected = "The room current player is in:\n"
            + "Room index:0. Name:Tombstone.\n"
            + "--1 items are in the room:\n"
            + "\tIndex:4. Name:Revolver. RoomIdx:0. Attack:3.\n"
            + "--Target character is in the room:\n"
            + "\tName:Bravo Mabel. RoomIdx:0. Health:50.\n"
            + "--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--2 players are in the room:\n"
            + "\taa\n"
            + "\tbb\n"
            + "--1 adjacent rooms:\n"
            + "\tRoom index:1. Name:Hell's Kitchen.\n"
            + "Neighbor rooms:\n"
            + "Room index:1. Name:Hell's Kitchen.\n"
            + "--1 items are in the room:\n"
            + "\tIndex:8. Name:Billiard Cue. RoomIdx:1. Attack:2.\n"
            + "--3 adjacent rooms:\n"
            + "\tRoom index:0. Name:Tombstone.\n"
            + "\tRoom index:2. Name:Kill Devil Hills.\n"
            + "\tRoom index:5. Name:Deadwood.\n"
            + "Target player moves from 0-indexed room to 1-indexed room.\n"
            + "Round 1 finish.\n";
    assertEquals(expected, info);
    world.curHumanPlayerMove(1);
    world.curHumanPlayerDisplayNeighborRooms();
    info = world.getLastTurnMsg();
    expected = "The room current player is in:\n"
            + "Room index:0. Name:Tombstone.\n"
            + "--1 items are in the room:\n"
            + "\tIndex:4. Name:Revolver. RoomIdx:0. Attack:3.\n"
            + "--1 players are in the room:\n"
            + "\taa\n"
            + "--1 adjacent rooms:\n"
            + "\tRoom index:1. Name:Hell's Kitchen.\n"
            + "Neighbor rooms:\n"
            + "Room index:1. Name:Hell's Kitchen.\n"
            + "--1 items are in the room:\n"
            + "\tIndex:8. Name:Billiard Cue. RoomIdx:1. Attack:2.\n"
            + "--1 players are in the room:\n"
            + "\tbb\n"
            + "--3 adjacent rooms:\n"
            + "\tRoom index:0. Name:Tombstone.\n"
            + "\tRoom index:2. Name:Kill Devil Hills.\n"
            + "\tRoom index:5. Name:Deadwood.\n"
            + "Target player moves from 2-indexed room to 3-indexed room.\n"
            + "Round 3 finish.\n";
    assertEquals(expected, info);
  }

  @Test
  public void testPlayerLookAroundPet() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    List<String> names = new ArrayList<>();
    List<Integer> rooms = new ArrayList<>();
    List<Boolean> ais = new ArrayList<>();
    names.add("aa");
    names.add("bb");
    rooms.add(0);
    rooms.add(0);
    ais.add(false);
    ais.add(false);
    world.setPlayers(names, rooms, ais);
    world.setMaxTurn(30);
    world.setMaxItemCarried(3);
    world.curHumanPlayerDisplayNeighborRooms();
    String info = world.getLastTurnMsg();
    String expected = "The room current player is in:\n"
            + "Room index:0. Name:Tombstone.\n"
            + "--1 items are in the room:\n"
            + "\tIndex:4. Name:Revolver. RoomIdx:0. Attack:3.\n"
            + "--Target character is in the room:\n"
            + "\tName:Bravo Mabel. RoomIdx:0. Health:50.\n"
            + "--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--2 players are in the room:\n"
            + "\taa\n"
            + "\tbb\n"
            + "--1 adjacent rooms:\n"
            + "\tRoom index:1. Name:Hell's Kitchen.\n"
            + "Neighbor rooms:\n"
            + "Room index:1. Name:Hell's Kitchen.\n"
            + "--1 items are in the room:\n"
            + "\tIndex:8. Name:Billiard Cue. RoomIdx:1. Attack:2.\n"
            + "--3 adjacent rooms:\n"
            + "\tRoom index:0. Name:Tombstone.\n"
            + "\tRoom index:2. Name:Kill Devil Hills.\n"
            + "\tRoom index:5. Name:Deadwood.\n"
            + "Target player moves from 0-indexed room to 1-indexed room.\n"
            + "Round 1 finish.\n";
    assertEquals(expected, info);
    world.curHumanPlayerDisplayNeighborRooms();
    info = world.getLastTurnMsg();
    expected = "The room current player is in:\n"
            + "Room index:0. Name:Tombstone.\n"
            + "--1 items are in the room:\n"
            + "\tIndex:4. Name:Revolver. RoomIdx:0. Attack:3.\n"
            + "--2 players are in the room:\n"
            + "\taa\n"
            + "\tbb\n"
            + "--1 adjacent rooms:\n"
            + "\tRoom index:1. Name:Hell's Kitchen.\n"
            + "Neighbor rooms:\n"
            + "Room index:1. Name:Hell's Kitchen.\n"
            + "Target player moves from 1-indexed room to 2-indexed room.\n"
            + "Round 2 finish.\n";
    assertEquals(expected, info);
    world.curHumanPlayerMove(1);
    world.curHumanPlayerDisplayNeighborRooms();
    info = world.getLastTurnMsg();
    expected = "The room current player is in:\n"
            + "Room index:0. Name:Tombstone.\n"
            + "--1 items are in the room:\n"
            + "\tIndex:4. Name:Revolver. RoomIdx:0. Attack:3.\n"
            + "--1 players are in the room:\n"
            + "\tbb\n"
            + "--1 adjacent rooms:\n"
            + "\tRoom index:1. Name:Hell's Kitchen.\n"
            + "Neighbor rooms:\n"
            + "Room index:1. Name:Hell's Kitchen.\n"
            + "--1 items are in the room:\n"
            + "\tIndex:8. Name:Billiard Cue. RoomIdx:1. Attack:2.\n"
            + "--1 players are in the room:\n"
            + "\taa\n"
            + "--3 adjacent rooms:\n"
            + "\tRoom index:0. Name:Tombstone.\n"
            + "\tRoom index:2. Name:Kill Devil Hills.\n"
            + "\tRoom index:5. Name:Deadwood.\n"
            + "Target player moves from 3-indexed room to 4-indexed room.\n"
            + "Round 4 finish.\n";
    assertEquals(expected, info);
    world.curHumanPlayerMovePet(0);
    world.curHumanPlayerDisplayNeighborRooms();
    info = world.getLastTurnMsg();
    expected = "The room current player is in:\n"
            + "Room index:0. Name:Tombstone.\n"
            + "--1 items are in the room:\n"
            + "\tIndex:4. Name:Revolver. RoomIdx:0. Attack:3.\n"
            + "--1 players are in the room:\n"
            + "\tbb\n"
            + "--1 adjacent rooms:\n"
            + "\tRoom index:1. Name:Hell's Kitchen.\n"
            + "Neighbor rooms:\n"
            + "Room index:1. Name:Hell's Kitchen.\n"
            + "Target player moves from 5-indexed room to 6-indexed room.\n"
            + "Round 6 finish.\n";
    assertEquals(expected, info);
  }

  @Test
  public void testAttack() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    List<String> names = new ArrayList<>();
    List<Integer> rooms = new ArrayList<>();
    List<Boolean> ais = new ArrayList<>();
    names.add("aa");
    names.add("bb");
    rooms.add(0);
    rooms.add(4);
    ais.add(false);
    ais.add(false);
    world.setPlayers(names, rooms, ais);
    world.setMaxTurn(30);
    world.setMaxItemCarried(3);
    world.curHumanPlayerAttackTargetCharacter(-1);
    String info = world.printRoomInfo(1);
    String expected = "--1 items are in the room:\n"
            + "\tIndex:8. Name:Billiard Cue. RoomIdx:1. Attack:2.\n"
            + "--Target character is in the room:\n"
            + "\tName:Bravo Mabel. RoomIdx:1. Health:49.\n"
            + "--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--3 adjacent rooms:\n"
            + "\tRoom index:0. Name:Tombstone.\n"
            + "\tRoom index:2. Name:Kill Devil Hills.\n"
            + "\tRoom index:5. Name:Deadwood.\n";
    assertEquals(expected, info);
    world.curHumanPlayerPickupItem(1);
    world.curHumanPlayerDisplayNeighborRooms();
    world.curHumanPlayerAttackTargetCharacter(1);
    info = world.printRoomInfo(4);
    expected = "--Target character is in the room:\n"
            + "\tName:Bravo Mabel. RoomIdx:4. Health:47.\n"
            + "--1 players are in the room:\n"
            + "\tbb\n"
            + "--4 adjacent rooms:\n"
            + "\tRoom index:2. Name:Kill Devil Hills.\n"
            + "\tRoom index:5. Name:Deadwood.\n"
            + "\tRoom index:8. Name:Devil's Lake.\n"
            + "\tRoom index:14. Name:Satan's Kingdom.\n";
    assertEquals(expected, info);
  }

  @Test
  public void testPokeEyesSeenByNeighbor() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    List<String> names = new ArrayList<>();
    List<Integer> rooms = new ArrayList<>();
    List<Boolean> ais = new ArrayList<>();
    names.add("aa");
    names.add("bb");
    names.add("cc");
    rooms.add(0);
    rooms.add(1);
    rooms.add(2);
    ais.add(false);
    ais.add(false);
    ais.add(false);
    world.setPlayers(names, rooms, ais);
    world.setMaxTurn(30);
    world.setMaxItemCarried(3);
    world.curHumanPlayerAttackTargetCharacter(-1);
    String info = world.getLastTurnMsg();
    String expected = "Player aa pokes Bravo Mabel's eyes successfully.\n"
            + "Target character's health, reduced by 1 points, now is 49.\n"
            + "Target player moves from 0-indexed room to 1-indexed room.\n"
            + "Round 1 finish.\n";
    assertEquals(expected, info);
    world.curHumanPlayerMovePet(0);
    world.curHumanPlayerAttackTargetCharacter(-1);
    info = world.getLastTurnMsg();
    expected = "Player cc's attack attempt failed for being seen by others.\n"
            + "Target player moves from 2-indexed room to 3-indexed room.\n"
            + "Round 3 finish.\n";
    assertEquals(expected, info);
  }

  @Test
  public void testAttackWithItemSeenByNeighbor() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    List<String> names = new ArrayList<>();
    List<Integer> rooms = new ArrayList<>();
    List<Boolean> ais = new ArrayList<>();
    names.add("aa");
    names.add("bb");
    names.add("cc");
    rooms.add(6);
    rooms.add(4);
    rooms.add(5);
    ais.add(false);
    ais.add(false);
    ais.add(false);
    world.setPlayers(names, rooms, ais);
    world.setMaxTurn(30);
    world.setMaxItemCarried(3);
    world.curHumanPlayerPickupItem(10);
    world.curHumanPlayerPickupItem(1);
    world.curHumanPlayerDisplayNeighborRooms();
    world.curHumanPlayerMovePet(8);
    world.curHumanPlayerAttackTargetCharacter(1);
    String info = world.getLastTurnMsg();
    String expected = "Player bb attempts attack with item Letter Opener successfully.\n"
            + "Target character's health, reduced by 2 points, now is 48.\n"
            + "Item Letter Opener is removed from the game.\n"
            + "Target player moves from 4-indexed room to 5-indexed room.\n"
            + "Round 5 finish.\n";
    assertEquals(expected, info);
    world.curHumanPlayerDisplayNeighborRooms();
    world.curHumanPlayerAttackTargetCharacter(10);
    info = world.getLastTurnMsg();
    expected = "Player aa's attack attempt failed for being seen by others.\n"
            + "Item Trowel is removed from the game.\n"
            + "Target player moves from 6-indexed room to 7-indexed room.\n"
            + "Round 7 finish.\n";
    assertEquals(expected, info);
  }

  @Test
  public void testPokeEyesSeenByPlayerInSameRoom() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath2);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    List<String> names = new ArrayList<>();
    List<Integer> rooms = new ArrayList<>();
    List<Boolean> ais = new ArrayList<>();
    names.add("aa");
    names.add("bb");
    rooms.add(0);
    rooms.add(0);
    ais.add(false);
    ais.add(false);
    world.setPlayers(names, rooms, ais);
    world.setMaxTurn(30);
    world.setMaxItemCarried(3);
    world.curHumanPlayerAttackTargetCharacter(-1);
    String info = world.getLastTurnMsg();
    String expected = "Player aa's attack attempt failed for being seen by others.\n"
            + "Target player moves from 0-indexed room to 1-indexed room.\n"
            + "Round 1 finish.\n";
    assertEquals(expected, info);
    world.curHumanPlayerMove(4);
    world.curHumanPlayerDisplayNeighborRooms();
    world.curHumanPlayerMove(0);
    world.curHumanPlayerMovePet(5);
    world.curHumanPlayerAttackTargetCharacter(-1);
    info = world.getLastTurnMsg();
    expected = "Player bb's attack attempt failed for being seen by others.\n"
            + "Target player moves from 5-indexed room to 0-indexed room.\n"
            + "Round 6 finish.\n";
    assertEquals(expected, info);
    world.curHumanPlayerAttackTargetCharacter(-1);
    info = world.getLastTurnMsg();
    expected = "Player aa's attack attempt failed for being seen by others.\n"
            + "Target player moves from 0-indexed room to 1-indexed room.\n"
            + "Round 7 finish.\n";
    assertEquals(expected, info);
  }

  @Test
  public void testAttackWithItemSeenByPlayerInSameRoom() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath2);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    List<String> names = new ArrayList<>();
    List<Integer> rooms = new ArrayList<>();
    List<Boolean> ais = new ArrayList<>();
    names.add("aa");
    names.add("bb");
    rooms.add(2);
    rooms.add(2);
    ais.add(false);
    ais.add(false);
    world.setPlayers(names, rooms, ais);
    world.setMaxTurn(30);
    world.setMaxItemCarried(3);
    world.curHumanPlayerPickupItem(4);
    world.curHumanPlayerDisplayNeighborRooms();
    world.curHumanPlayerAttackTargetCharacter(4);
    String info = world.getLastTurnMsg();
    String expected = "Player aa's attack attempt failed for being seen by others.\n"
            + "Item Revolver is removed from the game.\n"
            + "Target player moves from 2-indexed room to 3-indexed room.\n"
            + "Round 3 finish.\n";
    assertEquals(expected, info);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAttackWithInvalidItem() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    List<String> names = new ArrayList<>();
    List<Integer> rooms = new ArrayList<>();
    List<Boolean> ais = new ArrayList<>();
    names.add("aa");
    names.add("bb");
    names.add("cc");
    rooms.add(6);
    rooms.add(4);
    rooms.add(5);
    ais.add(false);
    ais.add(false);
    ais.add(false);
    world.setPlayers(names, rooms, ais);
    world.setMaxTurn(30);
    world.setMaxItemCarried(3);
    world.curHumanPlayerAttackTargetCharacter(10);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAttackWithRemovedItem() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath2);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    List<String> names = new ArrayList<>();
    List<Integer> rooms = new ArrayList<>();
    List<Boolean> ais = new ArrayList<>();
    names.add("aa");
    rooms.add(0);
    ais.add(false);
    world.setPlayers(names, rooms, ais);
    world.setMaxTurn(30);
    world.setMaxItemCarried(3);
    world.curHumanPlayerPickupItem(0);
    world.curHumanPlayerMove(4);
    world.curHumanPlayerDisplayNeighborRooms();
    world.curHumanPlayerDisplayNeighborRooms();
    world.curHumanPlayerAttackTargetCharacter(0);
    world.curHumanPlayerMove(0);
    world.curHumanPlayerAttackTargetCharacter(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testAttackNotInSameRoom() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath2);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    List<String> names = new ArrayList<>();
    List<Integer> rooms = new ArrayList<>();
    List<Boolean> ais = new ArrayList<>();
    names.add("aa");
    rooms.add(2);
    ais.add(false);
    world.setPlayers(names, rooms, ais);
    world.setMaxTurn(30);
    world.setMaxItemCarried(3);
    world.curHumanPlayerAttackTargetCharacter(-1);
  }

  @Test
  public void testPlayerInfo() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    List<String> names = new ArrayList<>();
    List<Integer> rooms = new ArrayList<>();
    List<Boolean> ais = new ArrayList<>();
    names.add("aa");
    rooms.add(0);
    ais.add(false);
    world.setPlayers(names, rooms, ais);
    world.setMaxTurn(30);
    world.setMaxItemCarried(3);
    world.curHumanPlayerDisplayNeighborRooms();
    String info = world.printPlayerInfo(0);
    String expected = "Player name:aa.\n"
            + "Room index:0.\n"
            + "Type: HUMAN\n"
            + "No item carried.\n";
    assertEquals(expected, info);
    world.curHumanPlayerPickupItem(4);
    info = world.printPlayerInfo(0);
    expected = "Player name:aa.\n"
            + "Room index:0.\n"
            + "Type: HUMAN\n"
            + "Carried 1 item(s):\n"
            + "Index:4. Name:Revolver. RoomIdx:0. Attack:3.\n";
    assertEquals(expected, info);
  }

  @Test
  public void testTakeTurns() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    List<String> names = new ArrayList<>();
    List<Integer> rooms = new ArrayList<>();
    List<Boolean> ais = new ArrayList<>();
    names.add("aa");
    names.add("bb");
    names.add("cc");
    rooms.add(0);
    rooms.add(0);
    rooms.add(0);
    ais.add(false);
    ais.add(false);
    ais.add(false);
    world.setPlayers(names, rooms, ais);
    world.setMaxTurn(30);
    world.setMaxItemCarried(3);
    assertEquals(0, world.getCurPlayerIdx());
    assertEquals("aa", world.getCurPlayerName());
    world.curHumanPlayerDisplayNeighborRooms();
    assertEquals(1, world.getCurPlayerIdx());
    assertEquals("bb", world.getCurPlayerName());
    world.curHumanPlayerDisplayNeighborRooms();
    assertEquals(2, world.getCurPlayerIdx());
    assertEquals("cc", world.getCurPlayerName());
    world.curHumanPlayerDisplayNeighborRooms();
    assertEquals(0, world.getCurPlayerIdx());
    assertEquals("aa", world.getCurPlayerName());
    world.curHumanPlayerDisplayNeighborRooms();
  }

  @Test
  public void testGameEndsReachMaxTurn() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    List<String> names = new ArrayList<>();
    List<Integer> rooms = new ArrayList<>();
    List<Boolean> ais = new ArrayList<>();
    names.add("aa");
    names.add("bb");
    names.add("cc");
    rooms.add(0);
    rooms.add(0);
    rooms.add(0);
    ais.add(false);
    ais.add(false);
    ais.add(false);
    world.setPlayers(names, rooms, ais);
    world.setMaxTurn(2);
    world.setMaxItemCarried(3);
    assertEquals(false, world.isGameOver());
    assertEquals(0, world.getCurPlayerIdx());
    assertEquals("aa", world.getCurPlayerName());
    world.curHumanPlayerDisplayNeighborRooms();
    assertEquals(false, world.isGameOver());
    assertEquals(1, world.getCurPlayerIdx());
    assertEquals("bb", world.getCurPlayerName());
    assertEquals(true, world.isGameOver());
  }


  @Test
  public void testGameEndsTargetKilledByHuman() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath2);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    world.setMaxTurn(30);
    world.setMaxItemCarried(3);
    List<Player> players = new ArrayList<>();
    players.add(new PlayerHumanImpl("aa", 0));
    world.setPlayers(players);
    world.curHumanPlayerAttackTargetCharacter(-1);
    world.curHumanPlayerPickupItem(0);
    world.curHumanPlayerMove(1);
    world.curHumanPlayerPickupItem(2);
    world.curHumanPlayerPickupItem(3);
    world.curHumanPlayerMove(0);
    world.curHumanPlayerAttackTargetCharacter(2);
    world.curHumanPlayerMove(4);
    world.curHumanPlayerPickupItem(7);
    world.curHumanPlayerAttackTargetCharacter(7);
    String info = world.getLastTurnMsg();
    String expected = "Player aa attempts attack with item Broom Stick successfully.\n"
            + "Target character's health, reduced by 4 points, now is 0.\n"
            + "Item Broom Stick is removed from the game.\n"
            + "Round 10 finish.\n";
    assertEquals(expected, info);
    info = world.getGameResultMsg();
    expected = "Target character is killed! Winner is aa.\n";
    assertEquals(expected, info);
  }

  @Test
  public void testGameEndsTargetKilledByAi() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath2);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    world.setMaxTurn(30);
    world.setMaxItemCarried(3);
    List<Player> players = new ArrayList<>();
    players.add(new PlayerHumanImpl("aa", 0));
    players.add(new PlayerAiImpl("bb", 1,
            new AiActionControllerImpl(
                    "PICKUP_ITEM", "1",
                    "PICKUP_ITEM", "2",
                    "MOVE", "3",
                    "PICKUP_ITEM", "6",
                    "LOOK_AROUND")));
    world.setPlayers(players);

    world.curHumanPlayerAttackTargetCharacter(-1);
    AiActionParam ap = world.aiPlayerDoAction();
    assertEquals(ap.getActionType(), PlayerActionType.ATTACK);
    assertEquals(ap.getActionParameter(), -1);

    world.curHumanPlayerMove(4);
    world.aiPlayerDoAction();
    world.curHumanPlayerAttackTargetCharacter(-1);
    world.aiPlayerDoAction();

    world.curHumanPlayerPickupItem(7);
    ap = world.aiPlayerDoAction();
    assertEquals(ap.getActionType(), PlayerActionType.ATTACK);
    assertEquals(ap.getActionParameter(), 2);
    world.curHumanPlayerMove(0);
    world.aiPlayerDoAction();
    world.curHumanPlayerPickupItem(0);
    world.aiPlayerDoAction();
    world.curHumanPlayerDisplayNeighborRooms();
    world.aiPlayerDoAction();
    world.curHumanPlayerDisplayNeighborRooms();
    world.aiPlayerDoAction();
    String info = world.getLastTurnMsg();
    String expected = "Player bb attempts attack with item Chain Saw successfully.\n"
            + "Target character's health, reduced by 4 points, now is 0.\n"
            + "Item Chain Saw is removed from the game.\n"
            + "Round 16 finish.\n";
    assertEquals(expected, info);
    info = world.getGameResultMsg();
    expected = "Target character is killed! Winner is bb.\n";
    assertEquals(expected, info);
  }

  @Test
  public void testTargetCharacterMoveToZeroAfterReachMaxIndex() {
    StringReader sr = new StringReader(
            "20 28 Gravity Falls\n"
                    + "50 Bravo Mabel\n"
                    + "Fortune the Cat\n"
                    + "2\n"
                    + " 0  0  3  3 Tombstone\n"
                    + " 0  4  5  7 Hell's Kitchen\n"
                    + "2\n"
                    + "0 3 Crepe Pan\n"
                    + "1 2 Letter Opener\n");
    WorldModel world = new WorldModelImpl(sr);
    List<String> names = new ArrayList<>();
    List<Integer> rooms = new ArrayList<>();
    List<Boolean> ais = new ArrayList<>();
    names.add("aa");
    rooms.add(0);
    ais.add(false);
    world.setPlayers(names, rooms, ais);
    world.setMaxTurn(30);
    world.setMaxItemCarried(2);
    assertEquals(0, world.getTargetCharacterRoomIdx());
    world.curHumanPlayerMove(1);
    assertEquals(1, world.getTargetCharacterRoomIdx());
    world.curHumanPlayerDisplayNeighborRooms();
    assertEquals(0, world.getTargetCharacterRoomIdx());
  }

  @Test
  public void outputMapImage() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    world.outputMapImage("testOutputMap.png", 30);
  }

  @Test(expected = IllegalArgumentException.class)
  public void outputMapImageScaleZero() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    world.outputMapImage("testOutputMap.png", 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void outputMapImageScaleNegative() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    world.outputMapImage("testOutputMap.png", -6);
  }

  @Test(expected = IllegalArgumentException.class)
  public void outputMapImageScaleOdd() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    world.outputMapImage("testOutputMap.png", 83);
  }


  //milestone 3
  @Test
  public void petMovesInDfs() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    List<String> names = new ArrayList<>();
    List<Integer> rooms = new ArrayList<>();
    List<Boolean> ais = new ArrayList<>();
    names.add(firstPlayerName);
    names.add(secondPlayerName);
    rooms.add(firstPlayerRoomIdx);
    rooms.add(secondPlayerRoomIdx);
    ais.add(false);
    ais.add(false);
    world.setPlayers(names, rooms, ais);
    world.setMaxTurn(30);
    world.setMaxItemCarried(2);

    StringBuilder sbComp = new StringBuilder();
    sbComp.append("--1 items are in the room:\n"
            + "\tIndex:4. Name:Revolver. RoomIdx:0. Attack:3.\n"
            + "--Target character is in the room:\n"
            + "\tName:Bravo Mabel. RoomIdx:0. Health:50.\n"
            + "--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--1 adjacent rooms:\n"
            + "\tRoom index:1. Name:Hell's Kitchen.\n");
    assertEquals(sbComp.toString(), world.printRoomInfo(0));
    world.curHumanPlayerMove(0);

    sbComp.delete(0, sbComp.length());
    sbComp.append("--1 items are in the room:\n"
            + "\tIndex:8. Name:Billiard Cue. RoomIdx:1. Attack:2.\n"
            + "--Target character is in the room:\n"
            + "\tName:Bravo Mabel. RoomIdx:1. Health:50.\n"
            + "--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--3 adjacent rooms:\n"
            + "\tRoom index:0. Name:Tombstone.\n"
            + "\tRoom index:2. Name:Kill Devil Hills.\n"
            + "\tRoom index:5. Name:Deadwood.\n");
    assertEquals(sbComp.toString(), world.printRoomInfo(1));
    world.curHumanPlayerMove(4);

    sbComp.delete(0, sbComp.length());
    sbComp.append("--2 items are in the room:\n"
            + "\tIndex:6. Name:Chain Saw. RoomIdx:2. Attack:4.\n"
            + "\tIndex:11. Name:Big Red Hammer. RoomIdx:2. Attack:4.\n"
            + "--Target character is in the room:\n"
            + "\tName:Bravo Mabel. RoomIdx:2. Health:50.\n"
            + "--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--4 adjacent rooms:\n"
            + "\tRoom index:1. Name:Hell's Kitchen.\n"
            + "\tRoom index:4. Name:Seven Devils.\n"
            + "\tRoom index:5. Name:Deadwood.\n"
            + "\tRoom index:14. Name:Satan's Kingdom.\n");
    assertEquals(sbComp.toString(), world.printRoomInfo(2));
    world.curHumanPlayerMove(1);

    sbComp.delete(0, sbComp.length());
    sbComp.append("--2 items are in the room:\n"
            + "\tIndex:6. Name:Chain Saw. RoomIdx:2. Attack:4.\n"
            + "\tIndex:11. Name:Big Red Hammer. RoomIdx:2. Attack:4.\n"
            + "--4 adjacent rooms:\n"
            + "\tRoom index:1. Name:Hell's Kitchen.\n"
            + "\tRoom index:4. Name:Seven Devils.\n"
            + "\tRoom index:5. Name:Deadwood.\n"
            + "\tRoom index:14. Name:Satan's Kingdom.\n");
    assertEquals(sbComp.toString(), world.printRoomInfo(2));
    sbComp.delete(0, sbComp.length());
    sbComp.append("--1 items are in the room:\n"
            + "\tIndex:1. Name:Letter Opener. RoomIdx:4. Attack:2.\n"
            + "--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--1 players are in the room:\n"
            + "\tWil\n"
            + "--4 adjacent rooms:\n"
            + "\tRoom index:2. Name:Kill Devil Hills.\n"
            + "\tRoom index:5. Name:Deadwood.\n"
            + "\tRoom index:8. Name:Devil's Lake.\n"
            + "\tRoom index:14. Name:Satan's Kingdom.\n");
    assertEquals(sbComp.toString(), world.printRoomInfo(4));
    world.curHumanPlayerMove(14);

    sbComp.delete(0, sbComp.length());
    sbComp.append("--2 items are in the room:\n"
            + "\tIndex:0. Name:Crepe Pan. RoomIdx:8. Attack:3.\n"
            + "\tIndex:3. Name:Sharp Knife. RoomIdx:8. Attack:3.\n"
            + "--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--4 adjacent rooms:\n"
            + "\tRoom index:4. Name:Seven Devils.\n"
            + "\tRoom index:7. Name:Devil's Thumb.\n"
            + "\tRoom index:10. Name:Sleepy Hollow.\n"
            + "\tRoom index:11. Name:Scarville.\n");
    assertEquals(sbComp.toString(), world.printRoomInfo(8));
    world.curHumanPlayerMove(2);

    sbComp.delete(0, sbComp.length());
    sbComp.append("--1 items are in the room:\n"
            + "\tIndex:19. Name:Loud Noise. RoomIdx:7. Attack:2.\n"
            + "--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--4 adjacent rooms:\n"
            + "\tRoom index:5. Name:Deadwood.\n"
            + "\tRoom index:6. Name:Slaughter Beach.\n"
            + "\tRoom index:8. Name:Devil's Lake.\n"
            + "\tRoom index:10. Name:Sleepy Hollow.\n");
    assertEquals(sbComp.toString(), world.printRoomInfo(7));
    world.curHumanPlayerMove(3);

    sbComp.delete(0, sbComp.length());
    sbComp.append("--2 items are in the room:\n"
            + "\tIndex:10. Name:Trowel. RoomIdx:6. Attack:2.\n"
            + "\tIndex:12. Name:Pinking Shears. RoomIdx:6. Attack:2.\n"
            + "--Target character is in the room:\n"
            + "\tName:Bravo Mabel. RoomIdx:6. Health:50.\n"
            + "--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--3 adjacent rooms:\n"
            + "\tRoom index:5. Name:Deadwood.\n"
            + "\tRoom index:7. Name:Devil's Thumb.\n"
            + "\tRoom index:10. Name:Sleepy Hollow.\n");
    assertEquals(sbComp.toString(), world.printRoomInfo(6));
    world.curHumanPlayerMove(4);

    sbComp.delete(0, sbComp.length());
    sbComp.append("--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--4 adjacent rooms:\n"
            + "\tRoom index:6. Name:Slaughter Beach.\n"
            + "\tRoom index:7. Name:Devil's Thumb.\n"
            + "\tRoom index:8. Name:Devil's Lake.\n"
            + "\tRoom index:12. Name:Casper.\n");
    assertEquals(sbComp.toString(), world.printRoomInfo(10));
    world.curHumanPlayerMove(9);

    sbComp.delete(0, sbComp.length());
    sbComp.append("--1 items are in the room:\n"
            + "\tIndex:2. Name:Shoe Horn. RoomIdx:12. Attack:2.\n"
            + "--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--2 adjacent rooms:\n"
            + "\tRoom index:10. Name:Sleepy Hollow.\n"
            + "\tRoom index:13. Name:Cape Fear.\n");
    assertEquals(sbComp.toString(), world.printRoomInfo(12));
    world.curHumanPlayerMove(8);

    sbComp.delete(0, sbComp.length());
    sbComp.append("--1 items are in the room:\n"
            + "\tIndex:14. Name:Bad Cream. RoomIdx:13. Attack:2.\n"
            + "--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--3 adjacent rooms:\n"
            + "\tRoom index:11. Name:Scarville.\n"
            + "\tRoom index:12. Name:Casper.\n"
            + "\tRoom index:18. Name:Bat Cave.\n");
    assertEquals(sbComp.toString(), world.printRoomInfo(13));
    world.curHumanPlayerMove(17);

    sbComp.delete(0, sbComp.length());
    sbComp.append("--2 items are in the room:\n"
            + "\tIndex:13. Name:Duck Decoy. RoomIdx:18. Attack:3.\n"
            + "\tIndex:15. Name:Monkey Hand. RoomIdx:18. Attack:2.\n"
            + "--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--2 adjacent rooms:\n"
            + "\tRoom index:11. Name:Scarville.\n"
            + "\tRoom index:13. Name:Cape Fear.\n");
    assertEquals(sbComp.toString(), world.printRoomInfo(18));
    world.curHumanPlayerMove(11);

    sbComp.delete(0, sbComp.length());
    sbComp.append("--1 items are in the room:\n"
            + "\tIndex:16. Name:Tight Hat. RoomIdx:11. Attack:2.\n"
            + "--Target character is in the room:\n"
            + "\tName:Bravo Mabel. RoomIdx:11. Health:50.\n"
            + "--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--1 players are in the room:\n"
            + "\tPhi\n"
            + "--5 adjacent rooms:\n"
            + "\tRoom index:8. Name:Devil's Lake.\n"
            + "\tRoom index:9. Name:Black Island.\n"
            + "\tRoom index:13. Name:Cape Fear.\n"
            + "\tRoom index:18. Name:Bat Cave.\n"
            + "\tRoom index:19. Name:Vulture City.\n");
    assertEquals(sbComp.toString(), world.printRoomInfo(11));
    world.curHumanPlayerMove(19);

    sbComp.delete(0, sbComp.length());
    sbComp.append("--1 items are in the room:\n"
            + "\tIndex:18. Name:Silken Cord. RoomIdx:9. Attack:3.\n"
            + "--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--3 adjacent rooms:\n"
            + "\tRoom index:3. Name:Skull Valley.\n"
            + "\tRoom index:11. Name:Scarville.\n"
            + "\tRoom index:17. Name:Black Creek.\n");
    assertEquals(sbComp.toString(), world.printRoomInfo(9));
    world.curHumanPlayerMove(18);

    sbComp.delete(0, sbComp.length());
    sbComp.append("--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--5 adjacent rooms:\n"
            + "\tRoom index:9. Name:Black Island.\n"
            + "\tRoom index:14. Name:Satan's Kingdom.\n"
            + "\tRoom index:15. Name:Devil's Den.\n"
            + "\tRoom index:16. Name:West Kill.\n"
            + "\tRoom index:17. Name:Black Creek.\n");
    assertEquals(sbComp.toString(), world.printRoomInfo(3));
    world.curHumanPlayerMove(11);

    sbComp.delete(0, sbComp.length());
    sbComp.append("--1 items are in the room:\n"
            + "\tIndex:5. Name:Civil War Cannon. RoomIdx:15. Attack:3.\n"
            + "--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--3 adjacent rooms:\n"
            + "\tRoom index:3. Name:Skull Valley.\n"
            + "\tRoom index:14. Name:Satan's Kingdom.\n"
            + "\tRoom index:16. Name:West Kill.\n");
    assertEquals(sbComp.toString(), world.printRoomInfo(15));
    world.curHumanPlayerMove(13);

    sbComp.delete(0, sbComp.length());
    sbComp.append("--1 items are in the room:\n"
            + "\tIndex:7. Name:Broom Stick. RoomIdx:16. Attack:2.\n"
            + "--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--2 adjacent rooms:\n"
            + "\tRoom index:3. Name:Skull Valley.\n"
            + "\tRoom index:15. Name:Devil's Den.\n");
    assertEquals(sbComp.toString(), world.printRoomInfo(16));
    world.curHumanPlayerMove(8);

    sbComp.delete(0, sbComp.length());
    sbComp.append("--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--3 adjacent rooms:\n"
            + "\tRoom index:3. Name:Skull Valley.\n"
            + "\tRoom index:9. Name:Black Island.\n"
            + "\tRoom index:19. Name:Vulture City.\n");
    assertEquals(sbComp.toString(), world.printRoomInfo(17));
    world.curHumanPlayerMove(12);

    sbComp.delete(0, sbComp.length());
    sbComp.append("--2 items are in the room:\n"
            + "\tIndex:17. Name:Piece of Rope. RoomIdx:19. Attack:2.\n"
            + "\tIndex:9. Name:Rat Poison. RoomIdx:19. Attack:2.\n"
            + "--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--2 adjacent rooms:\n"
            + "\tRoom index:11. Name:Scarville.\n"
            + "\tRoom index:17. Name:Black Creek.\n");
    assertEquals(sbComp.toString(), world.printRoomInfo(19));
    world.curHumanPlayerMove(4);

    sbComp.delete(0, sbComp.length());
    sbComp.append("--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--4 adjacent rooms:\n"
            + "\tRoom index:2. Name:Kill Devil Hills.\n"
            + "\tRoom index:3. Name:Skull Valley.\n"
            + "\tRoom index:4. Name:Seven Devils.\n"
            + "\tRoom index:15. Name:Devil's Den.\n");
    assertEquals(sbComp.toString(), world.printRoomInfo(14));
    world.curHumanPlayerMove(13);

    sbComp.delete(0, sbComp.length());
    sbComp.append("--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--5 adjacent rooms:\n"
            + "\tRoom index:1. Name:Hell's Kitchen.\n"
            + "\tRoom index:2. Name:Kill Devil Hills.\n"
            + "\tRoom index:4. Name:Seven Devils.\n"
            + "\tRoom index:6. Name:Slaughter Beach.\n"
            + "\tRoom index:7. Name:Devil's Thumb.\n");
    assertEquals(sbComp.toString(), world.printRoomInfo(5));
    world.curHumanPlayerMove(8);

    sbComp.delete(0, sbComp.length());
    sbComp.append("--1 items are in the room:\n"
            + "\tIndex:8. Name:Billiard Cue. RoomIdx:1. Attack:2.\n"
            + "--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--3 adjacent rooms:\n"
            + "\tRoom index:0. Name:Tombstone.\n"
            + "\tRoom index:2. Name:Kill Devil Hills.\n"
            + "\tRoom index:5. Name:Deadwood.\n");
    assertEquals(sbComp.toString(), world.printRoomInfo(1));
    world.curHumanPlayerMove(12);

    sbComp.delete(0, sbComp.length());
    sbComp.append("--1 items are in the room:\n"
            + "\tIndex:4. Name:Revolver. RoomIdx:0. Attack:3.\n"
            + "--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--1 adjacent rooms:\n"
            + "\tRoom index:1. Name:Hell's Kitchen.\n");
    assertEquals(sbComp.toString(), world.printRoomInfo(0));
    world.curHumanPlayerMove(4);

    sbComp.delete(0, sbComp.length());
    sbComp.append("--2 items are in the room:\n"
            + "\tIndex:6. Name:Chain Saw. RoomIdx:2. Attack:4.\n"
            + "\tIndex:11. Name:Big Red Hammer. RoomIdx:2. Attack:4.\n"
            + "--Target character is in the room:\n"
            + "\tName:Bravo Mabel. RoomIdx:2. Health:50.\n"
            + "--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--4 adjacent rooms:\n"
            + "\tRoom index:1. Name:Hell's Kitchen.\n"
            + "\tRoom index:4. Name:Seven Devils.\n"
            + "\tRoom index:5. Name:Deadwood.\n"
            + "\tRoom index:14. Name:Satan's Kingdom.\n");
    assertEquals(sbComp.toString(), world.printRoomInfo(2));
    world.curHumanPlayerMove(13);

    sbComp.delete(0, sbComp.length());
    sbComp.append("--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--4 adjacent rooms:\n"
            + "\tRoom index:2. Name:Kill Devil Hills.\n"
            + "\tRoom index:3. Name:Skull Valley.\n"
            + "\tRoom index:4. Name:Seven Devils.\n"
            + "\tRoom index:15. Name:Devil's Den.\n");
    assertEquals(sbComp.toString(), world.printRoomInfo(14));
    world.curHumanPlayerMove(8);

    sbComp.delete(0, sbComp.length());
    sbComp.append("--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--5 adjacent rooms:\n"
            + "\tRoom index:9. Name:Black Island.\n"
            + "\tRoom index:14. Name:Satan's Kingdom.\n"
            + "\tRoom index:15. Name:Devil's Den.\n"
            + "\tRoom index:16. Name:West Kill.\n"
            + "\tRoom index:17. Name:Black Creek.\n");
    assertEquals(sbComp.toString(), world.printRoomInfo(3));
    world.curHumanPlayerMove(12);

    sbComp.delete(0, sbComp.length());
    sbComp.append("--1 items are in the room:\n"
            + "\tIndex:18. Name:Silken Cord. RoomIdx:9. Attack:3.\n"
            + "--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--3 adjacent rooms:\n"
            + "\tRoom index:3. Name:Skull Valley.\n"
            + "\tRoom index:11. Name:Scarville.\n"
            + "\tRoom index:17. Name:Black Creek.\n");
    assertEquals(sbComp.toString(), world.printRoomInfo(9));
    world.curHumanPlayerMove(4);
  }

  @Test
  public void petRestartDfsAfterBeingMoved() {
    FileReader fileReader = null;
    try {
      fileReader = new FileReader(filePath);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
    WorldModel world = new WorldModelImpl(fileReader);
    List<String> names = new ArrayList<>();
    List<Integer> rooms = new ArrayList<>();
    List<Boolean> ais = new ArrayList<>();
    names.add(firstPlayerName);
    names.add(secondPlayerName);
    rooms.add(firstPlayerRoomIdx);
    rooms.add(secondPlayerRoomIdx);
    ais.add(false);
    ais.add(false);
    world.setPlayers(names, rooms, ais);
    world.setMaxTurn(30);
    world.setMaxItemCarried(2);

    StringBuilder sbComp = new StringBuilder();
    sbComp.append("--1 items are in the room:\n"
            + "\tIndex:4. Name:Revolver. RoomIdx:0. Attack:3.\n"
            + "--Target character is in the room:\n"
            + "\tName:Bravo Mabel. RoomIdx:0. Health:50.\n"
            + "--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--1 adjacent rooms:\n"
            + "\tRoom index:1. Name:Hell's Kitchen.\n");
    assertEquals(sbComp.toString(), world.printRoomInfo(0));
    world.curHumanPlayerMove(0);

    sbComp.delete(0, sbComp.length());
    sbComp.append("--1 items are in the room:\n"
            + "\tIndex:8. Name:Billiard Cue. RoomIdx:1. Attack:2.\n"
            + "--Target character is in the room:\n"
            + "\tName:Bravo Mabel. RoomIdx:1. Health:50.\n"
            + "--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--3 adjacent rooms:\n"
            + "\tRoom index:0. Name:Tombstone.\n"
            + "\tRoom index:2. Name:Kill Devil Hills.\n"
            + "\tRoom index:5. Name:Deadwood.\n");
    assertEquals(sbComp.toString(), world.printRoomInfo(1));
    world.curHumanPlayerMove(4);

    sbComp.delete(0, sbComp.length());
    sbComp.append("--2 items are in the room:\n"
            + "\tIndex:6. Name:Chain Saw. RoomIdx:2. Attack:4.\n"
            + "\tIndex:11. Name:Big Red Hammer. RoomIdx:2. Attack:4.\n"
            + "--Target character is in the room:\n"
            + "\tName:Bravo Mabel. RoomIdx:2. Health:50.\n"
            + "--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--4 adjacent rooms:\n"
            + "\tRoom index:1. Name:Hell's Kitchen.\n"
            + "\tRoom index:4. Name:Seven Devils.\n"
            + "\tRoom index:5. Name:Deadwood.\n"
            + "\tRoom index:14. Name:Satan's Kingdom.\n");
    assertEquals(sbComp.toString(), world.printRoomInfo(2));
    world.curHumanPlayerMovePet(0);

    sbComp.delete(0, sbComp.length());
    sbComp.append("--1 items are in the room:\n"
            + "\tIndex:8. Name:Billiard Cue. RoomIdx:1. Attack:2.\n"
            + "--Pet is in the room:\n"
            + "\tFortune the Cat\n"
            + "--3 adjacent rooms:\n"
            + "\tRoom index:0. Name:Tombstone.\n"
            + "\tRoom index:2. Name:Kill Devil Hills.\n"
            + "\tRoom index:5. Name:Deadwood.\n");
    assertEquals(sbComp.toString(), world.printRoomInfo(1));
    world.curHumanPlayerMove(2);
  }


}