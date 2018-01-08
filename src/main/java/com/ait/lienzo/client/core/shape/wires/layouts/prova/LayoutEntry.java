package com.ait.lienzo.client.core.shape.wires.layouts.prova;

import com.ait.lienzo.client.core.shape.IPrimitive;

public interface LayoutEntry<C extends ILayoutContainer> {

    IPrimitive<?> getPrimitive();

    ILayoutContainer<?> asContainer();

    void refresh(C layoutContainer);
}
