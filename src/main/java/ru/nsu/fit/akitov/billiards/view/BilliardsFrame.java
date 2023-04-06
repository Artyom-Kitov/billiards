package ru.nsu.fit.akitov.billiards.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.List;

public class BilliardsFrame extends JFrame implements BilliardsView {

  // CR: move to properties
  private static final String NAME = "Billiards";
  private static final String MENU = "Menu";
  private static final String NEW_GAME = "New game";
  private static final String EXIT = "Exit";

  private final Table table;
  private final CuePanel cuePanel;

  private ViewListener listener;

  public BilliardsFrame() {
    super(NAME);
    this.setExtendedState(MAXIMIZED_BOTH);
    // CR: hardcode
    this.setPreferredSize(new Dimension(1920, 1080));
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setVisible(true);

    table = new Table("src/main/resources/table.png");
    // CR: layout
    table.setLocation(0, 100);
    this.add(table);

    cuePanel = table.getCuePanel();

    setupMenu();
    pack();
  }

  private void setupMenu() {
    JMenuBar menuBar = new JMenuBar();
    menuBar.setBounds(0, 0, 100, 50);
    JMenu menu = new JMenu(MENU);

    JMenuItem newGameItem = new JMenuItem(NEW_GAME);
    JMenuItem exitItem = new JMenuItem(EXIT);

    newGameItem.addActionListener((event) -> listener.newGame());
    exitItem.addActionListener(event -> System.exit(0));

    menu.add(newGameItem);
    menu.add(exitItem);
    menuBar.add(menu);

    this.setJMenuBar(menuBar);
  }

  @Override
  public void start() {
    addKeyListener(new KeyAdapter() {
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
    });
  }

  @Override
  public void clear() {
    table.clear();
  }

  @Override
  public void attachListener(ViewListener listener) {
    this.listener = listener;
  }

  @Override
  public void addCueBall(int x, int y, int radius) {
    table.setCueBall(new Circle(radius, x, y, Color.darkGray));
  }

  @Override
  public void addBall(int x, int y, int radius) {
    table.addBall(new Circle(radius, x, y, Color.white));
  }

  @Override
  public void addPocket(int x, int y, int radius) {
    table.addPocket(new Circle(radius, x, y, Color.black));
  }

  @Override
  public void updateBalls(Point cueBall, List<Point> balls) {
    table.updateBalls(cueBall, balls);
    repaint();
  }

  @Override
  public void updateCue(float velocity, float angle) {
    cuePanel.setVelocity(velocity);
    cuePanel.setAngle(angle);
    table.moveCue();
  }

  @Override
  public void setCueVisible(boolean b) {
    cuePanel.setVisible(b);
    repaint();
  }
}
