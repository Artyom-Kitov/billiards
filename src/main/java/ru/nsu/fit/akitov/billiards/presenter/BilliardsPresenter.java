package ru.nsu.fit.akitov.billiards.presenter;

import ru.nsu.fit.akitov.billiards.model.Field;
import ru.nsu.fit.akitov.billiards.model.FieldListener;
import ru.nsu.fit.akitov.billiards.view.BilliardsView;

public class BilliardsPresenter implements Runnable, FieldListener {
  private final BilliardsView view;
  private final Field field;

  public BilliardsPresenter(Field field, BilliardsView view) {
    this.field = field;
    this.view = view;
  }

  @Override
  public void run() {
    view.attachPresenter(this);
  }
}
