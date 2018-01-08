package com.ait.lienzo.client.core.shape.wires.layouts.base;

import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.shape.wires.layouts.ILayoutContainer;

public interface LayoutEntry<C extends ILayoutContainer> {

    IPrimitive<?> getPrimitive();

    void refresh(C layoutContainer);
}
