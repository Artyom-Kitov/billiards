package ru.nsu.fit.akitov.billiards.view;

import ru.nsu.fit.akitov.billiards.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.List;

public class BilliardsFrame extends JFrame implements BilliardsView {

  private final String menuOption;
  private final String newGameOption;
  private final String highscoresOption;
  private final String aboutGameOption;
  private final String exitOption;

  private final FieldView fieldView;
  private final ClockView clockView;
  private final int menuBarSize = 25;

  private final HighscoresFrame highscoresFrame;
  private final AboutFrame aboutFrame;

  private ViewListener listener;

  private final KeyAdapter cueController = new KeyAdapter() {
    @Override
    public void keyPressed(KeyEvent event) {
      switch (event.getKeyCode()) {
        case KeyEvent.VK_SPACE -> listener.cueStrike();
        case KeyEvent.VK_LEFT -> listener.rotateCueLeft();
        case KeyEvent.VK_RIGHT -> listener.rotateCueRight();
        case KeyEvent.VK_UP -> listener.reduceCueVelocity();
        case KeyEvent.VK_DOWN -> listener.increaseCueVelocity();
      }
    }
  };

  private final KeyAdapter cueBallPlacer = new KeyAdapter() {
    @Override
    public void keyPressed(KeyEvent event) {
      switch (event.getKeyCode()) {
        case KeyEvent.VK_SPACE -> listener.placeCueBall();
        case KeyEvent.VK_LEFT -> listener.moveCueBallLeft();
        case KeyEvent.VK_RIGHT -> listener.moveCueBallRight();
        case KeyEvent.VK_UP -> listener.moveCueBallUp();
        case KeyEvent.VK_DOWN -> listener.moveCueBallDown();
      }
    }
  };

  public BilliardsFrame(GameProperties properties) {
    super(properties.gameName());

    int borderSize = (int) (properties.fieldSize() * properties.relativeBorderSize());
    int width = 2 * properties.fieldSize() + 2 * borderSize;
    int height = properties.fieldSize() + 2 * borderSize;
    int ballRadius = (int) (properties.fieldSize() * properties.relativeBallSize());

    fieldView = new FieldView(width, height, ballRadius, "/field.png");
    fieldView.setBorderSize(borderSize);
    clockView = new ClockView(properties.upperPanelSize());
    this.add(fieldView);
    this.add(clockView);

    this.setPreferredSize(new Dimension(width, height + 2 * menuBarSize + 3 + clockView.getHeight()));
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setVisible(true);
    this.setResizable(false);
    this.getContentPane().setBackground(Color.gray);

    menuOption = properties.menuOption();
    newGameOption = properties.newGameOption();
    highscoresOption = properties.highscoresOption();
    aboutGameOption = properties.aboutOption();
    exitOption = properties.exitOption();

    highscoresFrame = new HighscoresFrame();
    aboutFrame = new AboutFrame();

    SpringLayout layout = new SpringLayout();
    layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, clockView, 0, SpringLayout.HORIZONTAL_CENTER, getContentPane());
    layout.putConstraint(SpringLayout.NORTH, fieldView, 0, SpringLayout.SOUTH, clockView);

    Image icon = Toolkit.getDefaultToolkit().getImage(BilliardsFrame.class.getResource("/logo.png"));
    this.setIconImage(icon);

    this.setLayout(layout);
    this.setupMenu();
    this.pack();
  }

  private void setupMenu() {
    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu(menuOption);

    JMenuItem newGameItem = new JMenuItem(newGameOption);
    JMenuItem highscoresItem = new JMenuItem(highscoresOption);
    JMenuItem aboutItem = new JMenuItem(aboutGameOption);
    JMenuItem exitItem = new JMenuItem(exitOption);

    newGameItem.addActionListener(event -> listener.newGame());
    highscoresItem.addActionListener(event -> highscoresFrame.setVisible(true));
    aboutItem.addActionListener(event -> aboutFrame.setVisible(true));
    exitItem.addActionListener(event -> System.exit(0));

    menu.add(newGameItem);
    menu.add(highscoresItem);
    menu.add(aboutItem);
    menu.add(exitItem);
    menuBar.add(menu);

    menuBar.setPreferredSize(new Dimension(0, menuBarSize));
    this.setJMenuBar(menuBar);
  }

  @Override
  public void start() {
    addKeyListener(cueController);
    repaint();
  }

  @Override
  public void clear() {
    fieldView.clear();
    clockView.setTime(new ClockTime(0, 0));
  }

  @Override
  public void attachListener(ViewListener listener) {
    this.listener = listener;
  }

  @Override
  public void addPocket(PocketModel pocket) {
    fieldView.addPocket(new PocketView(pocket.radius(), (int) pocket.position().x(), (int) pocket.position().y()));
  }

  @Override
  public void updateBalls(List<BallModel> balls) {
    fieldView.updateBalls(balls);
    repaint();
  }

  @Override
  public void resetBalls(List<BallModel> balls) {
    fieldView.resetBalls(balls);
  }

  @Override
  public void updateCue(CueModel cue) {
    fieldView.setCueVelocity(cue.velocity());
    fieldView.setCueAngle(cue.angle());
    fieldView.moveCue();
    repaint();
  }

  @Override
  public void updateTime(ClockTime time) {
    clockView.setTime(time);
  }

  @Override
  public void startPlacingCueBall() {
    this.removeKeyListener(cueController);
    this.addKeyListener(cueBallPlacer);
    setCueAvailable(false);
  }

  @Override
  public void stopPlacingCueBall() {
    this.removeKeyListener(cueBallPlacer);
    this.addKeyListener(cueController);
    setCueAvailable(true);
  }

  @Override
  public void setCueAvailable(boolean b) {
    fieldView.setCueVisible(b);
    if (b) {
      this.addKeyListener(cueController);
    } else {
      this.removeKeyListener(cueController);
    }
    repaint();
  }
}
