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
      props = GameProperties.readFromFile();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    Field field = new Field(props.fieldSize() * 2, props.fieldSize());
    BilliardsView view = new BilliardsFrame();
    BilliardsPresenter presenter = new BilliardsPresenter(field, view);
    presenter.run();
  }
}
