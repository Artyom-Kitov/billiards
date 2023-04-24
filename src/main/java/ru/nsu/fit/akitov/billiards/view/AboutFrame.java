package ru.nsu.fit.akitov.billiards.view;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AboutFrame extends JFrame {

  public static final AboutFrame INSTANCE = new AboutFrame();

  private final Image background;
  private final List<String> lines;

  private AboutFrame() {
    background = Toolkit.getDefaultToolkit().getImage(AboutFrame.class.getResource("/about.png"));
    this.setIconImage(Toolkit.getDefaultToolkit().getImage(AboutFrame.class.getResource("/logo.png")));

    List<String> lines;
    try (InputStream resource = AboutFrame.class.getResourceAsStream("/about.txt")) {
      lines = new BufferedReader(new InputStreamReader(Objects.requireNonNull(resource), StandardCharsets.UTF_8))
              .lines()
              .toList();
    } catch (IOException e) {
      lines = new ArrayList<>();
    }
    this.lines = lines;

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
    g.setColor(Color.black);
    for (int i = 0; i < lines.size(); i++) {
      g.drawString(lines.get(i), 0,  100 + 20 * i);
    }
  }
}
