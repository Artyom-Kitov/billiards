package ru.nsu.fit.akitov.billiards.view;

import ru.nsu.fit.akitov.billiards.utils.Highscore;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class HighscoresFrame extends JFrame {

  private final List<Highscore> highscores;
  public HighscoresFrame() {
    highscores = new ArrayList<>();
  }

  public void updateHighscores(List<Highscore> highscores) {
    this.highscores.clear();
    this.highscores.addAll(highscores);
  }
}
