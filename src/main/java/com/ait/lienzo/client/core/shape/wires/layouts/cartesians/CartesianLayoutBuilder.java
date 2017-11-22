package com.ait.lienzo.client.core.shape.wires.layouts.cartesians;

import com.ait.lienzo.client.core.shape.wires.layouts.ILayoutBuilder;
import com.ait.lienzo.client.core.types.Point2D;

public final class CartesianLayoutBuilder implements ILayoutBuilder<CartesianChildEntry, WiresCartesianLayoutContainer>
{
    @Override
    public double[] getCoordinates(CartesianChildEntry entry, WiresCartesianLayoutContainer container)
    {
        Point2D position = entry.getPosition();

        if (position == null)
        {
            return null;
        }

        final double bbw = entry.getInitialWidth();
        final double bbh = entry.getInitialHeight();

        final double x = (bbw / 2) + position.getX();
        final double y = (bbh / 2) + position.getY();

        return new double[] { x, y };
    }
}
