package com.ait.lienzo.client.core.shape.wires.handlers.impl;

import com.ait.lienzo.client.core.shape.Group;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.wires.WiresContainer;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresLayoutControl;
import com.ait.lienzo.client.core.shape.wires.layouts.ILayoutContainer;
import com.ait.lienzo.client.core.shape.wires.layouts.ILayoutHandler;
import com.ait.lienzo.client.core.shape.wires.layouts.impl.AbstractWiresLayoutHandler;
import com.ait.lienzo.client.core.shape.wires.picker.ColorMapBackedPicker;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.tooling.common.api.java.util.function.Function;

public class WiresLayoutControlImpl
        extends AbstractWiresParentPickerControl
        implements WiresLayoutControl {

    private final Function<WiresShape, WiresShape> shapeGhostSupplier;
    private AbstractWiresLayoutHandler<ILayoutContainer<?>> layoutHandler;
    private WiresShape ghost;

    public WiresLayoutControlImpl(WiresShape shape,
                                  ColorMapBackedPicker.PickerOptions pickerOptions) {
        super(shape,
              pickerOptions);
        this.shapeGhostSupplier = new BoxShapeGhostSupplier();
    }

    public WiresLayoutControlImpl(WiresParentPickerControlImpl parentPickerControl) {
        super(parentPickerControl);
        this.shapeGhostSupplier = new BoxShapeGhostSupplier();
    }

    @Override
    protected void afterMoveStart(final double x,
                                  final double y) {
        super.afterMoveStart(x,
                             y);
        final WiresContainer _parent = getParent();
        if (_parent instanceof WiresShape) {

            final WiresShape shape = getShape();
            final WiresShape parent = (WiresShape) _parent;

            // Ghost.
            createGhost(parent,
                        shape,
                        shape.getLocation());

            // TODO: logicallyReplace(parent, shape, ghost);

            // TODO: Add this?? -> getLayoutHandler().requestLayout( parent );

        }
    }

    private AbstractWiresLayoutHandler getLayoutHandler(final WiresShape shape) {
        // TODO return (AbstractWiresLayoutHandler) shape.getLayoutHandler();
        return null;
    }

    @Override
    protected boolean afterMove(final double dx,
                                final double dy) {
        super.afterMove(dx,
                        dy);
        final WiresContainer _parent = getParent();
        if (_parent instanceof WiresShape) {
            final WiresShape shape = getShape();
            final WiresShape parent = (WiresShape) _parent;
            final Point2D location = getShapeLocation();
            final ILayoutHandler handler = getLayoutHandler(parent);
            if (null != layoutHandler && handler != layoutHandler) {
                removeCurrentGhost();
            }
            if (null == layoutHandler) {
                createGhost(parent,
                            shape,
                            location);
            } else {
                layoutHandler.setLocation(ghost,
                                          location);
            }
        }
        return false;
    }

    private void createGhost(final WiresShape parent,
                             final WiresShape shape,
                             final Point2D locatoin) {
        ghost = shapeGhostSupplier.apply(shape);
        layoutHandler = getLayoutHandler(parent);
        layoutHandler.addAt(ghost,
                            locatoin);
    }

    private void removeCurrentGhost() {
        if (null != layoutHandler && null != ghost) {
            layoutHandler.remove(ghost);
            ghost = null;
        }
        layoutHandler = null;
    }

    @Override
    protected boolean afterMoveComplete() {
        final boolean result = super.afterMoveComplete();
        removeCurrentGhost();
        return result;
    }

    @Override
    public void execute() {
        // TODO
    }

    @Override
    public void clear() {
        removeCurrentGhost();
    }

    @Override
    public void reset() {
        removeCurrentGhost();
    }

    @Override
    public Point2D getAdjust() {
        return null;
    }

    @Override
    public Point2D getLayoutCandidateLocation() {
        assert null != ghost;
        return ghost.getLocation();
    }

    private static class BoxShapeGhostSupplier implements Function<WiresShape, WiresShape> {

        @Override
        public WiresShape apply(final WiresShape shape) {
            final BoundingBox boundingBox = shape.getGroup().getBoundingBox();
            return new WiresShape(new MultiPath().rect(0,
                                                       0,
                                                       boundingBox.getWidth(),
                                                       boundingBox.getHeight()))
                    .setDraggable(false);
        }
    }

    private static void logicallyReplace(final WiresContainer parent,
                                         final WiresShape original,
                                         final WiresShape replacement) {
        if (original == null) {
            return;
        }
        if (replacement == null) {
            return;
        }
        if (replacement.getParent() == parent) {
            return;
        }

        parent.getChildShapes().set(getIndex(parent,
                                             original),
                                    replacement);
        parent.getContainer().getChildNodes().set(getNodeIndex(parent,
                                                               original.getGroup()),
                                                  replacement.getGroup());

        original.setParent(null);
        replacement.setParent(parent);

        // TODO: Add replace method for layout container entries?
        // TODO: Set location for replacement shape here?

        if (original.getMagnets() != null) {
            original.getControl().getMagnetsControl().shapeMoved();
        }

        if (replacement.getMagnets() != null) {
            replacement.getControl().getMagnetsControl().shapeMoved();
        }
    }

    private static int getIndex(final WiresContainer parent,
                                final WiresShape shape) {
        return parent.getChildShapes().toList().indexOf(shape);
    }

    private static int getNodeIndex(final WiresContainer parent,
                                    final Group group) {
        return parent.getContainer().getChildNodes().toList().indexOf(group);
    }
}
