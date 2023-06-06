package ru.nsu.fit.akitov.billiards.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BilliardsLogger {

  public static final BilliardsLogger INSTANCE = new BilliardsLogger();

  private final Logger logger;

  private BilliardsLogger() {
    logger = LogManager.getLogger("ru.nsu.fit.akitov.billiards.utils.BilliardsLogger");
  }

  public synchronized void error(String msg) {
    logger.error(msg);
  }

  public synchronized void warn(String msg) {
    logger.warn(msg);
  }
}
