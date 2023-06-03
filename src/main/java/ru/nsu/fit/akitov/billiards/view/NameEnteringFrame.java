package ru.nsu.fit.akitov.billiards.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class NameEnteringFrame extends JFrame {
  private static final int WIDTH = 512;
  private static final int HEIGHT = 150;

  private final JTextField textField;

  // CR: only one
  private final List<NameEnterObserver> observers;

  public NameEnteringFrame() {
    JLabel label = new JLabel("The game is over! Enter your name:");
    this.add(label);

    textField = new JTextField("Player");
    textField.setPreferredSize(new Dimension(WIDTH, 30));
    this.add(textField);

    JButton button = new JButton("Confirm");
    button.setPreferredSize(new Dimension(100, 40));
    button.setBackground(Color.LIGHT_GRAY);
    button.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        String name = textField.getText();
        if (name.isEmpty() || name.contains(" ")) {
          return;
        }
        for (NameEnterObserver observer : observers) {
          observer.nameEntered(name);
        }
      }
    });
    this.add(button);

    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

    observers = new ArrayList<>();

    SpringLayout layout = new SpringLayout();
    layout.putConstraint(SpringLayout.WEST, label, 0, SpringLayout.WEST, getContentPane());
    layout.putConstraint(SpringLayout.NORTH, textField, 0, SpringLayout.SOUTH, label);
    layout.putConstraint(SpringLayout.NORTH, button, 20, SpringLayout.SOUTH, textField);

    this.setLocation(400, 400);
    this.setLayout(layout);
    this.getContentPane().setBackground(Color.GRAY);
    this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
    this.pack();
    this.setResizable(false);
    this.setVisible(false);
  }

  public void addObserver(NameEnterObserver observer) {
    observers.add(observer);
  }
}
