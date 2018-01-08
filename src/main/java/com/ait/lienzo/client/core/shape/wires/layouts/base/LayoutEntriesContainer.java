package com.ait.lienzo.client.core.shape.wires.layouts.base;

import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.IContainer;
import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.shape.wires.layouts.ILayoutContainer;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.tooling.common.api.java.util.UUID;
import com.ait.tooling.common.api.java.util.function.Consumer;
import com.ait.tooling.common.api.java.util.function.Supplier;
import com.ait.tooling.nativetools.client.collection.NFastStringMap;

public class LayoutEntriesContainer<E extends LayoutEntry>
        implements ILayoutContainer<LayoutEntriesContainer> {

    private final Consumer<E> entryRefreshConsumer;
    private final IContainer<?, IPrimitive<?>> container;
    private final NFastStringMap<E> entries;
    private Supplier<BoundingBox> bbSupplier;

    public LayoutEntriesContainer(final Consumer<E> entryRefreshConsumer) {
        this(entryRefreshConsumer,
             new Group()
                     .setDraggable(false));
    }

    public LayoutEntriesContainer(final Consumer<E> entryRefreshConsumer,
                                  final IContainer<?, IPrimitive<?>> container) {
        this.container = container;
        this.entryRefreshConsumer = entryRefreshConsumer;
        this.entries = new NFastStringMap<>();
    }

    @Override
    public LayoutEntriesContainer forBoundingBox(final Supplier<BoundingBox> supplier) {
        this.bbSupplier = supplier;
        return this;
    }

    public LayoutEntriesContainer add(final E entry) {
        if (null == entry.getPrimitive().getID()) {
            entry.getPrimitive().setID(UUID.uuid());
        }
        entries.put(entry.getPrimitive().getID(),
                    entry);
        get().add(entry.getPrimitive());
        refresh(entry);
        return this;
    }

    public LayoutEntriesContainer remove(final E entry) {
        final String id = entry.getPrimitive().getID();
        entry.getPrimitive().removeFromParent();
        entries.remove(id);
        return this;
    }

    public LayoutEntriesContainer remove(final IPrimitive<?> child) {
        final E entry = getEntry(child);
        if (null != entry) {
            remove(entry);
        } else {
            get().remove(child);
        }
        return this;
    }

    public E getEntry(final IPrimitive<?> child) {
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
        if (null != bbSupplier) {
            entryRefreshConsumer.accept(entry);
        }
    }

    @Override
    public IContainer<?, IPrimitive<?>> get() {
        return container;
    }

    public BoundingBox getBoundingBox() {
        return bbSupplier.get();
    }
}
