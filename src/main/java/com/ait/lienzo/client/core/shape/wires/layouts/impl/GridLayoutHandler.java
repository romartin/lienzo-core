package com.ait.lienzo.client.core.shape.wires.layouts.impl;

import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.layouts.ILayoutHandler;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.tooling.common.api.java.util.function.Supplier;

public class GridLayoutHandler extends AbstractWiresLayoutHandler<GridLayoutContainer> {

    private final GridLayoutContainer container;

    public GridLayoutHandler(final Supplier<WiresShape> shapeSupplier,
                             final int rows,
                             final int columns) {
        super(shapeSupplier);
        this.container = new GridLayoutContainer(rows,
                                                 columns);
        container.forBoundingBox(boundingBoxSupplier);
    }

    @Override
    public ILayoutHandler addAt(final IPrimitive<?> shape,
                                final Point2D location) {
        final int[] gridIndex = getGridIndex(getLayoutContainer(),
                                             location);
        getLayoutContainer().add(shape,
                                 gridIndex[0],
                                 gridIndex[1]);
        return this;
    }

    @Override
    public ILayoutHandler setLocation(final IPrimitive<?> shape,
                                      final Point2D location) {
        final int[] gridIndex = getGridIndex(getLayoutContainer(),
                                             location);
        getLayoutContainer().set(shape,
                                 gridIndex[0],
                                 gridIndex[1]);
        return this;
    }

    @Override
    public ILayoutHandler remove(final IPrimitive<?> shape) {
        getLayoutContainer().remove(shape);
        return this;
    }

    @Override
    public GridLayoutContainer getLayoutContainer() {
        return container;
    }

    private static int[] getGridIndex(final GridLayoutContainer layoutContainer,
                                      final Point2D location) {
        final double[] cellSize = layoutContainer.getCellSize();
        final double cx = location.getX() / cellSize[0];
        final double cy = location.getY() / cellSize[1];
        return new int[]{(int) cx, (int) cy};
    }
}
