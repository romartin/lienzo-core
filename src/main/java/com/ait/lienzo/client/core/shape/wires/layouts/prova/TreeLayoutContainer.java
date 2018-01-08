package com.ait.lienzo.client.core.shape.wires.layouts.prova;

import com.ait.tooling.common.api.java.util.function.Consumer;

// TODO: CLASS
public interface TreeLayoutContainer<C extends TreeLayoutContainer, E extends LayoutEntry<C>>
        extends EntriesLayoutContainer<C, E> {

    C append(ILayoutContainer<?> container,
             Consumer<E> entry);

    ILayoutContainer<?> getParent();
}
