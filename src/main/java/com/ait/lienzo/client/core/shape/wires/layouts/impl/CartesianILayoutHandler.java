package com.ait.lienzo.client.core.shape.wires.layouts.impl;

import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.layouts.ILayoutHandler;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.tooling.common.api.java.util.function.Supplier;

public class CartesianILayoutHandler extends AbstractWiresLayoutHandler<CartesianLayoutContainer> {

    private final CartesianLayoutContainer container;

    public CartesianILayoutHandler(final Supplier<WiresShape> shapeSupplier) {
        super(shapeSupplier);
        this.container = new CartesianLayoutContainer();
        container.forBoundingBox(boundingBoxSupplier);
    }

    @Override
    public ILayoutHandler addAt(final IPrimitive<?> shape,
                                final Point2D location) {
        getLayoutContainer().add(shape,
                                 location);
        return this;
    }

    @Override
    public ILayoutHandler setLocation(final IPrimitive<?> shape,
                                      final Point2D location) {
        getLayoutContainer().setLocation(shape,
                                         location);
        return this;
    }

    @Override
    public ILayoutHandler remove(final IPrimitive<?> shape) {
        getLayoutContainer().remove(shape);
        return this;
    }

    @Override
    public CartesianLayoutContainer getLayoutContainer() {
        return container;
    }
}
