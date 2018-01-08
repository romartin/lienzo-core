package com.ait.lienzo.client.core.shape.wires.layouts.impl;

import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.shape.wires.layouts.ILayoutContainer;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.tooling.common.api.java.util.function.Supplier;

public class GridLayoutContainerEntry extends GridLayoutEntry {

    private final ILayoutContainer<?> layoutContainer;

    public GridLayoutContainerEntry(final ILayoutContainer<?> layoutContainer,
                                    int row,
                                    int column) {
        super((IPrimitive<?>) layoutContainer.get(),
              row,
              column);
        this.layoutContainer = layoutContainer;
    }

    @Override
    public void refresh(final GridLayoutContainer container) {
        final double[] cellSize = container.getCellSize();
        layoutContainer
                .forBoundingBox(new Supplier<BoundingBox>() {
                    @Override
                    public BoundingBox get() {
                        BoundingBox bb = new BoundingBox(0d,
                                                         0d,
                                                         cellSize[0],
                                                         cellSize[1]);
                        // GWT.log("INNER CONTAINER FOR BB = [" + bb + "]");
                        return bb;
                    }
                }).refresh();
        super.refresh(container);
    }
}
