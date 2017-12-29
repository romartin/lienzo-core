package com.ait.lienzo.client.core.shape.wires.layouts.impl;

import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.shape.wires.layouts.ILayoutContainer;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.tooling.common.api.java.util.UUID;
import com.ait.tooling.nativetools.client.collection.NFastStringMap;

public class LayoutEntriesContainer<E extends LayoutEntry>
        implements ILayoutContainer<LayoutEntriesContainer> {

    public interface LayoutEntryRefreshExecutor<E extends LayoutEntry> {
        public void refresh(E entry);
    }

    private final LayoutEntryRefreshExecutor<E> refreshExecutor;
    private final Group group;
    private final NFastStringMap<E> entries;
    private ILayoutContainer.BoundingBoxSupplier bbSupplier;

    public LayoutEntriesContainer(final LayoutEntryRefreshExecutor<E> refreshExecutor) {
        this(refreshExecutor,
                new Group()
                        .setDraggable(false));
    }

    LayoutEntriesContainer(final LayoutEntryRefreshExecutor<E> refreshExecutor,
                           final Group group) {
        this.group = group;
        this.refreshExecutor = refreshExecutor;
        this.entries = new NFastStringMap<>();
    }

    @Override
    public LayoutEntriesContainer forBoundingBox(final BoundingBoxSupplier supplier) {
        this.bbSupplier = supplier;
        return this;
    }

    LayoutEntriesContainer add(final E entry) {
        if (null == entry.getPrimitive().getID()) {
            entry.getPrimitive().setID(UUID.uuid());
        }
        entries.put(entry.getPrimitive().getID(), entry);
        getGroup().add(entry.getPrimitive());
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
            asGroup().remove(child);
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
    public Group asGroup() {
        return getGroup();
    }

    BoundingBox getBoundingBox() {
        return bbSupplier.get();
    }

    Group getGroup() {
        return group;
    }

}
