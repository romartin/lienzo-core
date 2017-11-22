package com.ait.lienzo.client.core.shape.wires.layouts;

import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.client.core.types.Point2D;

public abstract class AbstractChildEntry
{
    private final String uuid;

    private Point2D initial_coords;

    private Double initial_width;

    private Double initial_height;

    private final IPrimitive<?> child;


    protected AbstractChildEntry(final IPrimitive<?> child)
    {
        this.child = child;
        this.uuid = child.getID();
        this.initial_coords = null;

        this.initial_width = null;
        this.initial_height = null;
    }


    public String getUUID()
    {
        return uuid;
    }


    public double[] getInitialCoordinates()
    {
        if (!isReady())
        {
            initializeChild();
        }

        return new double[] { initial_coords.getX(), initial_coords.getY() };

    }

    public double getInitialWidth()
    {
        if (!isReady())
        {
            initializeChild();
        }

        return initial_width;
    }

    public double getInitialHeight()
    {
        if (!isReady())
        {
            initializeChild();
        }

        return initial_height;
    }


    private void initializeChild()
    {
        final BoundingBox bb = child.getBoundingBox();

        final double[] c = getChildRelativeCoordinates(bb);

        initial_width = bb.getWidth();
        initial_height = bb.getHeight();

        initial_coords = new Point2D(c[0], c[1]);
    }

    private double[] getChildRelativeCoordinates(final BoundingBox bb)
    {
        final double bbx = bb.getX();
        final double bby = bb.getY();
        final double bbw = bb.getWidth();
        final double bbh = bb.getHeight();

        final double x = -bbx - (bbw / 2);
        final double y = -bby - (bbh / 2);

        return new double[] { x, y };
    }


    public void refresh()
    {
        this.initial_coords = null;
        this.initial_width = null;
        this.initial_height = null;
    }

    public boolean isReady()
    {
        return null != initial_coords && null != initial_width && null != initial_height;
    }

    public boolean equals(final Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof AbstractChildEntry))
        {
            return false;
        }

        AbstractChildEntry that = (AbstractChildEntry) o;

        return uuid.equals(that.uuid);
    }


}
