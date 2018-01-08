package com.ait.lienzo.client.core.shape.wires.layouts.prova;

// TODO: CLASS
public interface DirectionalLayoutContainer
        extends TreeLayoutContainer<DirectionalLayoutContainer, DirectionalLayoutEntry> {

    public enum Direction {
        HORIZONTAL,
        VERTICAL;
    }

    DirectionalLayoutContainer direction(Direction direction);

    // TODO: Package protected methods (as entry#refresh method could require access)
}
