package ru.nsu.fit.akitov.billiards.utils;

// CR: move to package dto (for all dto models)
public record BallModel(Point2D position, int radius, boolean isAvailable) {
}
