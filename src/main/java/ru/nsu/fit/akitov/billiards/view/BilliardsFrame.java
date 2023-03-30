package ru.nsu.fit.akitov.billiards.view;

import ru.nsu.fit.akitov.billiards.presenter.BilliardsPresenter;

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
  private static final String HIGH_SCORES = "High scores";
  private static final String EXIT = "Exit";

  private final Timer timer;

  private final Table table;

  private BilliardsPresenter presenter;

  public BilliardsFrame(int sizeY) {
    super(NAME);
    setSize(1920, 1080);
    setVisible(true);
    setExtendedState(MAXIMIZED_BOTH);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    table = new Table("src/main/images/table.png");
    add(table);

    timer = new Timer(2, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        presenter.updateAll(timer.getDelay() * 0.001f);
      }
    });
    timer.restart();

    addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
      }

      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
          presenter.cueStrike(2000.0f, 2000.0f);
        }
      }

      @Override
      public void keyReleased(KeyEvent e) {

      }
    });
  }

  @Override
  public void attachPresenter(BilliardsPresenter presenter) {
    this.presenter = presenter;
  }

  @Override
  public void addCueBall(Point center, int radius) {
    table.addBall(new Circle(radius, center.x, center.y, Color.darkGray));
  }

  @Override
  public void addBall(Point center, int radius) {
    table.addBall(new Circle(radius, center.x, center.y, Color.white));
  }

  @Override
  public void addPocket(Point center, int radius) {
    table.addPocket(new Circle(radius, center.x, center.y, Color.black));
  }

  @Override
  public void updateBalls(List<Point> balls) {
    table.updateBalls(balls);
    repaint();
  }
}
