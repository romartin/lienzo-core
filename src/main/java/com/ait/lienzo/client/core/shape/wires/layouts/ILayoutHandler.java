package com.ait.lienzo.client.core.shape.wires.layouts;

import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.types.Point2D;

public interface ILayoutHandler<C extends ILayoutContainer<?>> {

    public ILayoutHandler addAt(IPrimitive<?> shape,
                                Point2D location);

    public ILayoutHandler setLocation(IPrimitive<?> shape,
                                      Point2D location);

    public ILayoutHandler remove(IPrimitive<?> shape);

    C getLayoutContainer();
}
