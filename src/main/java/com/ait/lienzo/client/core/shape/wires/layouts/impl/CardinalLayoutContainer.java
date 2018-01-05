package com.ait.lienzo.client.core.shape.wires.layouts.impl;

import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.shape.wires.layouts.ILayoutContainer;

public class CardinalLayoutContainer
        extends DelegateLayoutContainer<CardinalLayoutContainer> {

    public enum Cardinal {
        CENTER,
        EAST,
        NORTH,
        NORTHEAST,
        NORTHWEST,
        SOUTH,
        SOUTHEAST,
        SOUTHWEST,
        WEST
    }

    private final GridLayoutContainer gridLayoutContainer;

    public CardinalLayoutContainer() {
        this.gridLayoutContainer = new GridLayoutContainer(3,
                                                           3);
    }

    public CardinalLayoutContainer add(final IPrimitive<?> child,
                                       final Cardinal cardinal) {
        final int[] gridLocation = getGridLocation(cardinal);
        getGridLayoutContainer().add(child,
                                     gridLocation[0],
                                     gridLocation[1]);
        return this;
    }

    public CardinalLayoutContainer set(final IPrimitive<?> child,
                                       final Cardinal cardinal) {
        final GridLayoutEntry entry = getGridLayoutContainer().getDelegate().getEntry(child);
        final int[] gridLocation = getGridLocation(cardinal);
        entry.row(gridLocation[0]);
        entry.column(gridLocation[1]);
        entry.refresh(getGridLayoutContainer());
        return this;
    }

    @Override
    public CardinalLayoutContainer remove(final IPrimitive<?> child) {
        getGridLayoutContainer().remove(child);
        return this;
    }

    @Override
    protected ILayoutContainer<?> getDelegate() {
        return getGridLayoutContainer();
    }

    GridLayoutContainer getGridLayoutContainer() {
        return gridLayoutContainer;
    }

    private static int[] getGridLocation(final Cardinal cardinal) {
        int column = 0;
        int row = 0;

        switch (cardinal) {
            case CENTER:
                column = 1;
                row = 1;
                break;
            case EAST:
                column = 2;
                row = 1;
                break;
            case NORTH:
                column = 1;
                row = 0;
                break;
            case NORTHEAST:
                column = 2;
                row = 0;
                break;
            case NORTHWEST:
                column = 0;
                row = 0;
                break;
            case SOUTH:
                column = 1;
                row = 2;
                break;
            case SOUTHEAST:
                column = 2;
                row = 2;
                break;
            case SOUTHWEST:
                column = 0;
                row = 2;
                break;
            case WEST:
                column = 0;
                row = 1;
                break;
        }

        return new int[]{row, column};
    }
}
