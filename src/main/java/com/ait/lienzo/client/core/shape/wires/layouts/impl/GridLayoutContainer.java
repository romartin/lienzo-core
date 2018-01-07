package com.ait.lienzo.client.core.shape.wires.layouts.impl;

import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.shape.wires.layouts.ILayoutContainer;
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

    public GridLayoutContainer add(final ILayoutContainer<?> container,
                                   final int row,
                                   final int column) {
        getDelegate().add(new GridLayoutContainerEntry(container,
                                                       row,
                                                       column));
        return this;
    }

    public GridLayoutContainer add(final IPrimitive<?> child,
                                   final int row,
                                   final int column) {
        getDelegate().add(new GridLayoutEntry(child,
                                              row,
                                              column));
        return this;
    }

    public GridLayoutContainer set(final IPrimitive<?> child,
                                   final int row,
                                   final int column) {
        final GridLayoutEntry entry = getEntry(child);
        return set(entry,
                   row,
                   column);
    }

    public GridLayoutContainer set(final ILayoutContainer<?> layoutContainer,
                                   final int row,
                                   final int column) {
        final GridLayoutContainerEntry entry = getContainerEntry(layoutContainer);
        return set(entry,
                   row,
                   column);
    }

    private GridLayoutContainer set(final GridLayoutEntry entry,
                                    final int row,
                                    final int column) {
        entry.row(row);
        entry.column(column);
        entry.refresh(this);
        return this;
    }

    GridLayoutEntry getEntry(final IPrimitive<?> child) {
        return getDelegate().getEntry(child);
    }

    GridLayoutContainerEntry getContainerEntry(final ILayoutContainer<?> layoutContainer) {
        return (GridLayoutContainerEntry) getEntry((IPrimitive<?>) layoutContainer.get());
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
