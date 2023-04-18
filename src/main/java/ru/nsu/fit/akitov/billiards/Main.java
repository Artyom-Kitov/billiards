package ru.nsu.fit.akitov.billiards;

import ru.nsu.fit.akitov.billiards.model.Field;
import ru.nsu.fit.akitov.billiards.presenter.BilliardsPresenter;
import ru.nsu.fit.akitov.billiards.utils.GameProperties;
import ru.nsu.fit.akitov.billiards.view.BilliardsFrame;
import ru.nsu.fit.akitov.billiards.view.BilliardsView;

import java.io.IOException;

public class Main {

  public static void main(String[] args) {
    GameProperties props;
    try {
      props = GameProperties.readFromConfig();
    } catch (IOException e) {
      System.err.println(e.getMessage());
      return;
    }

    Field field = new Field(props);
    BilliardsView view = new BilliardsFrame(props);
    BilliardsPresenter presenter = new BilliardsPresenter(field, view);
    presenter.run();
  }
}
