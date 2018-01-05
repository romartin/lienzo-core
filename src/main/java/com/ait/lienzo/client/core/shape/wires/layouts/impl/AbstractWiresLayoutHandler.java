package com.ait.lienzo.client.core.shape.wires.layouts.impl;

import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.layouts.ILayoutContainer;
import com.ait.lienzo.client.core.shape.wires.layouts.ILayoutHandler;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.tooling.common.api.java.util.function.Supplier;

public abstract class AbstractWiresLayoutHandler<C extends ILayoutContainer<?>> implements ILayoutHandler<C> {

    private final Supplier<WiresShape> shapeSupplier;

    @SuppressWarnings("unchecked")
    public AbstractWiresLayoutHandler(final Supplier<WiresShape> shapeSupplier) {
        this.shapeSupplier = shapeSupplier;
    }

    public ILayoutHandler addAt(final WiresShape child,
                                final Point2D location) {
        getShape().add(child);
        addAt(child.getGroup(),
              location);
        return this;
    }

    public ILayoutHandler setLocation(final WiresShape child,
                                      final Point2D location) {
        setLocation(child.getGroup(),
                    location);
        return this;
    }

    public ILayoutHandler remove(final WiresShape child) {
        getShape().remove(child);
        remove(child.getGroup());
        return this;
    }

    protected WiresShape getShape() {
        return shapeSupplier.get();
    }

    protected final Supplier<BoundingBox> boundingBoxSupplier = new Supplier<BoundingBox>() {
        @Override
        public BoundingBox get() {
            return getShape().getGroup().getBoundingBox();
        }
    };
}
