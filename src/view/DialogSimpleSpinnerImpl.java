package view;

import controller.WorldCommand;
import controller.WorldFeatureController;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;
import javax.swing.border.Border;


/**
 * This is a dialog that contains a text label(optional),
 * a number spinner and a confirmation button.
 */
public class DialogSimpleSpinnerImpl extends JDialog implements DialogView {
  private final JSpinner spinnerNum;
  private final JButton btnConfirm;
  private final WorldCommand callback;
  private boolean closed;

  /**
   * Constructor of the DialogSimpleSpinnerImpl.
   * The number displayed in the spinner will be between minNum and maxNum (all included).
   * If the maxNum equal to minNum, then there will be one number in the spinner.
   * The hint is optional, usually used for telling the user what is needed.
   *
   * @param f           parent JFrame
   * @param dialogTitle title of the dialog
   * @param hint        hint for what information is displayed (optional)
   * @param minNum      the lower bound of the list of integers
   * @param maxNum      the upper bound of the list of integers
   * @param invokeCmd   the command that will be invoked once the confirmation button is clicked
   * @IllegalArgumentException f is null, or maxNum is less than minNum, or invokeCmd is null
   */
  public DialogSimpleSpinnerImpl(JFrame f, String dialogTitle, String hint, int minNum, int maxNum,
                                 WorldCommand invokeCmd, boolean forbidClose)
          throws IllegalArgumentException {
    super(f, dialogTitle);
    if (f == null) {
      throw new IllegalArgumentException("parameter is null");
    }
    if (maxNum < minNum) {
      throw new IllegalArgumentException("max num less than min num");
    }
    if (invokeCmd == null) {
      throw new IllegalArgumentException("cmd is null");
    }
    callback = invokeCmd;
    closed = false;
    setResizable(false);
    final int paddingLeft = 20;
    final int paddingRight = 20;
    final int paddingTop = 20;
    final int paddingBtm = 5;
    Border padding =
            BorderFactory.createEmptyBorder(paddingTop, paddingLeft, paddingBtm, paddingRight);

    JPanel rootPanel = new JPanel();
    rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
    rootPanel.setBorder(padding);
    add(rootPanel);
    if (hint != null && !hint.isEmpty()) {
      JLabel labelHint = new JLabel(hint);
      rootPanel.add(labelHint, BorderLayout.CENTER);
    }

    spinnerNum = new JSpinner(new SpinnerNumberModel(1, 1, maxNum, 1));
    spinnerNum.setMaximumSize(new Dimension(200, 100));
    rootPanel.add(spinnerNum, BorderLayout.CENTER);
    btnConfirm = new JButton("confirm");
    rootPanel.add(btnConfirm);

    setLocationRelativeTo(f);

    if (forbidClose) {
      setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
      for (WindowListener wl : this.getWindowListeners()) {
        this.removeWindowListener(wl);
      }
      this.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
          JOptionPane.showMessageDialog(DialogSimpleSpinnerImpl.this,
                  "The choice must be made before return");
        }
      });
    }

    pack();
    setVisible(true);
  }

  @Override
  public boolean isClosed() {
    return closed;
  }

  @Override
  public void closeDialog() {
    closed = true;
    dispose();
  }

  @Override
  public void setFeatures(WorldFeatureController f) throws IllegalArgumentException {
    if (f == null) {
      throw new IllegalArgumentException("controller is null");
    }
    btnConfirm.addActionListener(new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int itemNum = (int) spinnerNum.getValue();
        f.executeCommand(callback, itemNum);
      }
    });
  }
}
