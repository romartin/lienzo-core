package com.ait.lienzo.client.core.shape.wires.layouts.impl;

import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.IContainer;
import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.shape.wires.layouts.ILayoutContainer;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.tooling.common.api.java.util.UUID;
import com.ait.tooling.common.api.java.util.function.Supplier;
import com.ait.tooling.nativetools.client.collection.NFastStringMap;

public class LayoutEntriesContainer<E extends LayoutEntry>
        implements ILayoutContainer<LayoutEntriesContainer> {

    public interface LayoutEntryRefreshExecutor<E extends LayoutEntry> {

        public void refresh(E entry);
    }

    private final LayoutEntryRefreshExecutor<E> refreshExecutor;
    private final IContainer<?, IPrimitive<?>> container;
    private final NFastStringMap<E> entries;
    private Supplier<BoundingBox> bbSupplier;

    public LayoutEntriesContainer(final LayoutEntryRefreshExecutor<E> refreshExecutor) {
        this(refreshExecutor,
             new Group()
                     .setDraggable(false));
    }

    public LayoutEntriesContainer(final LayoutEntryRefreshExecutor<E> refreshExecutor,
                                  final IContainer<?, IPrimitive<?>> container) {
        this.container = container;
        this.refreshExecutor = refreshExecutor;
        this.entries = new NFastStringMap<>();
    }

    @Override
    public LayoutEntriesContainer forBoundingBox(final Supplier<BoundingBox> supplier) {
        this.bbSupplier = supplier;
        return this;
    }

    LayoutEntriesContainer add(final E entry) {
        if (null == entry.getPrimitive().getID()) {
            entry.getPrimitive().setID(UUID.uuid());
        }
        entries.put(entry.getPrimitive().getID(),
                    entry);
        getContainer().add(entry.getPrimitive());
        refresh(entry);
        return this;
    }

    LayoutEntriesContainer remove(final E entry) {
        final String id = entry.getPrimitive().getID();
        entry.getPrimitive().removeFromParent();
        entries.remove(id);
        return this;
    }

    LayoutEntriesContainer remove(final IPrimitive<?> child) {
        final E entry = getEntry(child);
        if (null != entry) {
            remove(entry);
        } else {
            getContainer().remove(child);
        }
        return this;
    }

    E getEntry(final IPrimitive<?> child) {
        return entries.get(child.getID());
    }

    @Override
    public LayoutEntriesContainer refresh() {
        for (E entry : entries.values()) {
            refresh(entry);
        }
        return this;
    }

    private void refresh(final E entry) {
        refreshExecutor.refresh(entry);
    }

    @Override
    public IContainer<?, IPrimitive<?>> getContainer() {
        return container;
    }

    BoundingBox getBoundingBox() {
        return bbSupplier.get();
    }
}
