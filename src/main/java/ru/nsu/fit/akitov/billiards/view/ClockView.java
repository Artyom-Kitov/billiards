package ru.nsu.fit.akitov.billiards.view;

import ru.nsu.fit.akitov.billiards.utils.ClockTime;

import javax.swing.*;
import java.awt.*;

public class ClockView extends JLabel {

  public ClockView(int height) {
    this.setSize(new Dimension(3 * height, height));
    this.setText("0:0");
    this.setForeground(Color.black);
    this.setFont(new Font("Serif", Font.PLAIN, height));
  }

  public void setTime(ClockTime time) {
    this.setText(time.toString());
  }
}
