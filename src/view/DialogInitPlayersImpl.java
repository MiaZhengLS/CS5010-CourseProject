package view;

import controller.SetPlayerCommand;
import controller.WorldCommand;
import controller.WorldFeatureController;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * Dialogue used to set players' information.
 */
public class DialogInitPlayersImpl extends JDialog implements DialogView {

  private final JPanel rootPanel;
  private final JSpinner spinnerPlayerNum;
  private final JPanel middlePanel;
  private final JButton btnConfirm;
  private final List<JLabel> labelPlayerName;
  private final List<JTextField> textFieldPlayerName;
  private final List<JLabel> labelPlayerRoomIdx;
  private final List<JSpinner> spinnerPlayerRoomIdx;
  private final List<JLabel> labelPlaceholder;
  private final List<JCheckBox> checkBoxPlayerType;

  private boolean closed;
  private final int maxGap;
  private final int paddingLeft;
  private final int paddingRight;
  private final int paddingTop;
  private final int paddingBtm;

  private final int maxRoomIdx;


  /**
   * Constructor of DialogInitPlayersImpl.
   *
   * @param f parent JFrame
   * @param maxRoomIdx max room index
   * @param maxPlayerNum max player number
   * @throws IllegalArgumentException maxRoomIdx is negative or maxPlayerNum is less than 1
   */
  public DialogInitPlayersImpl(JFrame f, int maxRoomIdx, int maxPlayerNum, boolean forbidClose)
          throws IllegalArgumentException {
    super(f, "Init Player");
    if (f == null) {
      throw new IllegalArgumentException("parameter is null");
    }
    if (maxRoomIdx < 0 || maxPlayerNum < 1) {
      throw new IllegalArgumentException("upper limit is invalid");
    }
    closed = false;
    setResizable(false);
    maxGap = 10;
    paddingLeft = 20;
    paddingRight = 20;
    paddingTop = 20;
    paddingBtm = 5;
    Border padding =
            BorderFactory.createEmptyBorder(paddingTop, paddingLeft, paddingBtm, paddingRight);

    rootPanel = new JPanel();
    rootPanel.setLayout(new BoxLayout(rootPanel, BoxLayout.Y_AXIS));
    rootPanel.setBorder(padding);
    this.add(rootPanel);
    JPanel topPanel = new JPanel();
    SpinnerNumberModel model1 =
            new SpinnerNumberModel(1, 1, maxPlayerNum, 1);
    spinnerPlayerNum = new JSpinner(model1);
    spinnerPlayerNum.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        SpinnerModel playerNumModel = spinnerPlayerNum.getModel();
        if ((int) playerNumModel.getValue() != labelPlayerName.size()) {
          setPlayerList();
          pack();
        }
      }
    });
    topPanel.add(spinnerPlayerNum);
    rootPanel.add(topPanel);

    GridLayout layout = new GridLayout(2, 3);
    middlePanel = new JPanel();
    middlePanel.setLayout(layout);
    rootPanel.add(middlePanel);

    JPanel btmPanel = new JPanel();
    btnConfirm = new JButton("confirm");
    btmPanel.add(btnConfirm);
    rootPanel.add(btmPanel);

    labelPlayerName = new ArrayList<>();
    textFieldPlayerName = new ArrayList<>();
    labelPlayerRoomIdx = new ArrayList<>();
    spinnerPlayerRoomIdx = new ArrayList<>();
    labelPlaceholder = new ArrayList<>();
    checkBoxPlayerType = new ArrayList<>();

    this.maxRoomIdx = maxRoomIdx;
    setPlayerList();

    setLocationRelativeTo(f);

    if (forbidClose) {
      setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
      for (WindowListener wl : this.getWindowListeners()) {
        this.removeWindowListener(wl);
      }
      this.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
          JOptionPane.showMessageDialog(DialogInitPlayersImpl.this,
                  "The choice must be made before return");
        }
      });
    }

    pack();
    setVisible(true);
  }

  private void setPlayerList() {
    int curSelectPlayerNum = (int) spinnerPlayerNum.getValue();
    GridLayout layout = new GridLayout(curSelectPlayerNum * 2, 3);
    middlePanel.setLayout(layout);
    for (int i = 0; i < curSelectPlayerNum; ++i) {
      JLabel labelNameTitle = null;
      JTextField textFieldName = null;
      JLabel labelRoomTitle = null;
      JSpinner spinnerRoomIdx = null;
      JLabel labelEmpty = null;
      JCheckBox checkBoxAi = null;

      if (i >= labelPlayerName.size()) {
        labelNameTitle = new JLabel();
        labelPlayerName.add(labelNameTitle);
        textFieldName = new JTextField();
        textFieldPlayerName.add(textFieldName);
        labelRoomTitle = new JLabel("Room index");
        labelPlayerRoomIdx.add(labelRoomTitle);
        var playerRoomSpinnerModel = new SpinnerNumberModel(0, 0, maxRoomIdx, 1);
        spinnerRoomIdx = new JSpinner();
        spinnerRoomIdx.setModel(playerRoomSpinnerModel);

        spinnerPlayerRoomIdx.add(spinnerRoomIdx);
        labelEmpty = new JLabel(" ");
        labelPlaceholder.add(labelEmpty);
        checkBoxAi = new JCheckBox("is AI");
        checkBoxPlayerType.add(checkBoxAi);
        middlePanel.add(labelNameTitle, BorderLayout.CENTER);
        middlePanel.add(labelRoomTitle, BorderLayout.CENTER);
        middlePanel.add(labelEmpty);
        middlePanel.add(textFieldName, BorderLayout.CENTER);
        middlePanel.add(spinnerRoomIdx, BorderLayout.CENTER);
        middlePanel.add(checkBoxAi, BorderLayout.CENTER);
      } else {
        labelNameTitle = labelPlayerName.get(i);
        labelNameTitle.setVisible(true);
        textFieldName = textFieldPlayerName.get(i);
        textFieldName.setVisible(true);
        labelRoomTitle = labelPlayerRoomIdx.get(i);
        labelRoomTitle.setVisible(true);
        spinnerRoomIdx = spinnerPlayerRoomIdx.get(i);
        spinnerRoomIdx.setVisible(true);
        labelEmpty = labelPlaceholder.get(i);
        labelEmpty.setVisible(true);
        checkBoxAi = checkBoxPlayerType.get(i);
        checkBoxAi.setVisible(true);
      }
      labelNameTitle.setText(String.format("Player %d's name", (i + 1)));
      textFieldName.setText(String.format("Player %d", (i + 1)));
    }

    for (int i = labelPlayerName.size() - 1; i >= curSelectPlayerNum; --i) {
      middlePanel.remove(labelPlayerName.get(i));
      middlePanel.remove(textFieldPlayerName.get(i));
      middlePanel.remove(labelPlayerRoomIdx.get(i));
      middlePanel.remove(spinnerPlayerRoomIdx.get(i));
      middlePanel.remove(checkBoxPlayerType.get(i));
      middlePanel.remove(labelPlaceholder.get(i));
      labelPlayerName.remove(i);
      textFieldPlayerName.remove(i);
      labelPlayerRoomIdx.remove(i);
      spinnerPlayerRoomIdx.remove(i);
      checkBoxPlayerType.remove(i);
      labelPlaceholder.remove(i);

    }
    middlePanel.revalidate();
    middlePanel.repaint();
    middlePanel.setSize(new Dimension(
            (int) (labelPlayerName.get(0).getWidth() * 3.5) + maxGap * 2,
            labelPlayerName.get(0).getHeight() * curSelectPlayerNum * 2 + maxGap * 2));

    var sz = middlePanel.getSize();
    rootPanel.setSize(new Dimension(sz.width + paddingLeft + paddingRight,
            sz.height + paddingTop + paddingBtm));
  }

  @Override
  public boolean isClosed() {
    return closed;
  }

  @Override
  public void closeDialog() {
    //System.out.println("#4");
    closed = true;
    dispose();
    //System.out.println("#5");
  }

  @Override
  public void setFeatures(WorldFeatureController f) throws IllegalArgumentException {
    if (f == null) {
      throw new IllegalArgumentException("controller is null");
    }
    btnConfirm.addActionListener(new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        List<String> names = new ArrayList<>();
        List<Integer> rooms = new ArrayList<>();
        List<Boolean> isAi = new ArrayList<>();
        int playerNum = (int) spinnerPlayerNum.getValue();
        for (int i = 0; i < playerNum; ++i) {
          names.add(textFieldPlayerName.get(i).getText());
          rooms.add((int) spinnerPlayerRoomIdx.get(i).getValue());
          isAi.add(checkBoxPlayerType.get(i).isSelected());
        }
        WorldCommand cmd = new SetPlayerCommand(names, rooms, isAi);
        f.executeCommand(cmd, null);
      }
    });
  }
}
