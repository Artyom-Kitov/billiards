package ru.nsu.fit.akitov.billiards.view;

import ru.nsu.fit.akitov.billiards.utils.Highscore;
import ru.nsu.fit.akitov.billiards.utils.HighscoresTable;

import javax.swing.*;
import java.awt.*;

public class HighscoresFrame extends JFrame {

  private static final int WIDTH = 512;
  private static final int HEIGHT = 512;

  private static final String[] COLUMNS = {"Name", "Ball count", "Time (seconds)"};

  private final JPanel panel;

  public HighscoresFrame() {
    // CR: return highscore
    String[][] data = HighscoresTable.getTable();
    JTable table = new JTable(data, COLUMNS);
    table.setBounds(0, 0, 512, 400);

    panel = new JPanel(new BorderLayout());
    panel.setBounds(0, 0, WIDTH, HEIGHT);
    panel.add(new JScrollPane(table));

    this.add(panel);
    this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    this.setLayout(null);
    this.setResizable(false);
    this.pack();
    this.setVisible(false);
  }

  @Override
  public void setVisible(boolean b) {
    if (b) {
      JTable table = new JTable(HighscoresTable.getTable(), COLUMNS);
      panel.removeAll();
      panel.add(new JScrollPane(table));
    }
    super.setVisible(b);
  }

  public void addHighscore(Highscore highscore) {
    HighscoresTable.addHighscore(highscore);
  }
}
