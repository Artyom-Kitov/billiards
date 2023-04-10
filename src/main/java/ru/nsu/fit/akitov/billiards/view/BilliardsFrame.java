package ru.nsu.fit.akitov.billiards.view;

import ru.nsu.fit.akitov.billiards.utils.GameProperties;
import ru.nsu.fit.akitov.billiards.utils.Point2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.List;

public class BilliardsFrame extends JFrame implements BilliardsView {

  private final String menuOption;
  private final String newGameOption;
  private final String exitOption;

  private final FieldPanel fieldPanel;

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
      repaint();
    }
  };

  public BilliardsFrame(GameProperties properties) {
    super(properties.gameName());

    int width = (int) (2 * properties.fieldSize() + 2 * properties.fieldSize() * properties.relativeBorderSize());
    int height = (int) (properties.fieldSize() + 2 * properties.fieldSize() * properties.relativeBorderSize());
    this.setPreferredSize(new Dimension(width, height));
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setVisible(true);
    this.setLayout(new BorderLayout());
    this.setResizable(false);

    menuOption = properties.menuOption();
    newGameOption = properties.newGameOption();
    exitOption = properties.exitOption();

    fieldPanel = new FieldPanel(width, height, "table.png", properties.fieldSize() / properties.relativeBallSize() / 2);
    fieldPanel.setBorderSize((int) (properties.fieldSize() * properties.relativeBorderSize()));
    this.add(fieldPanel);

    Image icon = Toolkit.getDefaultToolkit().getImage(
            Thread.currentThread().getContextClassLoader().getResource("logo.png")
    );
    this.setIconImage(icon);

    setupMenu();
    pack();
  }

  private void setupMenu() {
    JMenuBar menuBar = new JMenuBar();
    JMenu menu = new JMenu(menuOption);

    JMenuItem newGameItem = new JMenuItem(newGameOption);
    JMenuItem exitItem = new JMenuItem(exitOption);

    newGameItem.addActionListener(event -> listener.newGame());
    exitItem.addActionListener(event -> System.exit(0));

    menu.add(newGameItem);
    menu.add(exitItem);
    menuBar.add(menu);

    this.setJMenuBar(menuBar);
  }

  @Override
  public void start() {
    addKeyListener(onKeyDown);
    repaint();
  }

  @Override
  public void clear() {
    fieldPanel.clear();
  }

  @Override
  public void attachListener(ViewListener listener) {
    this.listener = listener;
  }

  @Override
  public void addCueBall(int x, int y, int radius) {
    fieldPanel.setCueBall(new Circle(radius, x, y, Color.darkGray));
  }

  @Override
  public void addBall(int x, int y, int radius) {
    fieldPanel.addBall(new Circle(radius, x, y, Color.white));
  }

  @Override
  public void addPocket(int x, int y, int radius) {
    fieldPanel.addPocket(new Circle(radius, x, y, Color.black));
  }

  @Override
  public void updateBalls(Point2D cueBall, List<Point2D> balls) {
    fieldPanel.updateBalls(cueBall, balls);
    repaint();
  }

  @Override
  public void updateCue(float velocity, float angle) {
    fieldPanel.setCueVelocity(velocity);
    fieldPanel.setCueAngle(angle);
    fieldPanel.moveCue();
  }

  @Override
  public void removeBall(int index) {
    fieldPanel.removeBall(index);
  }

  @Override
  public void setCueAvailable(boolean b) {
    fieldPanel.setCueVisible(b);
    if (b) {
      this.addKeyListener(onKeyDown);
    } else {
      this.removeKeyListener(onKeyDown);
    }
    repaint();
  }
}
