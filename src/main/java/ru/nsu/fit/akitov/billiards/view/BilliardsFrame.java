package ru.nsu.fit.akitov.billiards.view;

import ru.nsu.fit.akitov.billiards.dto.BallModel;
import ru.nsu.fit.akitov.billiards.dto.CueModel;
import ru.nsu.fit.akitov.billiards.dto.PocketModel;
import ru.nsu.fit.akitov.billiards.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.Arrays;
import java.util.List;

public class BilliardsFrame extends JFrame implements BilliardsView, NameEnterObserver {

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
  private final NameEnteringFrame nameEnteringFrame;

  private boolean started = false;

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

  @Override
  public synchronized void addKeyListener(KeyListener l) {
    if (Arrays.asList(getKeyListeners()).contains(l)) {
      return;
    }
    super.addKeyListener(l);
  }

  public BilliardsFrame(GameProperties properties) {
    super(properties.gameName());

    clockView = new ClockView(properties.upperPanelSize());

    int borderSize = (int) (properties.fieldSize() * properties.relativeBorderSize());
    int width = 2 * properties.fieldSize() + 2 * borderSize;
    int height = properties.fieldSize() + 2 * borderSize;
    int ballRadius = (int) (properties.fieldSize() * properties.relativeBallSize());
    this.setPreferredSize(new Dimension(width, 37 + height + menuBarSize + clockView.getHeight()));

    fieldView = new FieldView(width, height, ballRadius, "/field.png");
    fieldView.setBorderSize(borderSize);
    this.add(fieldView);
    this.add(clockView);

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
    nameEnteringFrame = new NameEnteringFrame();
    nameEnteringFrame.setObserver(this);

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
    started = true;
    addKeyListener(cueController);
    repaint();
  }

  @Override
  public void clear() {
    clockView.setTime(new ClockTime(0, 0));
  }

  @Override
  public void setListener(ViewListener listener) {
    this.listener = listener;
  }

  @Override
  public void addPocket(PocketModel pocket) {
    fieldView.addPocket(new PocketView(pocket.radius(), (int) pocket.position().x(), (int) pocket.position().y()));
  }

  @Override
  public void addBall(BallModel ball) {
    BallView ballView = new BallView(ball.radius(), Color.white);
    ballView.setLocation((int) ball.position().x(), (int) ball.position().y());
    fieldView.addBall(ballView);
  }

  @Override
  public void setCueBall(BallModel cueBall) {
    BallView ballView = new BallView(cueBall.radius(), Color.darkGray);
    ballView.setLocation((int) cueBall.position().x(), (int) cueBall.position().y());
    fieldView.setCueBall(ballView);
  }

  @Override
  public void updateCueBall(BallModel cueBall) {
    fieldView.updateCueBall(cueBall);
    repaint();
  }

  @Override
  public void updateBalls(List<BallModel> balls) {
    fieldView.updateBalls(balls);
    repaint();
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
  public void startEnteringName() {
    nameEnteringFrame.setVisible(true);
    this.removeKeyListener(cueController);
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

  @Override
  public void nameEntered(String name) {
    listener.playerNameEnter(name);
    nameEnteringFrame.setVisible(false);
  }

  @Override
  public void paint(Graphics g) {
    if (!started) {
      return;
    }
    super.paint(g);
  }
}
