package com.ait.lienzo.client.core.shape.wires.layouts.base;

import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.tooling.common.api.java.util.function.Consumer;

public class FunctionalLayoutContainer
        extends DelegateLayoutContainer<FunctionalLayoutContainer> {

    private final LayoutEntriesContainer<FunctionalLayoutEntry> entriesContainer;

    public FunctionalLayoutContainer() {
        this.entriesContainer = new LayoutEntriesContainer<>(new Consumer<FunctionalLayoutEntry>() {
            @Override
            public void accept(final FunctionalLayoutEntry entry) {
                entry.refresh(FunctionalLayoutContainer.this);
            }
        });
    }

    public FunctionalLayoutContainer add(final FunctionalLayoutEntry entry) {
        getDelegate().add(entry);
        return this;
    }

    BoundingBox getBoundingBox() {
        return getDelegate().getBoundingBox();
    }

    @Override
    protected LayoutEntriesContainer<FunctionalLayoutEntry> getDelegate() {
        return entriesContainer;
    }
}
