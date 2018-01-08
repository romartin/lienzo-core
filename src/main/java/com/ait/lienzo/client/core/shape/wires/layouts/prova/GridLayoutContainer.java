package com.ait.lienzo.client.core.shape.wires.layouts.prova;

import com.ait.lienzo.client.core.shape.wires.layouts.impl.Grid;

// TODO: CLASS
public interface GridLayoutContainer
        extends TreeLayoutContainer<GridLayoutContainer, GridLayoutEntry> {

    GridLayoutContainer grid(Grid grid);

    // TODO: Package protected methods
}
