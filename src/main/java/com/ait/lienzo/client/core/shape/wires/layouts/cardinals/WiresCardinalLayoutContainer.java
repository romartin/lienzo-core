/*
   Copyright (c) 2017 Ahome' Innovation Technologies. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.ait.lienzo.client.core.shape.wires.layouts.cardinals;

import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.shape.wires.layouts.ILayoutContainer;
import com.ait.lienzo.client.core.shape.wires.layouts.grids.Grid;
import com.ait.lienzo.client.core.shape.wires.layouts.grids.WiresGridLayoutContainer;
import com.ait.lienzo.client.core.types.Point2D;

public class WiresCardinalLayoutContainer implements ILayoutContainer<Grid>
{
    public enum Cardinals
    {
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

    private final WiresGridLayoutContainer gridLayoutContainer;

    public WiresCardinalLayoutContainer()
    {
        this.gridLayoutContainer = new WiresGridLayoutContainer(3, 3);
    }

    @Override
    public WiresCardinalLayoutContainer setOffset(final Point2D offset)
    {
        gridLayoutContainer.setOffset(offset);
        return this;
    }

    @Override
    public WiresCardinalLayoutContainer setSize(final double width, final double height)
    {
        gridLayoutContainer.setSize(width, height);
        return this;
    }

    public double getWidth()
    {
        return gridLayoutContainer.getWidth();
    }

    public double getHeight()
    {
        return gridLayoutContainer.getHeight();
    }

    public Point2D getOffset() {
        return gridLayoutContainer.getOffset();
    }

    @Override
    public Grid getLayout()
    {
        return gridLayoutContainer.getLayout();
    }

    public WiresCardinalLayoutContainer add(final IPrimitive<?> child)
    {
        gridLayoutContainer.add(child);
        return this;
    }

    public WiresCardinalLayoutContainer add(final IPrimitive<?> child, final Cardinals cardinal)
    {
        int column = 0;
        int row = 0;

        switch (cardinal)
        {
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

        gridLayoutContainer.add(child, column, row);

        return this;
    }

    public void addChild(final IPrimitive<?> child)
    {
        gridLayoutContainer.addChild(child);
    }

    public WiresCardinalLayoutContainer remove(final IPrimitive<?> child)
    {
        gridLayoutContainer.remove(child);

        return this;
    }

    public WiresCardinalLayoutContainer execute()
    {
        gridLayoutContainer.execute();

        if (null != getGroup().getLayer())
        {

            getGroup().getLayer().batch();

        }

        return this;
    }

    public WiresCardinalLayoutContainer refresh()
    {
        gridLayoutContainer.refresh();

        return this;
    }

    @Override
    public void destroy()
    {
        gridLayoutContainer.destroy();
    }

    public Group getGroup()
    {
        return gridLayoutContainer.getGroup();
    }
}
