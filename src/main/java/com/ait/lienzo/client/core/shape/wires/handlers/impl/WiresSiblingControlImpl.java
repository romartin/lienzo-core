package com.ait.lienzo.client.core.shape.wires.handlers.impl;

import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.wires.WiresContainer;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresSiblingControl;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.client.core.util.Geometry;
import com.ait.lienzo.shared.core.types.ColorName;
import com.ait.tooling.common.api.java.util.function.BiPredicate;
import com.google.gwt.core.client.GWT;

public class WiresSiblingControlImpl implements WiresSiblingControl {

    private static final Point2D ADJUST_NONE = new Point2D(0, 0);
    private static final double PADDING = 10;

    private final WiresParentPickerControlImpl parentPickerControl;
    private final WiresShape shape;
    private BiPredicate<WiresContainer, WiresContainer> active;
    private MultiPath ghost;

    public WiresSiblingControlImpl(final WiresShape shape,
                                   final WiresParentPickerControlImpl parentPickerControl) {
        this.shape = shape;
        this.parentPickerControl = parentPickerControl;
        this.ghost = null;
        this.active = new BiPredicate<WiresContainer, WiresContainer>() {
            @Override
            public boolean test(WiresContainer parent,
                                WiresContainer shape) {
                return false;
            }
        };
    }

    public WiresSiblingControlImpl setActive(final BiPredicate<WiresContainer, WiresContainer> active) {
        this.active = active;
        return this;
    }

    @Override
    public void onMoveStart(final double x,
                            final double y) {
        parentPickerControl.onMoveStart(x,
                                        y);
    }

    @Override
    public boolean onMove(final double dx,
                          final double dy) {
        parentPickerControl.onMove(dx, dy);
        if (isActive()) {
            final WiresContainer parent = parentPickerControl.getParent();
            if (null != parent) {
                createGhost().setLocation(calculateCandidateGhostLocation(shape, parent));
            }
        } else {
            destroyGhost();
        }
        return false;
    }

    private static Point2D calculateCandidateGhostLocation(final WiresShape shape,
                                                           final WiresContainer parent) {
        // TODO: Expensive.
        // TODO: Check bounds not exceeded!
        final BoundingBox parentBB = parent.getGroup().getComputedBoundingPoints().getBoundingBox();
        final BoundingBox shapeBB = shape.getGroup().getComputedBoundingPoints().getBoundingBox();
        final Point2D parentCenter = Geometry.findCenter(parentBB);
        final Point2D shapeCenter = Geometry.findCenter(shapeBB);
        final double[] parentarray = {parentCenter.getX(), parentCenter.getY()};
        final double[] shapearray = {shapeCenter.getX(), shapeCenter.getY()};
        // TODO: Improve cardinal detection
        final double ratio = Geometry.getVectorRatio(parentarray, shapearray);
        final double angle = Geometry.getVectorAngle(parentarray, shapearray);
        //GWT.log("ANGLE = " + angle);
        //GWT.log("RATIO = " + angle);

        if (angle > 0) {
            // BELLOW
            return new Point2D(shape.getX(),
                               parentBB.getY() + parentBB.getHeight())
                    .offset(0, PADDING);
        } else {
            // TOP
            return new Point2D(shape.getX(),
                               parentBB.getY() - parentBB.getHeight())
                    .offset(0, -PADDING);
        }
    }

    @Override
    public Point2D getCandidateLocation() {
        return ghost
                .getLocation()
                .copy();
    }

    @Override
    public boolean accept() {
        return null != ghost;
    }

    @Override
    public Point2D getAdjust() {
        return ADJUST_NONE;
    }

    @Override
    public boolean onMoveComplete() {
        return parentPickerControl.onMoveComplete();
    }

    @Override
    public void execute() {
        // TODO: Hmmm... empty? :/
    }

    @Override
    public void clear() {
        destroyGhost();
        parentPickerControl.clear();
    }

    @Override
    public void reset() {
        destroyGhost();
        parentPickerControl.reset();
    }

    private boolean isActive() {
        final WiresContainer parent = parentPickerControl.getParent();
        return null != parent && active.test(parent,
                                             shape);
    }

    private MultiPath createGhost() {
        if (null == ghost) {
            ghost = shape.getPath()
                    .copy()
                    .setStrokeWidth(1)
                    .setStrokeColor(ColorName.BLACK)
                    .setStrokeAlpha(0.6)
                    .setFillColor(ColorName.BLACK)
                    .setFillAlpha(0.6);
            // TODO: Asolutes? Add into same shape parent?
            shape.getGroup().getLayer().getLayer().add(ghost);
        }
        return ghost;
    }

    private void destroyGhost() {
        if (null != ghost) {
            ghost.removeFromParent();
            ghost = null;
        }
    }
}
