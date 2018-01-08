package com.ait.lienzo.client.core.shape.wires.layouts.prova;

public interface CardinalLayoutEntry extends LayoutEntry<CardinalLayoutContainer> {

    CardinalLayoutEntry at(com.ait.lienzo.client.core.shape.wires.layouts.impl.CardinalLayoutContainer.Cardinal cardinal);

    GridLayoutEntry entry();
}
