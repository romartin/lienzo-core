package com.ait.lienzo.client.core.shape.wires.layouts.cartesians;

import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.shape.wires.layouts.AbstractChildEntry;
import com.ait.lienzo.client.core.types.Point2D;

class CartesianChildEntry extends AbstractChildEntry
{
    private final Point2D position;


    protected CartesianChildEntry(final IPrimitive<?> child, final Point2D position)
    {
        super(child);

        this.position = position;
    }


    protected Point2D getPosition()
    {
        return position;
    }
}
