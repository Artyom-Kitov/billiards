package ru.nsu.fit.akitov.billiards.utils;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class BilliardsLogger {

  private BilliardsLogger() {
  }

  public static void error(String msg) {
    log.error(msg);
  }
}
