package ru.nsu.fit.akitov.billiards.view;

import ru.nsu.fit.akitov.billiards.presenter.BilliardsPresenter;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class BilliardsFrame extends JFrame implements BilliardsView {
  private static final String NAME = "Billiards";
  private static final String MENU = "Menu";
  private static final String NEW_GAME = "New game";
  private static final String HIGH_SCORES = "High scores";
  private static final String EXIT = "Exit";

  private Table table;

  private BilliardsPresenter presenter;

  public BilliardsFrame(int sizeY) {
    super(NAME);
    setSize(1920, 1080);
    setVisible(true);
    setExtendedState(MAXIMIZED_BOTH);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    table = new Table("src/main/images/table.png");
    add(table);
    table.addCircle(new Circle(70, 64, 46, Color.black));

    addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
        cueStrike();
      }

      @Override
      public void keyPressed(KeyEvent e) {

      }

      @Override
      public void keyReleased(KeyEvent e) {

      }
    });
  }

  private void cueStrike() {
    Circle circle = new Circle(70, 900, 500, Color.white);
    table.addCircle(circle);
  }

  @Override
  public void attachPresenter(BilliardsPresenter presenter) {
    this.presenter = presenter;
  }
}
