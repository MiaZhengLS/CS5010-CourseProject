package helper;

/**
 * Helps handle serialization of some classes.
 */
public class Parser {

  private static Parser instance;

  /**
   * Get the instance of Parser.
   * There is only one Parser for an application.
   *
   * @return the instance of Parser
   */
  public static Parser getInstance() {
    if (instance == null) {
      instance = new Parser();
    }
    return instance;
  }

  /**
   * concatenate separated string from given index.
   *
   * @param splitStr an array of strings to be combine
   * @param startIdx from which index should the strings combined
   * @return combined string
   * @throws IllegalArgumentException splitStr is null, or any string within the array is null,
   *                                  or startIdx is negative or out of range
   */
  public String concatStringWithSpace(String[] splitStr, int startIdx)
          throws IllegalArgumentException {
    if (splitStr == null) {
      throw new IllegalArgumentException("splitStr is null");
    }
    if (startIdx < 0 || startIdx >= splitStr.length) {
      throw new IllegalArgumentException(String.format("startIdx %d is invalid!", startIdx));
    }

    StringBuilder sb = new StringBuilder();
    for (int i = startIdx; i < splitStr.length - 1; ++i) {
      if (splitStr[i] == null) {
        throw new IllegalArgumentException("string is null");
      }
      sb.append(splitStr[i]);
      sb.append(' ');
    }
    sb.append(splitStr[splitStr.length - 1]);
    return sb.toString();
  }

  /**
   * Convert string to int.
   *
   * @param str the string to be converted
   * @return the int that is parsed from the given string
   * @throws IllegalArgumentException str is null or empty, or cannot be converted to int
   */
  public int getIntFromString(String str) throws IllegalArgumentException {
    if (str == null || str.isEmpty()) {
      throw new IllegalArgumentException(String.format("str %s is invalid!", str));
    }
    int ret = 0;
    try {
      ret = Integer.parseInt(str);
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException(ex);
    }
    return ret;
  }
}
