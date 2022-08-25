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
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.Border;


/**
 * This is a dialog that contains a text label(optional),
 * a combo box and a confirmation button.
 */
public class DialogSimpleComboBoxImpl extends JDialog implements DialogView {
  private final JComboBox comboBox;
  private final JButton btnConfirm;
  private final WorldCommand callback;
  private boolean closed;

  /**
   * Constructor of the DialogSimpleComboBoxImpl.
   * The hint is optional, usually used for telling the user what is needed.
   *
   * @param f           parent JFrame
   * @param dialogTitle title of the dialog
   * @param hint        hint for what information is displayed (optional)
   * @param content     content shown in the combo box
   * @param invokeCmd   the command that will be invoked once the confirmation button is clicked
   * @throws IllegalArgumentException f is null, or content is null or empty, or invokeCmd is null
   */
  public DialogSimpleComboBoxImpl(JFrame f, String dialogTitle, String hint, String[] content,
                                  WorldCommand invokeCmd, boolean forbidClose)
          throws IllegalArgumentException {
    super(f, dialogTitle);
    if (f == null) {
      throw new IllegalArgumentException("parameter is null");
    }
    if (content == null || content.length == 0) {
      throw new IllegalArgumentException("content list invalid");
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

    comboBox = new JComboBox(content);
    comboBox.setMaximumSize(new Dimension(200, 100));
    rootPanel.add(comboBox, BorderLayout.CENTER);
    btnConfirm = new JButton("confirm");
    rootPanel.add(btnConfirm);

    if (forbidClose) {
      setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
      for (WindowListener wl : this.getWindowListeners()) {
        this.removeWindowListener(wl);
      }
      this.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
          JOptionPane.showMessageDialog(DialogSimpleComboBoxImpl.this,
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
        int selectIdx = comboBox.getSelectedIndex();
        f.executeCommand(callback, selectIdx);
      }
    });
  }
}
