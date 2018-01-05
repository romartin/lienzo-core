package com.ait.lienzo.client.core.shape.wires.layouts.impl;

import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.client.core.types.Point2D;

//  TODO: Listen for and handle shape's transforming attributes changed.

public class CartesianLayoutEntry implements LayoutEntry<CartesianLayoutContainer> {

    private final IPrimitive<?> primitive;
    private final Point2D location;

    public CartesianLayoutEntry(final IPrimitive<?> primitive,
                                final Point2D location) {
        this.primitive = primitive;
        this.location = location;
    }

    public CartesianLayoutEntry setLocation(final Point2D point) {
        location.set(point);
        return this;
    }

    @Override
    public IPrimitive<?> getPrimitive() {
        return primitive;
    }

    @Override
    public void refresh(final CartesianLayoutContainer layoutContainer) {
        final BoundingBox boundingBox = layoutContainer.getBoundingBox();
        checkBounds(location,
                    boundingBox);
        primitive.setLocation(location);
    }

    private static void checkBounds(final Point2D location,
                                    final BoundingBox boundingBox) {
        final double x = boundingBox.getX();
        final double y = boundingBox.getX();
        final double width = boundingBox.getWidth();
        final double height = boundingBox.getHeight();
        final boolean valid = location.getX() >= x &&
                location.getX() <= x + width &&
                location.getY() >= y &&
                location.getY() <= y + height;
        if (!valid) {
            throw new IllegalArgumentException("The specified location [" + location + "] exceeds the parent " +
                                                       "bounding box area [" + boundingBox + "]");
        }
    }
}
