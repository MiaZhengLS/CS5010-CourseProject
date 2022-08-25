package controller;

import model.WorldModel;
import view.WorldView;

/**
 * The feature controller's concrete class.
 * The class stores reference to the model and the view, edit model and determine
 * what the view should display via commands.
 */
public class WorldFeatureControllerImpl implements WorldFeatureController {
  private final WorldModel model;
  private WorldView view;

  /**
   * Constructor of WorldFeatureControllerImpl.
   *
   * @param model world model, mutable version with full functions
   * @throws IllegalArgumentException model is null
   */
  public WorldFeatureControllerImpl(WorldModel model) throws IllegalArgumentException {
    if (model == null) {
      throw new IllegalArgumentException("model is null");
    }
    this.model = model;
  }

  @Override
  public void setView(WorldView v) {
    if (v == null) {
      throw new IllegalArgumentException("view is null");
    }
    view = v;
    view.setFeatures(this);
    view.setViewModel(model);
  }

  @Override
  public void executeCommand(WorldCommand cmd, Object param) throws IllegalArgumentException {
    if (cmd == null) {
      throw new IllegalArgumentException("invalid command!");
    }
    if (view == null) {
      throw new IllegalStateException("view not set");
    }
    cmd.execute(model, view, param);
  }
}
