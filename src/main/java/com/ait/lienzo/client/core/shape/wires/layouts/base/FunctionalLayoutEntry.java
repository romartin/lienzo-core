package com.ait.lienzo.client.core.shape.wires.layouts.base;

import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.tooling.common.api.java.util.function.Function;

public class FunctionalLayoutEntry
        extends AbstractLayoutEntry<FunctionalLayoutContainer> {

    private final Function<BoundingBox, Point2D> locationProvider;

    public FunctionalLayoutEntry(final IPrimitive<?> primitive,
                                 final Function<BoundingBox, Point2D> locationProvider) {
        super(primitive);
        this.locationProvider = locationProvider;
    }

    @Override
    protected Point2D calculateLocation(FunctionalLayoutContainer layoutContainer) {
        final BoundingBox boundingBox = layoutContainer.getBoundingBox();
        return locationProvider.apply(boundingBox);
    }
}
