package ru.nsu.fit.akitov.billiards.dto;

import ru.nsu.fit.akitov.billiards.utils.Point2D;

public record BallModel(Point2D position, int radius, boolean isAvailable) {
}
