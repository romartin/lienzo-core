package com.ait.lienzo.client.core.shape.wires.layouts.prova;

public interface GridLayoutEntry
        extends LayoutEntry<GridLayoutContainer> {

    GridLayoutEntry row(int value);

    GridLayoutEntry column(int value);
}
