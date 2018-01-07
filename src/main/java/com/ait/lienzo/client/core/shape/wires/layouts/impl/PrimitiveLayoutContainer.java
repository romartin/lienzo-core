package com.ait.lienzo.client.core.shape.wires.layouts.impl;

import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.types.BoundingBox;

public class PrimitiveLayoutContainer
        extends DelegateLayoutContainer<PrimitiveLayoutContainer> {

    private final LayoutEntriesContainer<PrimitiveLayoutEntry<PrimitiveLayoutContainer>> entriesContainer;

    public PrimitiveLayoutContainer() {
        this.entriesContainer = new LayoutEntriesContainer<>(new LayoutEntriesContainer.LayoutEntryRefreshExecutor<PrimitiveLayoutEntry<PrimitiveLayoutContainer>>() {
            @Override
            public void refresh(final PrimitiveLayoutEntry<PrimitiveLayoutContainer> entry) {
                entry.refresh(PrimitiveLayoutContainer.this);
                ;
            }
        });
    }

    public PrimitiveLayoutContainer add(final PrimitiveLayoutEntry<PrimitiveLayoutContainer> entry) {
        getDelegate().add(entry);
        return this;
    }

    BoundingBox getBoundingBox() {
        return getDelegate().getBoundingBox();
    }

    @Override
    public PrimitiveLayoutContainer remove(final IPrimitive<?> child) {
        entriesContainer.remove(child);
        return this;
    }

    @Override
    protected LayoutEntriesContainer<PrimitiveLayoutEntry<PrimitiveLayoutContainer>> getDelegate() {
        return entriesContainer;
    }
}
