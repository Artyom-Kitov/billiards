package ru.nsu.fit.akitov.billiards.view;

import ru.nsu.fit.akitov.billiards.utils.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.List;

public class BilliardsFrame extends JFrame implements BilliardsView {

  private final String menuOption;
  private final String newGameOption;
  private final String aboutGameOption;
  private final String exitOption;

  private final FieldView fieldView;
  private final ClockView clockView;
  private final int menuBarSize = 25;

  private ViewListener listener;

  private final KeyAdapter onKeyDown = new KeyAdapter() {
    @Override
    public void keyPressed(KeyEvent e) {
      switch (e.getKeyCode()) {
        case KeyEvent.VK_SPACE -> listener.onSpacePressed();
        case KeyEvent.VK_LEFT -> listener.onLeftPressed();
        case KeyEvent.VK_RIGHT -> listener.onRightPressed();
        case KeyEvent.VK_UP -> listener.onUpPressed();
        case KeyEvent.VK_DOWN -> listener.onDownPressed();
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
    aboutGameOption = properties.aboutOption();
    exitOption = properties.exitOption();

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
    JMenuItem aboutItem = new JMenuItem(aboutGameOption);
    JMenuItem exitItem = new JMenuItem(exitOption);

    newGameItem.addActionListener(event -> listener.newGame());
    aboutItem.addActionListener(event -> AboutFrame.INSTANCE.setVisible(true));
    exitItem.addActionListener(event -> System.exit(0));

    menu.add(newGameItem);
    menu.add(aboutItem);
    menu.add(exitItem);
    menuBar.add(menu);

    menuBar.setPreferredSize(new Dimension(0, menuBarSize));
    this.setJMenuBar(menuBar);
  }

  @Override
  public void start() {
    addKeyListener(onKeyDown);
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
  public void addBall(BallModel ball) {
    fieldView.addBall(new BallView(ball.radius(), (int) ball.position().x(), (int) ball.position().y(), Color.white));
  }

  @Override
  public void addCueBall(BallModel cueBall) {
    fieldView.addCueBall(cueBall);
    updateCueBall(cueBall);
  }

  @Override
  public void addPocket(PocketModel pocket) {
    fieldView.addPocket(new PocketView(pocket.radius(), (int) pocket.position().x(), (int) pocket.position().y()));
  }

  @Override
  public void updateCueBall(BallModel cueBall) {
    fieldView.setCueBallPosition((int) cueBall.position().x(), (int) cueBall.position().y());
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
  public void removeBall(int index) {
    fieldView.removeBall(index);
  }

  @Override
  public void setCueAvailable(boolean b) {
    fieldView.setCueVisible(b);
    if (b) {
      this.addKeyListener(onKeyDown);
    } else {
      this.removeKeyListener(onKeyDown);
    }
    repaint();
  }
}
