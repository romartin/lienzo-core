package com.ait.lienzo.client.core.shape.wires.layouts.impl;

import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.tooling.common.api.java.util.function.Function;

public class PrimitiveLayoutEntry<C extends PrimitiveLayoutContainer>
        implements LayoutEntry<C> {

    public static class Cardinals {

        public static PrimitiveLayoutEntry<PrimitiveLayoutContainer> center(final IPrimitive<?> primitive) {
            return new PrimitiveLayoutEntry<>(primitive,
                                              new Function<BoundingBox, Point2D>() {
                                                  @Override
                                                  public Point2D apply(BoundingBox boundingBox) {
                                                      return new Point2D(boundingBox.getWidth() / 2,
                                                                         boundingBox.getHeight() / 2);
                                                  }
                                              });
        }
    }

    private final IPrimitive<?> primitive;
    private final Function<BoundingBox, Point2D> locationProvider;

    public PrimitiveLayoutEntry(final IPrimitive<?> primitive,
                                final Function<BoundingBox, Point2D> locationProvider) {
        this.primitive = primitive;
        this.locationProvider = locationProvider;
    }

    @Override
    public IPrimitive<?> getPrimitive() {
        return primitive;
    }

    @Override
    public void refresh(final PrimitiveLayoutContainer layoutContainer) {
        final BoundingBox boundingBox = layoutContainer.getBoundingBox();
        getPrimitive().setLocation(locationProvider.apply(boundingBox));
    }
}
