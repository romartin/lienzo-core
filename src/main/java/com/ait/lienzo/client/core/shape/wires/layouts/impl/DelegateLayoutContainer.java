package com.ait.lienzo.client.core.shape.wires.layouts.impl;

import com.ait.lienzo.client.core.shape.IContainer;
import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.shape.wires.layouts.ILayoutContainer;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.tooling.common.api.java.util.function.Supplier;

public abstract class DelegateLayoutContainer<T extends DelegateLayoutContainer>
        implements ILayoutContainer<T> {

    public abstract T remove(IPrimitive<?> child);

    protected abstract ILayoutContainer<?> getDelegate();

    @Override
    public T forBoundingBox(final Supplier<BoundingBox> supplier) {
        onRefreshBounds();
        getDelegate().forBoundingBox(supplier);
        return cast();
    }

    public T add(final IPrimitive<?> primitive) {
        getDelegate().getContainer().add(primitive);
        return cast();
    }

    @Override
    public T refresh() {
        onRefreshBounds();
        getDelegate().refresh();
        return cast();
    }

    @Override
    public IContainer<?, IPrimitive<?>> getContainer() {
        return getDelegate().getContainer();
    }

    protected void onRefreshBounds() {
    }

    @SuppressWarnings("unchecked")
    private T cast() {
        return (T) this;
    }
}
