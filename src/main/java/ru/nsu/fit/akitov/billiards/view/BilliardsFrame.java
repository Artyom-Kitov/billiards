package ru.nsu.fit.akitov.billiards.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import java.util.List;

public class BilliardsFrame extends JFrame implements BilliardsView {

  private static final String NAME = "Billiards";
  private static final String MENU = "Menu";
  private static final String NEW_GAME = "New game";
  private static final String EXIT = "Exit";

  private final Timer timer;

  private final Table table;

  private ViewListener listener;

  public BilliardsFrame() {
    super(NAME);
    this.setExtendedState(MAXIMIZED_BOTH);
    this.setPreferredSize(new Dimension(1920, 1080));
    this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    this.setVisible(true);

    table = new Table("src/main/images/table.png");
    table.setLocation(0, 100);
    this.add(table);

    timer = new Timer(2, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent event) {
        listener.moveBalls(timer.getDelay() * 0.001f);
      }
    });


    setupMenu();
    pack();
  }

  private void setupMenu() {
    JMenuBar menuBar = new JMenuBar();
    menuBar.setBounds(0, 0, 100, 50);
    JMenu menu = new JMenu(MENU);

    JMenuItem newGameItem = new JMenuItem(NEW_GAME);
    JMenuItem exitItem = new JMenuItem(EXIT);

    newGameItem.addActionListener(event -> listener.newGame());
    exitItem.addActionListener(event -> System.exit(0));

    menu.add(newGameItem);
    menu.add(exitItem);
    menuBar.add(menu);

    this.setJMenuBar(menuBar);
  }

  @Override
  public void start() {
    timer.start();
    addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
      }

      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
          listener.cueStrike(2000.0f, 2500.0f);
        }
      }

      @Override
      public void keyReleased(KeyEvent e) {
      }
    });
    repaint();
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
    table.addBall(new Circle(radius, x, y, Color.darkGray));
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
  public void updateBalls(List<Point> balls) {
    table.updateBalls(balls);
    repaint();
  }
}
