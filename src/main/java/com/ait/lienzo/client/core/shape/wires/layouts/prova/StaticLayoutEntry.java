package com.ait.lienzo.client.core.shape.wires.layouts.prova;

import com.ait.lienzo.client.core.shape.wires.layouts.impl.CardinalLayoutContainer;

public interface StaticLayoutEntry
        extends LayoutEntry<StaticLayoutContainer> {

    StaticLayoutEntry at(CardinalLayoutContainer.Cardinal cardinal);
}
