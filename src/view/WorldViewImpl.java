package view;

import static javax.swing.SwingConstants.CENTER;

import controller.AttemptAttackStep1Command;
import controller.AttemptAttackStep2Command;
import controller.ClickPlayerCommand;
import controller.ClickRoomCommand;
import controller.ExitGameCommand;
import controller.LookAroundCommand;
import controller.MovePetStep1Command;
import controller.MovePetStep2Command;
import controller.NewGameCommand;
import controller.PickupItemStep1Command;
import controller.PickupItemStep2Command;
import controller.SetMaxItemCarriedCommand;
import controller.SetMaxTurnCommand;
import controller.TurnStartCommand;
import controller.WorldCommand;
import controller.WorldFeatureController;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.OverlayLayout;
import javax.swing.ScrollPaneLayout;
import model.WorldViewModel;


/**
 * This represents the view for the game.
 * It is based on the Java Swing framework to create the GUI.
 */
public class WorldViewImpl extends JFrame implements WorldView {

  private final JMenu topMenu;
  private final JMenuItem menuItem1;
  private final JMenuItem menuItem2;
  private final JMenuItem menuItem3;
  private final JFileChooser fileChooser;

  private final JTabbedPane paneRoot;

  private final JSplitPane paneGame;

  private final JPanel panelMap;
  private final JLayeredPane layeredPane;

  private final JLabel labelMap;

  private final JTextArea textAreaRightTop;
  private final JTextArea textAreaRightMiddle;

  private final JTextArea textAreaRightBtm;

  private final JLabel labelTargetChar;
  private final List<JLabel> labelPlayers;
  private final List<JLabel> labelPlayerNames;
  private final List<JLabel> labelRooms;
  private final List<Rectangle> roomBounds;

  private WorldFeatureController featureController;
  private WorldViewModel viewModel;

  private DialogView curDialog;

  private WorldCommand cmdOnHold;
  private boolean lockGameInput;
  private boolean lockTurnInput;

  private final int mapLayer;
  private final int playerInactiveLayer;

  private final float labelNameOffsetPercent;
  private final float sameRoomPlayerOffsetPercent;

  /**
   * Constructor of WorldViewImpl.
   *
   * @param caption title of the frame
   */
  public WorldViewImpl(String caption) {
    super(caption);
    mapLayer = 1;
    playerInactiveLayer = 2;
    int targetCharLayer = 0;

    labelNameOffsetPercent = 0.3f;
    sameRoomPlayerOffsetPercent = 0.5f;

    setSize(1300, 1000);
    setMinimumSize(new Dimension(300, 300));
    setLocation(200, 200);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //menu
    menuItem1 = new JMenuItem("New Game with current config");
    menuItem2 = new JMenuItem("New Game with new config...");
    menuItem3 = new JMenuItem("Quit");

    topMenu = new JMenu("Options");
    topMenu.add(menuItem1);
    topMenu.add(menuItem2);
    topMenu.add(menuItem3);
    JMenuBar topMenuBar = new JMenuBar();
    topMenuBar.add(topMenu);
    this.setJMenuBar(topMenuBar);

    fileChooser = new JFileChooser("../configs");

    //paneWelcome
    //welcome tab
    JPanel paneWelcome = new JPanel();
    paneWelcome.setLayout(new BoxLayout(paneWelcome, BoxLayout.Y_AXIS));

    JLabel labelWelcome = new JLabel("Welcome");
    JLabel labelAuthor = new JLabel("Author: Yanting Zheng");
    //use 0.5, not 0 to do center alignment
    labelWelcome.setAlignmentX(Component.CENTER_ALIGNMENT);
    labelAuthor.setAlignmentX(Component.CENTER_ALIGNMENT);
    paneWelcome.add(labelWelcome);
    paneWelcome.add(labelAuthor);

    //paneGame
    //right top
    JPanel panelRightTop = new JPanel();
    panelRightTop.setLayout(new BoxLayout(panelRightTop, BoxLayout.Y_AXIS));

    textAreaRightTop = new JTextArea();
    textAreaRightTop.setEditable(false);
    panelRightTop.add(textAreaRightTop);

    textAreaRightMiddle = new JTextArea();
    textAreaRightMiddle.setEditable(false);
    panelRightTop.add(textAreaRightMiddle);

    JScrollPane scrPaneRightTop = new JScrollPane();
    scrPaneRightTop.setLayout(new ScrollPaneLayout());
    scrPaneRightTop.setViewportView(panelRightTop);
    scrPaneRightTop.setPreferredSize(new Dimension(200, 300));

    //right btm
    JPanel panelRightBtm = new JPanel();
    panelRightBtm.setLayout(new BorderLayout());

    textAreaRightBtm = new JTextArea();
    textAreaRightBtm.setEditable(false);
    panelRightBtm.add(textAreaRightBtm);

    JScrollPane scrPaneRightBtm = new JScrollPane();
    scrPaneRightBtm.setLayout(new ScrollPaneLayout());
    scrPaneRightBtm.setViewportView(panelRightBtm);

    JSplitPane paneGameRight = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
            scrPaneRightTop, scrPaneRightBtm);
    paneGameRight.setMinimumSize(new Dimension(200, 300));

