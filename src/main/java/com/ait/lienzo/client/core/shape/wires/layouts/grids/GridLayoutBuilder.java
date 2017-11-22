package com.ait.lienzo.client.core.shape.wires.layouts.grids;

import com.ait.lienzo.client.core.shape.wires.layouts.ILayoutBuilder;

public final class GridLayoutBuilder implements ILayoutBuilder<GridChildEntry, WiresGridLayoutContainer>
{
    @Override
    public double[] getCoordinates(GridChildEntry entry, WiresGridLayoutContainer container)
    {
        final int column = entry.getColumn();
        final int row = entry.getRow();

        if ((column == -1) || (row == -1))
        {
            return  null;
        }

        final Grid grid = container.getLayout();

        final double cellWidth = container.getWidth() / grid.getColumns();
        final double cellHeight = container.getHeight() / grid.getRows();

        final double x = (cellWidth * column) + (cellWidth / 2);
        final double y = (cellHeight  * row) + (cellHeight / 2);

        return new double[] { x, y };
    }
}
