package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * Controller for AI actions.
 */
public class AiActionControllerImpl implements AiActionController {

  private final List<AiActionParam> predefinedActionList;
  private int curPredefinedListIdx;
  private final Random random;
  private final boolean usePredefinedAction;

  /**
   * Constructor.
   * If pre-defined action is used, expectedAction shouldn't be empty.
   * If random action is expected, leave expectedAction empty.
   *
   * @param expectedAction pre-defined actions
   */
  public AiActionControllerImpl(String... expectedAction) throws IllegalArgumentException {
    curPredefinedListIdx = -1;

    if (expectedAction != null && expectedAction.length > 0) {
      usePredefinedAction = true;
      random = null;
      predefinedActionList = new ArrayList<>();
      for (int i = 0; i < expectedAction.length; ) {
        if (expectedAction[i].isEmpty()) {
          continue;
        }
        var action = PlayerActionType.valueOf(expectedAction[i]);
        if (action != PlayerActionType.LOOK_AROUND) {
          if (i + 1 < expectedAction.length) {
            i = i + 1;
            var param = Integer.parseInt(expectedAction[i]);
            predefinedActionList.add(new AiActionParamImpl(action, param));
            i = i + 1;
          } else {
            throw new IllegalArgumentException("lack index for move or pickupItem!");
          }
        } else {
          predefinedActionList.add(new AiActionParamImpl(action, 0));
          i = i + 1;
        }
      }
    } else {
      usePredefinedAction = false;
      predefinedActionList = null;
      random = new Random();
    }
  }

  /**
   * Whether the AI is using pre-defined actions.
   *
   * @return true for using pre-defined actions, false for not
   */
  @Override
  public boolean usePredefinedAction() {
    return usePredefinedAction;
  }

  /**
   * Get the next action of AI.
   *
   * @return next action of AI
   */
  @Override
  public PlayerActionType getRandomAction() {
    PlayerActionType at = null;
    if (random != null) {
      while (true) {
        int r = random.nextInt(PlayerActionType.getLength());
        at = PlayerActionType.values()[r];
        if (at != PlayerActionType.ATTACK) {
          break;
        }
      }
    }
    return at;
  }

  @Override
  public int getRandomNumber(Collection<Integer> resultSet)
          throws IllegalArgumentException, IllegalStateException {
    if (resultSet == null || resultSet.size() == 0) {
      throw new IllegalArgumentException("invalid result set!");
    }
    if (random == null) {
      throw new IllegalStateException("This AI controller doesn't use random!");
    }
    var rand = random.nextInt(resultSet.size());
    int ret = resultSet.iterator().next();
    for (var v : resultSet) {
      if (rand == 0) {
        ret = v;
        break;
      }
      rand--;
    }
    return ret;
  }

  @Override
  public int getRandomNumberBetween(int min, int max) throws IllegalArgumentException {
    if (max <= min) {
      throw new IllegalArgumentException("invalid max!");
    }
    if (random == null) {
      throw new IllegalStateException("This AI controller doesn't use random!");
    }
    return min + random.nextInt(max - min);
  }

  /**
   * Get the next pre-defined action and its parameter
   * wrapped in a AiAction instance.
   *
   * @return next pre-defined action and its parameter
   */
  @Override
  public AiActionParam getPredefinedAction() {
    if (predefinedActionList != null) {
      curPredefinedListIdx += 1;
      if (curPredefinedListIdx == predefinedActionList.size()) {
        curPredefinedListIdx = 0;
      }
      return predefinedActionList.get(curPredefinedListIdx);
    }
    return null;
  }
}