    //left
    JScrollPane paneGameLeft = new JScrollPane();
    paneGameLeft.setLayout(new ScrollPaneLayout());

    paneGame = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
            paneGameLeft, paneGameRight);

    paneRoot = new JTabbedPane();
    paneRoot.addTab("Welcome Page", paneWelcome);

    this.getContentPane().add(paneRoot);


    labelMap = new JLabel();
    labelMap.setOpaque(false);
    panelMap = new JPanel();
    panelMap.setLayout(new OverlayLayout(panelMap));
    panelMap.add(labelMap);
    paneGameLeft.setViewportView(panelMap);
    layeredPane = new JLayeredPane();
    layeredPane.setOpaque(false);
    labelMap.add(layeredPane);

    labelTargetChar = new JLabel();
    layeredPane.add(labelTargetChar, Integer.valueOf(targetCharLayer));
    labelPlayers = new ArrayList<>();
    labelPlayerNames = new ArrayList<>();
    labelRooms = new ArrayList<>();
    roomBounds = new ArrayList<>();

    viewModel = null;
    featureController = null;
    cmdOnHold = null;
    lockGameInput = true;
    lockTurnInput = true;

    pack();
    setLocationRelativeTo(null);

    setVisible(true);
  }

  @Override
  public void showTurnInfo(String str) {
    if (str == null) {
      throw new IllegalArgumentException("str is null");
    }
    textAreaRightTop.setText(str);
  }

  @Override
  public void showTurnResult(String str) {
    if (str == null) {
      throw new IllegalArgumentException("str is null");
    }
    textAreaRightTop.setText(str);
  }

  @Override
  public void showPrompt(String str) {
    if (str == null) {
      throw new IllegalArgumentException("str is null");
    }
    textAreaRightBtm.setText(str);
  }

  @Override
  public void showWarning(String str) {
    if (str == null) {
      throw new IllegalArgumentException("str is null");
    }
    JOptionPane.showMessageDialog(this,
            str,
            "Warning",
            JOptionPane.WARNING_MESSAGE);
  }

  @Override
  public void showGameResult(String str) {
    if (str == null) {
      throw new IllegalArgumentException("str is null");
    }
    JOptionPane.showMessageDialog(this,
            str,
            "Game Over",
            JOptionPane.INFORMATION_MESSAGE);
  }

  @Override
  public void movePlayerTo(int playerIdx, int roomIdx) {
    if (playerIdx < 0 || playerIdx >= labelPlayers.size()) {
      throw new IllegalArgumentException("player index invalid");
    }
    if (roomIdx < 0 || roomIdx >= labelRooms.size()) {
      throw new IllegalArgumentException("room index invalid");
    }
    putPlayerToRoom(playerIdx, roomIdx);
  }

  @Override
  public void moveTargetCharacterTo(int roomIdx) throws IllegalArgumentException {
    if (roomIdx >= roomBounds.size()) {
      throw new IllegalArgumentException("roomBounds may not be initialized");
    }
    if (labelTargetChar.getIcon() == null) {
      throw new IllegalStateException("graphical target character isn't initialized");
    }
    Rectangle rb = roomBounds.get(roomIdx);
    labelTargetChar.setBounds(
            new Rectangle(
                    rb.x + rb.width / 2 - labelTargetChar.getIcon().getIconWidth() / 2,
                    rb.y + rb.height / 2 - labelTargetChar.getIcon().getIconHeight() / 2,
                    labelTargetChar.getIcon().getIconWidth(),
                    labelTargetChar.getIcon().getIconHeight()));
  }

  @Override
  public void showPlayerInfo(String str) {
    textAreaRightMiddle.setText(str);
  }

  @Override
  public void resetAllInfo() {
    textAreaRightTop.setText("");
    textAreaRightMiddle.setText("");
    textAreaRightBtm.setText("");
  }

  @Override
  public void resetAllData() {
    cmdOnHold = null;
    if (curDialog != null && !curDialog.isClosed()) {
      curDialog.closeDialog();
    }
    curDialog = null;
    lockGameInput = true;
    lockTurnInput = true;
    resetAllInfo();
  }

  @Override
  public void initMap(String mapImgFilePath, List<int[]> roomBounds,
                      List<String> playerIcons, String targetIcon) {
    if (paneRoot.getTabCount() == 1) {
      paneRoot.addTab("Game", paneGame);
    }
    if (mapImgFilePath == null || mapImgFilePath.isEmpty()) {
      throw new IllegalArgumentException("mapImgFilePath invalid");
    }
    if (roomBounds == null) {
      throw new IllegalArgumentException("roomBounds is null");
    }
    if (playerIcons == null) {
      throw new IllegalArgumentException("playerIcons is null");
    }
    if (targetIcon == null || targetIcon.isEmpty()) {
      throw new IllegalArgumentException("targetIcon invalid");
    }
    paneRoot.setSelectedIndex(1);
    loadMap(mapImgFilePath);
    initRooms(roomBounds);
    initPlayers(playerIcons);
    initTargetCharacter(targetIcon);
    pack();
    setLocationRelativeTo(null);
  }

  private void loadMap(String mapImgFilePath) {
    try {
      BufferedImage myPicture = ImageIO.read(new File(mapImgFilePath));
      labelMap.setIcon(new ImageIcon(myPicture));
      labelMap.setHorizontalAlignment(CENTER);
      labelMap.setBounds(new Rectangle(0, 0, myPicture.getWidth(), myPicture.getHeight()));
      layeredPane.setSize(new Dimension(myPicture.getWidth(), myPicture.getHeight()));
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
  }

  private void initRooms(List<int[]> bounds) {
    roomBounds.clear();
    int roomNum = viewModel.getRoomNumber();
    for (int i = 0; i < roomNum; ++i) {
      JLabel label = null;
      if (i >= labelRooms.size()) {
        label = new JLabel();
        labelRooms.add(label);
      } else {
        label = labelRooms.get(i);
      }
      layeredPane.add(label, Integer.valueOf(mapLayer));
      int[] b = bounds.get(i);
      if (b.length != 4) {
        throw new IllegalArgumentException("array should have four elements");
      }
      Rectangle rectangle = new Rectangle(b[0], b[1], b[2], b[3]);
      label.setBounds(rectangle);
      roomBounds.add(rectangle);

      label.setOpaque(false);
      final int roomIdx = i;
      label.removeAll();
      label.addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          if (lockTurnInput) {
            return;
          }
          super.mouseClicked(e);
          featureController.executeCommand(new ClickRoomCommand(), roomIdx);
        }
      });
    }
    for (int i = labelRooms.size() - 1; i >= roomNum; --i) {
      layeredPane.remove(labelRooms.get(i));
      labelRooms.remove(i);
    }
  }

  private void initPlayers(List<String> iconPath) {
    if (iconPath == null) {
      throw new IllegalArgumentException("player icon path invalid");
    }
    for (int i = 0; i < iconPath.size(); ++i) {
      try {
        String path = iconPath.get(i);
        BufferedImage pic = ImageIO.read(new File(path));
        Image scaledPic = pic.getScaledInstance(
                pic.getWidth() / 5, pic.getHeight() / 5, java.awt.Image.SCALE_SMOOTH);
        JLabel label = null;
        JLabel labelName = null;
        if (i >= labelPlayers.size()) {
          label = new JLabel();
          labelPlayers.add(label);
          labelName = new JLabel();
          labelPlayerNames.add(labelName);
          layeredPane.add(label, Integer.valueOf(playerInactiveLayer));
          layeredPane.add(labelName, Integer.valueOf(playerInactiveLayer));
        } else {
          label = labelPlayers.get(i);
          labelName = labelPlayerNames.get(i);
        }
        label.setIcon(new ImageIcon(scaledPic));
        label.setOpaque(false);
        labelName.setText(viewModel.getPlayerName(i));
        final int playerIdx = i;
        label.removeAll();
        label.addMouseListener(new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            if (lockGameInput) {
              return;
            }
            super.mouseClicked(e);
            featureController.executeCommand(new ClickPlayerCommand(), playerIdx);
          }
        });

        int room = viewModel.getPlayerRoomIdx(i);
        putPlayerToRoom(i, room);
      } catch (IOException ioe) {
        throw new IllegalStateException(ioe.getMessage());
      }
    }
    for (int i = labelPlayers.size() - 1; i >= iconPath.size(); --i) {
      layeredPane.remove(labelPlayers.get(i));
      labelPlayers.remove(i);
    }
    for (int i = labelPlayerNames.size() - 1; i >= iconPath.size(); --i) {
      layeredPane.remove(labelPlayerNames.get(i));
      labelPlayerNames.remove(i);
    }
  }

  private void initTargetCharacter(String iconPath) {
    if (iconPath == null || iconPath.isEmpty()) {
      throw new IllegalArgumentException("target character icon path invalid");
    }
    var roomIdx = viewModel.getTargetCharacterRoomIdx();
    if (roomIdx >= roomBounds.size()) {
      throw new IllegalArgumentException("roomBounds may not be initialized");
    }
    try {
      BufferedImage pic = ImageIO.read(new File(iconPath));
      Image scaledPic = pic.getScaledInstance(
              pic.getWidth() / 3, pic.getHeight() / 3, java.awt.Image.SCALE_SMOOTH);
      labelTargetChar.setIcon(new ImageIcon(scaledPic));
      labelTargetChar.setOpaque(false);
      moveTargetCharacterTo(roomIdx);
    } catch (IOException ioe) {
      throw new IllegalStateException(ioe.getMessage());
    }
  }

  private void putPlayerToRoom(int playerIdx, int roomIdx) throws IllegalStateException {
    if (roomIdx >= roomBounds.size()) {
      throw new IllegalArgumentException("roomBounds may not be initialized");
    }
    JLabel l = labelPlayers.get(playerIdx);
    if (l.getIcon() == null) {
      throw new IllegalStateException("graphical player isn't initialized");
    }
    Rectangle rb = roomBounds.get(roomIdx);
    Rectangle pb = new Rectangle(rb.x + rb.width / 2 - l.getIcon().getIconWidth() / 2,
            rb.y + rb.height / 2 - l.getIcon().getIconHeight() / 2,
            l.getIcon().getIconWidth(),
            l.getIcon().getIconHeight());
    l.setBounds(pb);

    JLabel ln = labelPlayerNames.get(playerIdx);
    ln.setBounds(
            new Rectangle(pb.x + (pb.width - ln.getPreferredSize().width) / 2,
                    pb.y - (int) (pb.height * labelNameOffsetPercent),
                    ln.getPreferredSize().width,
                    ln.getPreferredSize().height));

    JLabel room = labelRooms.get(roomIdx);
    var roomBounds = room.getBounds();
    List<JLabel> sameRoomLabels = new ArrayList<>();
    List<JLabel> sameRoomLabelNames = new ArrayList<>();
    for (int i = 0; i < labelPlayers.size(); ++i) {
      if (!labelPlayers.get(i).equals(l)) {
        var b = labelPlayers.get(i).getBounds();
        var center = new Point(b.x + b.width / 2, b.y + b.height / 2);
        if (center.x > roomBounds.x && center.x < roomBounds.x + roomBounds.width
                && center.y > roomBounds.y && center.y < roomBounds.y + roomBounds.height) {
          sameRoomLabels.add(labelPlayers.get(i));
          sameRoomLabelNames.add(labelPlayerNames.get(i));
        }
      }
    }
    if (sameRoomLabels.size() > 0) {
      int flipIdx = 1;
      int count = 1;
      for (int i = 0; i < sameRoomLabels.size(); ++i) {
        var label = sameRoomLabels.get(i);
        Rectangle ppb = new Rectangle(
                pb.x + (int) (count * flipIdx * label.getIcon().getIconWidth()
                        * sameRoomPlayerOffsetPercent),
                pb.y,
                label.getIcon().getIconWidth(),
                label.getIcon().getIconHeight());
        label.setBounds(ppb);
        var labelName = sameRoomLabelNames.get(i);
        labelName.setBounds(new Rectangle(
                ppb.x + (ppb.width - labelName.getPreferredSize().width) / 2,
                ppb.y - (int) (ppb.height * labelNameOffsetPercent),
                labelName.getPreferredSize().width,
                labelName.getPreferredSize().height));
        flipIdx *= -1;
        count = i % 2 > 0 ? (count + 1) : count;
      }
    }
  }

  @Override
  public void showInitPlayerPanel(int maxPlayerNum) {
    if (featureController == null) {
      throw new IllegalStateException("feature not set");
    }
    curDialog = new DialogInitPlayersImpl(this, viewModel.getRoomNumber() - 1, maxPlayerNum, true);
    topMenu.setEnabled(false);
    curDialog.setFeatures(featureController);
  }

  @Override
  public void closeInitPlayerPanel() {
    //System.out.println("#2");
    topMenu.setEnabled(true);
    closeCurDialog();
  }

  @Override
  public void showInitMaxItemCarriedPanel() {
    if (featureController == null) {
      throw new IllegalStateException("feature not set");
    }
    curDialog =
            new DialogSimpleSpinnerImpl(this,
                    "Init Max Item",
                    "Select the max item a player can carry",
                    1, viewModel.getItemNumber(),
                    new SetMaxItemCarriedCommand(), true);
    topMenu.setEnabled(false);
    curDialog.setFeatures(featureController);
  }

  @Override
  public void closeInitMaxItemCarriedPanel() {
    topMenu.setEnabled(true);
    closeCurDialog();
  }

  @Override
  public void showInitMaxTurnPanel() {
    if (featureController == null) {
      throw new IllegalStateException("feature not set");
    }
    curDialog =
            new DialogSimpleSpinnerImpl(this,
                    "Init Max Turn",
                    "Select the max turn (which marks the end of the game)",
                    1, viewModel.getItemNumber(), new SetMaxTurnCommand(),
                    true);
    topMenu.setEnabled(false);
    curDialog.setFeatures(featureController);
  }

  @Override
  public void closeInitMaxTurnPanel() {
    topMenu.setEnabled(true);
    closeCurDialog();
  }

  @Override
  public void showPickupItemPanel(List<Integer> items) {
    if (featureController == null) {
      throw new IllegalStateException("feature not set");
    }
    if (items == null || items.isEmpty()) {
      throw new IllegalArgumentException("items invalid");
    }
    String[] comboList = new String[items.size()];
    for (int i = 0; i < items.size(); ++i) {
      String str = String.format("%s (atk-%d)",
              viewModel.getItemName(items.get(i)),
              viewModel.getItemAttack(items.get(i)));
      comboList[i] = str;
    }
    cmdOnHold = new PickupItemStep2Command();
    curDialog = new DialogSimpleComboBoxImpl(WorldViewImpl.this,
            "Select Item",
            "Select an item to pick",
            comboList, cmdOnHold, true);
    topMenu.setEnabled(false);
    curDialog.setFeatures(featureController);
  }

  @Override
  public void closePickupItemPanel() {
    cmdOnHold = null;
    topMenu.setEnabled(true);
    closeCurDialog();
  }

  @Override
  public void showMovePetPanel() {
    if (featureController == null) {
      throw new IllegalStateException("feature not set");
    }

    String[] comboList = new String[viewModel.getRoomNumber()];
    for (int i = 0; i < comboList.length; ++i) {
      String str = String.format("%d-%s",
              i, viewModel.getRoomName(i));
      comboList[i] = str;
    }
    cmdOnHold = new MovePetStep2Command();
    curDialog = new DialogSimpleComboBoxImpl(WorldViewImpl.this,
            "Move Pet",
            "Select a room to move to",
            comboList, cmdOnHold, true);
    topMenu.setEnabled(false);
    curDialog.setFeatures(featureController);
  }

  @Override
  public void closeMovePetPanel() {
    cmdOnHold = null;
    topMenu.setEnabled(true);
    closeCurDialog();
  }

  @Override
  public void showUseItemPanel(List<Integer> items) {
    if (featureController == null) {
      throw new IllegalStateException("feature not set");
    }
    String[] comboList = new String[items.size()];
    for (int i = 0; i < items.size(); ++i) {
      String str = String.format("%s (atk-%d)",
              viewModel.getItemName(items.get(i)),
              viewModel.getItemAttack(items.get(i)));
      comboList[i] = str;
    }
    cmdOnHold = new AttemptAttackStep2Command();
    curDialog = new DialogSimpleComboBoxImpl(WorldViewImpl.this,
            "Select Item",
            "Select an item to use",
            comboList, cmdOnHold, true);
    topMenu.setEnabled(false);
    curDialog.setFeatures(featureController);
  }

  @Override
  public void closeUseItemPanel() {
    cmdOnHold = null;
    topMenu.setEnabled(true);
    closeCurDialog();
  }

  private void closeCurDialog() {
    if (curDialog == null || curDialog.isClosed()) {
      return;
    }
    //System.out.println("#3");
    curDialog.closeDialog();
  }

  @Override
  public void setViewLock(boolean lockGameInput, boolean lockTurnInput) {
    this.lockGameInput = lockGameInput;
    this.lockTurnInput = lockTurnInput;
  }

  @Override
  public void setFeatures(WorldFeatureController f) {
    if (f == null) {
      throw new IllegalArgumentException("feature is null!");
    }
    featureController = f;
    //menu
    menuItem1.addActionListener(
            l -> featureController.executeCommand(new NewGameCommand(), null));
    menuItem2.addActionListener(new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        var ret = fileChooser.showOpenDialog(WorldViewImpl.this);
        if (ret == JFileChooser.APPROVE_OPTION) {
          featureController.executeCommand(new NewGameCommand(), fileChooser.getSelectedFile());
        }
      }
    });
    menuItem3.addActionListener(l ->
            featureController.executeCommand(new ExitGameCommand(), null));
    setFocusable(true);
    addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
        if (lockGameInput) {
          return;
        }
        if (cmdOnHold != null) {
          //System.out.println("cmdOnHold :"+cmdOnHold);
          return;
        }
        char keyChar = e.getKeyChar();
        WorldCommand cmd = null;
        Object param = null;

        switch (keyChar) {
          case 'c':
            cmd = new TurnStartCommand();
            break;
          case 'p':
            if (!lockTurnInput) {
              cmd = new PickupItemStep1Command();
              cmdOnHold = cmd;
            }
            break;
          case 'l':
            if (!lockTurnInput) {
              cmd = new LookAroundCommand();
            }
            break;
          case 'a':
            if (!lockTurnInput) {
              cmd = new AttemptAttackStep1Command();
              cmdOnHold = cmd;
            }
            break;
          case 'm':
            if (!lockTurnInput) {
              cmd = new MovePetStep1Command();
              cmdOnHold = cmd;
            }
            break;
          default:
            break;
        }
        if (cmd != null) {
          featureController.executeCommand(cmd, param);
        }
      }

      @Override
      public void keyPressed(KeyEvent e) {

      }

      @Override
      public void keyReleased(KeyEvent e) {

      }
    });
  }

  @Override
  public void setViewModel(WorldViewModel m) throws IllegalArgumentException {
    if (m == null) {
      throw new IllegalArgumentException("view model is null!");
    }
    viewModel = m;
  }
}
