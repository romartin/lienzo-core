package com.ait.lienzo.client.core.shape.wires.layouts.impl;

import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.client.core.types.Point2D;

//  TODO: Listen for and handle shape's transforming attributes changed.

public class CartesianLayoutContainer
        extends AbstractLayoutContainer<CartesianLayoutContainer> {

    private final LayoutEntriesContainer<CartesianLayoutEntry> layoutContainer;

    protected CartesianLayoutContainer() {
        this.layoutContainer = new LayoutEntriesContainer<>(new LayoutEntriesContainer.LayoutEntryRefreshExecutor<CartesianLayoutEntry>() {
            @Override
            public void refresh(final CartesianLayoutEntry entry) {
                entry.refresh(CartesianLayoutContainer.this);
            }
        });
    }

    public CartesianLayoutContainer add(final IPrimitive<?> child,
                                        final Point2D location) {
        getDelegate().add(new CartesianLayoutEntry(child, location));
        return this;
    }

    public CartesianLayoutContainer setLocation(final IPrimitive<?> child,
                                                final Point2D location) {
        final CartesianLayoutEntry entry = getDelegate().getEntry(child);
        entry.setLocation(location);
        entry.refresh(this);
        return this;
    }

    @Override
    public CartesianLayoutContainer remove(final IPrimitive<?> child) {
        getDelegate().remove(child);
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
