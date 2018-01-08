package com.ait.lienzo.client.core.shape.wires.layouts.prova;

import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.tooling.common.api.java.util.function.Consumer;

// TODO: CLASS
public interface EntriesLayoutContainer<C extends EntriesLayoutContainer, E extends LayoutEntry<C>>
        extends ILayoutContainer<C> {

    C add(IPrimitive<?> primitive,
          Consumer<E> entry);

    C consume(IPrimitive<?> primitive,
              Consumer<E> entry);

    C remove(IPrimitive<?> primitive);

}
