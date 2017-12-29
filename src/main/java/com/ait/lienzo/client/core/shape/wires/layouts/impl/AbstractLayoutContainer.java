package com.ait.lienzo.client.core.shape.wires.layouts.impl;

import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.shape.wires.layouts.ILayoutContainer;

public abstract class AbstractLayoutContainer<T extends AbstractLayoutContainer>
        implements ILayoutContainer<T> {

    public abstract T remove(IPrimitive<?> child);

    protected abstract ILayoutContainer<?> getDelegate();

    @Override
    public T forBoundingBox(final BoundingBoxSupplier supplier) {
        getDelegate().forBoundingBox(supplier);
        return cast();
    }

    public T add(final IPrimitive<?> primitive) {
        getDelegate().asGroup().add(primitive);
        return cast();
    }

    @Override
    public T refresh() {
        getDelegate().refresh();
        return cast();
    }

    @Override
    public Group asGroup() {
        return getDelegate().asGroup();
    }

    @SuppressWarnings("unchecked")
    private T cast() {
        return (T) this;
    }
}
