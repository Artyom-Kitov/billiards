package ru.nsu.fit.akitov.billiards;

import ru.nsu.fit.akitov.billiards.model.Field;
import ru.nsu.fit.akitov.billiards.presenter.BilliardsPresenter;
import ru.nsu.fit.akitov.billiards.view.BilliardsFrame;
import ru.nsu.fit.akitov.billiards.view.BilliardsView;

public class Main {
  private static final int SIZE_Y = 900;

  public static void main(String[] args) {
    Field field = new Field(2 * SIZE_Y, SIZE_Y);
    BilliardsView view = new BilliardsFrame(SIZE_Y);
    BilliardsPresenter presenter = new BilliardsPresenter(field, view);
    presenter.run();
  }
}
