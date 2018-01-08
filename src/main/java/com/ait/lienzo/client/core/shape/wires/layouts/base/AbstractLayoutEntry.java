package com.ait.lienzo.client.core.shape.wires.layouts.base;

import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.shape.wires.layouts.ILayoutContainer;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.client.core.types.Point2D;

public abstract class AbstractLayoutEntry<C extends ILayoutContainer<?>>
        implements LayoutEntry<C> {

    private final IPrimitive<?> primitive;
    private final BoundingBox boundingBox;

    public AbstractLayoutEntry(final IPrimitive<?> primitive) {
        this.primitive = primitive;
        this.boundingBox = primitive.getBoundingBox();
    }

    @Override
    public IPrimitive<?> getPrimitive() {
        return primitive;
    }

    public BoundingBox getPrimitiveBoundingBox() {
        return boundingBox;
    }

    protected abstract Point2D calculateLocation(C layoutContainer);

    @Override
    public void refresh(final C layoutContainer) {
        final Point2D location = calculateLocation(layoutContainer);
        final BoundingBox primBoundingBox = getPrimitiveBoundingBox();
        final double cx = primBoundingBox.getX() + (primBoundingBox.getWidth() / 2);
        final double cy = primBoundingBox.getY() + (primBoundingBox.getHeight() / 2);
        getPrimitive().setLocation(location.minus(cx,
                                                  cy));
    }
}
