package com.ait.lienzo.client.core.shape.wires.layouts.prova;

public interface LayoutFactory {

    StaticLayoutContainer position();

    DirectionalLayoutContainer directional();

    GridLayoutContainer grid();

    CardinalLayoutContainer cardinal();
}
