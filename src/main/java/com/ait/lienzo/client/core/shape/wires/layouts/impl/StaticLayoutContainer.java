package com.ait.lienzo.client.core.shape.wires.layouts.impl;

import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.shape.wires.layouts.base.DelegateLayoutContainer;
import com.ait.lienzo.client.core.shape.wires.layouts.base.FunctionalLayoutContainer;
import com.ait.lienzo.client.core.shape.wires.layouts.base.FunctionalLayoutEntry;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.tooling.common.api.java.util.function.Function;

public class StaticLayoutContainer
        extends DelegateLayoutContainer<StaticLayoutContainer> {

    private final FunctionalLayoutContainer container;

    public StaticLayoutContainer() {
        this.container = new FunctionalLayoutContainer();
    }

    public StaticLayoutContainer addAtCenter(final IPrimitive<?> primitive) {
        getDelegate().add(center(primitive));
        return this;
    }

    @Override
    protected FunctionalLayoutContainer getDelegate() {
        return container;
    }

    private static FunctionalLayoutEntry center(final IPrimitive<?> primitive) {
        return new FunctionalLayoutEntry(primitive,
                                         new Function<BoundingBox, Point2D>() {
                                             @Override
                                             public Point2D apply(BoundingBox boundingBox) {
                                                 return new Point2D(boundingBox.getWidth() / 2,
                                                                    boundingBox.getHeight() / 2);
                                             }
                                         });
    }
}
