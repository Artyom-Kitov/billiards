package ru.nsu.fit.akitov.billiards.view;

import javax.swing.*;
import java.awt.*;

public class AboutFrame extends JFrame {

  public static final AboutFrame INSTANCE = new AboutFrame();

  private final Image background;
  private final String text;

  private AboutFrame() {
    background = Toolkit.getDefaultToolkit().getImage(
            Thread.currentThread().getContextClassLoader().getResource("about.png")
    );

    text = "To be done...";

    this.setPreferredSize(new Dimension(512, 480));
    this.setLayout(null);
    this.setResizable(false);
    this.pack();
    this.setVisible(true);
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
    g.drawImage(background, 0, 0, this);
    g.setFont(new Font(Font.SERIF, Font.PLAIN, 20));
    g.setColor(Color.white);
    g.drawString(text, 0, 60);
  }
}
