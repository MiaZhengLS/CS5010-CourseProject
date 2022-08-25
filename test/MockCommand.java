import controller.WorldCommand;
import model.WorldModel;
import view.WorldView;

/**
 * Mock WorldCommand for testing controller.
 */
public class MockCommand implements WorldCommand {
  private final StringBuilder log;
  private final int uniqueCode;

  /**
   * Constructor of MockCommand.
   *
   * @param log        output information
   * @param uniqueCode mark this unique instance
   */
  public MockCommand(StringBuilder log, int uniqueCode) {
    this.log = log;
    this.uniqueCode = uniqueCode;
  }

  @Override
  public void execute(WorldModel world, WorldView view, Object param) {
    log.append("execute: ").append(uniqueCode).append("\n");
  }
}
