package com.ait.lienzo.client.core.shape.wires.layouts.impl;

import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.types.BoundingBox;

public class GridLayoutContainer
        extends DelegateLayoutContainer<GridLayoutContainer> {

    private final LayoutEntriesContainer<GridLayoutEntry> layoutContainer;
    private final Grid grid;
    private double[] cellSize;

    public GridLayoutContainer(final int rows,
                               final int columns) {
        this.layoutContainer = new LayoutEntriesContainer<>(new LayoutEntriesContainer.LayoutEntryRefreshExecutor<GridLayoutEntry>() {
            @Override
            public void refresh(final GridLayoutEntry entry) {
                entry.refresh(GridLayoutContainer.this);
            }
        });
        this.grid = new Grid(columns,
                             rows);
        this.cellSize = null;
    }

    public GridLayoutContainer add(final IPrimitive<?> child,
                                   final int row,
                                   final int column) {
        getDelegate().add(new GridLayoutEntry(child,
                                              row,
                                              column));
        return this;
    }

    public GridLayoutContainer setRow(final IPrimitive<?> child,
                                      final int row) {
        final GridLayoutEntry entry = getDelegate().getEntry(child);
        entry.row(row);
        entry.refresh(this);
        return this;
    }

    public GridLayoutContainer setColumn(final IPrimitive<?> child,
                                         final int column) {
        final GridLayoutEntry entry = getDelegate().getEntry(child);
        entry.column(column);
        entry.refresh(this);
        return this;
    }

    public GridLayoutContainer set(final IPrimitive<?> child,
                                   final int row,
                                   final int column) {
        final GridLayoutEntry entry = getDelegate().getEntry(child);
        entry.row(row);
        entry.column(column);
        entry.refresh(this);
        return this;
    }

    @Override
    public GridLayoutContainer remove(final IPrimitive<?> child) {
        getDelegate().remove(child);
        return this;
    }

    @Override
    protected void onRefreshBounds() {
        super.onRefreshBounds();
        cellSize = null;
    }

    public Grid getGrid() {
        return grid;
    }

    BoundingBox getBoundingBox() {
        return getDelegate().getBoundingBox();
    }

    double[] getCellSize() {
        if (null == cellSize) {
            final BoundingBox bb = getBoundingBox();
            final Grid grid = getGrid();
            final double cellWidth = bb.getWidth() / grid.getColumns();
            final double cellHeight = bb.getHeight() / grid.getRows();
            cellSize = new double[]{cellWidth, cellHeight};
        }
        return cellSize;
    }

    @Override
    protected LayoutEntriesContainer<GridLayoutEntry> getDelegate() {
        return layoutContainer;
    }
}
