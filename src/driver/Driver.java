package driver;

import controller.WorldFeatureController;
import controller.WorldFeatureControllerImpl;
import java.io.FileReader;
import java.io.IOException;
import model.WorldModel;
import model.WorldModelImpl;
import view.WorldView;
import view.WorldViewImpl;

/**
 * Driver for testing complete interaction of world controller.
 */
public class Driver {

  /**
   * Entry function for testing the game.
   *
   * @param args first is config file path, second is max turn
   */
  public static void main(String[] args) {

    if (args == null) {
      System.out.println("no args!");
      return;
    }
    if (args.length < 2) {
      System.out.println("lack information for config path and max turn!");
      return;
    }
    int maxTurn = 0;
    try {
      maxTurn = Integer.parseInt(args[1]);
    } catch (NumberFormatException nfe) {
      System.out.println("max turn should be positive integer!");
      System.out.println(args[1]);
      return;
    }

    if (maxTurn <= 0) {
      System.out.println("max turn should be positive integer!");
      System.out.println(maxTurn);
      return;
    }
    try {
      FileReader fileReader = new FileReader(args[0]);
      WorldModel model = new WorldModelImpl(fileReader);
      model.setMaxTurn(maxTurn);
      WorldFeatureController fc = new WorldFeatureControllerImpl(model);
      WorldView view = new WorldViewImpl("World");
      fc.setView(view);
    } catch (IOException | IllegalStateException ise) {
      System.out.println(ise.getMessage());
    }
  }
  //  public static void main(String[] args) {
  //    int maxTurn = 30;
  //    try {
  //      FileReader fileReader = new FileReader("GravityFalls.txt");
  //      WorldModel model = new WorldModelImpl(fileReader);
  //      model.setMaxTurn(maxTurn);
  //      WorldFeatureController fc = new WorldFeatureControllerImpl(model);
  //      WorldView view = new WorldViewImpl("World");
  //      fc.setView(view);
  //    } catch (IOException | IllegalStateException | IllegalArgumentException ise) {
  //      System.out.println(ise.getMessage());
  //    }
  //  }
}
