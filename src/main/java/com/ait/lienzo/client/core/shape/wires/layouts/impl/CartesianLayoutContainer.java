package com.ait.lienzo.client.core.shape.wires.layouts.impl;

import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.IContainer;
import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.shape.wires.layouts.base.DelegateLayoutContainer;
import com.ait.lienzo.client.core.shape.wires.layouts.base.LayoutEntriesContainer;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.tooling.common.api.java.util.function.Consumer;

//  TODO: Listen for and handle shape's transforming attributes changed.

public class CartesianLayoutContainer
        extends DelegateLayoutContainer<CartesianLayoutContainer> {

    private final LayoutEntriesContainer<CartesianLayoutEntry> layoutContainer;

    public CartesianLayoutContainer() {
        this(new Group());
    }

    public CartesianLayoutContainer(final IContainer<?, IPrimitive<?>> container) {
        this.layoutContainer = new LayoutEntriesContainer<>(refreshExecutor,
                                                            container);
    }

    private final Consumer<CartesianLayoutEntry> refreshExecutor =
            new Consumer<CartesianLayoutEntry>() {
                @Override
                public void accept(final CartesianLayoutEntry entry) {
                    entry.refresh(CartesianLayoutContainer.this);
                }
            };

    public CartesianLayoutContainer add(final IPrimitive<?> child,
                                        final Point2D location) {
        getDelegate().add(new CartesianLayoutEntry(child,
                                                   location));
        return this;
    }

    public CartesianLayoutContainer setLocation(final IPrimitive<?> child,
                                                final Point2D location) {
        final CartesianLayoutEntry entry = getDelegate().getEntry(child);
        entry.setLocation(location);
        entry.refresh(this);
        return this;
    }

    BoundingBox getBoundingBox() {
        return getDelegate().getBoundingBox();
    }

    @Override
    protected LayoutEntriesContainer<CartesianLayoutEntry> getDelegate() {
        return layoutContainer;
    }
}
