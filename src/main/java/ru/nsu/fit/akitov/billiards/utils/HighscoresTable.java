package ru.nsu.fit.akitov.billiards.utils;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class HighscoresTable {

  private final Set<Highscore> HIGHSCORES = new TreeSet<>();
  public static final HighscoresTable INSTANCE = new HighscoresTable();

  private HighscoresTable() {
    readFromConfig();
  }

  private void readFromConfig() {
    Set<Highscore> highscores = new TreeSet<>();
    try (InputStream stream = HighscoresTable.class.getResourceAsStream("/highscores")) {
      List<String> lines = new BufferedReader(new InputStreamReader(Objects.requireNonNull(stream)))
              .lines()
              .toList();
      for (String line : lines) {
        String[] args = line.split(" ");
        Highscore highscore = new Highscore(args[0], Integer.parseUnsignedInt(args[1]), Integer.parseUnsignedInt(args[2]));
        highscores.add(highscore);
      }
      HIGHSCORES.clear();
      HIGHSCORES.addAll(highscores);
    } catch (IndexOutOfBoundsException | IOException | NumberFormatException e) {
      BilliardsLogger.INSTANCE.error("Couldn't read highscores from the file");
    }
  }

  private void writeToConfig() {
    try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(HighscoresTable.class.getResource("/highscores").toURI()))) {
      for (Highscore highscore : HIGHSCORES) {
        writer.write(highscore.toString() + "\n");
      }
    } catch (IOException | URISyntaxException e) {
      BilliardsLogger.INSTANCE.error("Couldn't write highscores to the file");
    }
  }

  public List<Highscore> getHighscores() {
    return HIGHSCORES.stream().toList();
  }

  public void addHighscore(Highscore highscore) {
    HIGHSCORES.add(highscore);
    writeToConfig();
  }
}
