package com.ait.lienzo.client.core.shape.wires.layouts.grids;

import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.shape.wires.layouts.AbstractChildEntry;

class GridChildEntry extends AbstractChildEntry
{
    private final int column;

    private final int row;

    protected GridChildEntry(final IPrimitive<?> child, final int column, final int row)
    {
        super(child);
        this.column = column;
        this.row = row;
    }

    protected int getColumn()
    {
        return column;
    }

    protected int getRow()
    {
        return row;
    }
}
