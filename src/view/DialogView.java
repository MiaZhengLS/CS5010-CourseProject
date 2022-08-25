package view;

import controller.WorldFeatureController;

/**
 * This is an interface for custom dialog in the game.
 * Dialog of this type accepts a WorldFeatureController to decide who listens to the
 * callback of any change in the dialog.
 */
public interface DialogView {

  /**
   * Check whether this dialog is closed.
   *
   * @return true means this dialog isn't closed, false means the opposite
   */
  boolean isClosed();

  /**
   * Close this dialog.
   * After this is called, if the dialog is opened before, it will be closed.
   */
  void closeDialog();

  /**
   * Set the WorldFeatureController which offers features that could handle what
   * should be executed when elements in the view are changed.
   *
   * @param f the WorldFeatureController
   * @throws IllegalArgumentException f is null
   */
  void setFeatures(WorldFeatureController f) throws IllegalArgumentException;

}
