package com.ait.lienzo.client.core.shape.wires.handlers.impl;

import com.ait.lienzo.client.core.shape.wires.ILocationAcceptor;
import com.ait.lienzo.client.core.shape.wires.PickerPart;
import com.ait.lienzo.client.core.shape.wires.WiresConnector;
import com.ait.lienzo.client.core.shape.wires.WiresContainer;
import com.ait.lienzo.client.core.shape.wires.WiresManager;
import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.handlers.AlignAndDistributeControl;
import com.ait.lienzo.client.core.shape.wires.handlers.MouseEvent;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresContainmentControl;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresDockingControl;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresMagnetsControl;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresShapeControl;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresSiblingControl;
import com.ait.lienzo.client.core.shape.wires.picker.ColorMapBackedPicker;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.client.core.util.Geometry;
import com.ait.tooling.common.api.java.util.function.BiPredicate;
import com.ait.tooling.nativetools.client.collection.NFastArrayList;

/**
 * The default WiresShapeControl implementation.
 * It orchestrates different controls for handling interactions with a single wires shape.
 */
public class WiresShapeControlImpl
        extends AbstractWiresBoundsConstraintControl
        implements WiresShapeControl {

    private final WiresParentPickerCachedControl parentPickerControl;
    private final WiresSiblingControl siblingControl;
    private WiresMagnetsControl m_magnetsControl;
    private WiresDockingControl m_dockingAndControl;
    private WiresContainmentControl m_containmentControl;
    private AlignAndDistributeControl m_alignAndDistributeControl;
    private BoundingBox shapeBounds;
    private Point2D m_adjust;
    private boolean c_accept;
    private boolean d_accept;
    private boolean s_accept;
    private WiresConnector[] m_connectorsWithSpecialConnections;

    public WiresShapeControlImpl(final WiresShape shape,
                                 final WiresManager wiresManager) {
        final ColorMapBackedPicker.PickerOptions pickerOptions =
                new ColorMapBackedPicker.PickerOptions(true,
                                                       wiresManager.getDockingAcceptor().getHotspotSize());
        parentPickerControl = new WiresParentPickerCachedControl(shape,
                                                                 pickerOptions);
        m_dockingAndControl = new WiresDockingControlImpl(getParentPickerControl());
        m_containmentControl = new WiresContainmentControlImpl(getParentPickerControl());
        m_magnetsControl = new WiresMagnetsControlImpl(shape);
        siblingControl = new WiresSiblingControlImpl(shape,
                                                     getParentPickerControl())
                .setActive(new BiPredicate<WiresContainer, WiresContainer>() {
                    @Override
                    public boolean test(final WiresContainer parent,
                                        final WiresContainer shape) {
                        return !m_containmentControl.isAllow() &&
                                !m_dockingAndControl.isAllow() &&
                                getLocationAcceptor().sibling(parent, shape);
                    }
                });
    }

    public WiresShapeControlImpl(final WiresParentPickerCachedControl parentPickerControl,
                                 final WiresMagnetsControl m_magnetsControl,
                                 final WiresDockingControl m_dockingAndControl,
                                 final WiresContainmentControl m_containmentControl,
                                 final WiresSiblingControl siblingControl) {
        this.parentPickerControl = parentPickerControl;
        this.m_magnetsControl = m_magnetsControl;
        this.m_dockingAndControl = m_dockingAndControl;
        this.m_containmentControl = m_containmentControl;
        this.siblingControl = siblingControl;
    }

    @Override
    public void onMoveStart(double x,
                            double y) {
        shapeBounds = getShape().getGroup().getComputedBoundingPoints().getBoundingBox();
        m_adjust = new Point2D(0, 0);
        d_accept = false;
        c_accept = false;
        s_accept = false;

        // Important - skip the shape and its children, if any, from the picker.
        // Otherwise children or the shape itself are being processed by the parent picker
        // and it ends up with wrong parent-child nested issues.
        final NFastArrayList<WiresShape> shapesToSkip =
                parentPickerControl.getPickerOptions().getShapesToSkip();
        shapesToSkip.clear();
        shapesToSkip.add(getShape());
        final NFastArrayList<WiresShape> children = getShape().getChildShapes();
        for (int i = 0; i < children.size(); i++) {
            shapesToSkip.add(children.get(i));
        }

        // Delegate mvoe start to the shape's docking control
        if (m_dockingAndControl != null) {
            m_dockingAndControl.onMoveStart(x,
                                            y);
        }

        // Delegate mvoe start to the shape's containment control
        if (m_containmentControl != null) {
            m_containmentControl.onMoveStart(x,
                                             y);
        }

        if (siblingControl != null) {
            siblingControl.onMoveStart(x,
                                       y);
        }

        // Delegate mvoe start to the align and distribute control.
        if (m_alignAndDistributeControl != null) {
            m_alignAndDistributeControl.dragStart();
        }

        // index nested shapes that have special connectors, to avoid searching during drag.
        m_connectorsWithSpecialConnections = ShapeControlUtils.collectionSpecialConnectors(getShape());
    }

    @Override
    public boolean isOutOfBounds(double dx, double dy) {
        // Check the location bounds, if any.
        if (null != getConstrainedBounds()) {
            final double shapeMinX = shapeBounds.getMinX() + dx;
            final double shapeMinY = shapeBounds.getMinY() + dy;
            final double shapeMaxX = shapeMinX + (shapeBounds.getMaxX() - shapeBounds.getMinX());
            final double shapeMaxY = shapeMinY + (shapeBounds.getMaxY() - shapeBounds.getMinY());
            if (shapeMinX <= getConstrainedBounds().getMinX() ||
                    shapeMaxX >= getConstrainedBounds().getMaxX() ||
                    shapeMinY <= getConstrainedBounds().getMinY() ||
                    shapeMaxY >= getConstrainedBounds().getMaxY()) {
                // Bounds are exceeded as from last adjusted location, so
                // just accept adjust and keep current location value.
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onMove(double dx,
                          double dy) {

        if (isOutOfBounds(dx, dy)) {
            return true;
        }

        // First step is to delegate the location deltas to the shared parent picker control.
        if (parentPickerControl.onMove(dx, dy)) {
            m_adjust = parentPickerControl.getAdjust();
            return true;
        }

        final Point2D dxy = new Point2D(dx,
                                        dy);

        final boolean isDockAdjust = null != m_dockingAndControl &&
                m_dockingAndControl.onMove(dx,
                                           dy);
        if (isDockAdjust) {
            final Point2D dadjust = m_dockingAndControl.getAdjust();
            double adjustDistance = Geometry.distance(dx, dy, dadjust.getX(), dadjust.getY());
            if (adjustDistance > getWiresManager().getDockingAcceptor().getHotspotSize()) {
                dxy.setX(dadjust.getX());
                dxy.setY(dadjust.getY());
            }
        }

        final boolean isContAdjust = null != m_containmentControl &&
                m_containmentControl.onMove(dx,
                                            dy);
        if (isContAdjust) {
            final Point2D cadjust = m_containmentControl.getAdjust();
            dxy.setX(cadjust.getX());
            dxy.setY(cadjust.getY());
        }

        final boolean isAlignDistroAdjust = null != m_alignAndDistributeControl &&
                m_alignAndDistributeControl.isDraggable() &&
                m_alignAndDistributeControl.dragAdjust(dxy);

        // Special adjustments.
        boolean adjust = true;
        if ((isDockAdjust || isContAdjust)
                && isAlignDistroAdjust
                && (dxy.getX() != dx || dxy.getY() != dy)) {
            BoundingBox box = getShape().getPath().getBoundingBox();

            PickerPart part = getPicker().findShapeAt((int) (shapeBounds.getMinX() + dxy.getX() + (box.getWidth() / 2)),
                                                      (int) (shapeBounds.getMinY() + dxy.getY() + (box.getHeight() / 2)));

            if (part == null || part.getShapePart() != PickerPart.ShapePart.BORDER) {
                dxy.setX(dx);
                dxy.setY(dy);
                adjust = false;
            }
        }

        if (null != siblingControl) {
            siblingControl.onMove(dx,
                                  dy);
        }

        // Cache the current adjust point.
        m_adjust = dxy;
        parentPickerControl.onMoveAdjusted(m_adjust);

        shapeUpdated(false);

        return adjust;
    }

    @Override
    public boolean accept() {
        Point2D location = null;
        d_accept = null != getDockingControl() && getDockingControl().accept();
        c_accept = !d_accept && null != getContainmentControl() && getContainmentControl().accept();
        s_accept = null != siblingControl && siblingControl.accept();

        if (c_accept) {
            location = getContainmentControl().getCandidateLocation();
        } else if (d_accept) {
            location = getDockingControl().getCandidateLocation();
        } else if (s_accept) {
            location = siblingControl.getCandidateLocation();
        }

        boolean accept = false;
        if (null != location) {
            accept = getLocationAcceptor()
                    .accept(new WiresShape[]{getShape()},
                            new Point2D[]{location});
        }

        return accept;
    }

    @Override
    public boolean onMoveComplete() {
        final boolean dcompleted = null == m_dockingAndControl || m_dockingAndControl.onMoveComplete();
        final boolean ccompleted = null == m_containmentControl || m_containmentControl.onMoveComplete();
        if (m_alignAndDistributeControl != null) {
            m_alignAndDistributeControl.dragEnd();
        }
        if (null != siblingControl) {
            siblingControl.onMoveComplete();
        }
        return dcompleted && ccompleted;
    }

    @Override
    public void execute() {
        final boolean accept = c_accept || d_accept || s_accept;
        if (!accept) {
            throw new IllegalStateException("Execute should not be called. No containment neither docking operations have been accepted.");
        }
        Point2D location;
        if (d_accept) {
            location = getDockingControl().getCandidateLocation();
            getDockingControl().execute();
        } else if (c_accept) {
            location = getContainmentControl().getCandidateLocation();
            getContainmentControl().execute();
        } else {
            location = siblingControl.getCandidateLocation();
        }
        getParentPickerControl().setShapeLocation(location);
        ShapeControlUtils.checkForAndApplyLineSplice(getWiresManager(),
                                                     getShape());
        shapeUpdated(true);
        clear();
    }

    @Override
    public void clear() {
        parentPickerControl.clear();
        if (null != m_dockingAndControl) {
            m_dockingAndControl.clear();
        }
        if (null != m_containmentControl) {
            m_containmentControl.clear();
        }
        if (null != siblingControl) {
            siblingControl.clear();
        }
        clearState();
    }

    @Override
    public void reset() {
        if (null != getDockingControl()) {
            getDockingControl().reset();
        }
        if (null != getContainmentControl()) {
            getContainmentControl().reset();
        }
        parentPickerControl.reset();
        if (null != m_alignAndDistributeControl) {
            m_alignAndDistributeControl.dragEnd();
        }
        if (null != siblingControl) {
            siblingControl.reset();
        }
        getShape().shapeMoved();
        clearState();
    }

    @Override
    public void onMouseClick(MouseEvent event) {
        parentPickerControl.onMouseClick(event);
        if (getWiresManager().getSelectionManager() != null) {
            getWiresManager().getSelectionManager().selected(getShape(),
                                                             event.isShiftKeyDown());
        }
        getShape().getGroup().getLayer().draw();
    }

    @Override
    public void onMouseDown(MouseEvent event) {
        parentPickerControl.onMouseDown(event);
    }

    @Override
    public void onMouseUp(MouseEvent event) {
        parentPickerControl.onMouseUp(event);
    }

    @Override
    public void setAlignAndDistributeControl(AlignAndDistributeControl control) {
        this.m_alignAndDistributeControl = control;
    }

    @Override
    public WiresMagnetsControl getMagnetsControl() {
        return m_magnetsControl;
    }

    @Override
    public AlignAndDistributeControl getAlignAndDistributeControl() {
        return m_alignAndDistributeControl;
    }

    @Override
    public WiresDockingControl getDockingControl() {
        return m_dockingAndControl;
    }

    @Override
    public WiresContainmentControl getContainmentControl() {
        return m_containmentControl;
    }

    @Override
    public WiresParentPickerCachedControl getParentPickerControl() {
        return parentPickerControl;
    }

    @Override
    public Point2D getAdjust() {
        return m_adjust;
    }

    private void shapeUpdated(final boolean isAcceptOp) {
        ShapeControlUtils.updateSpecialConnections(m_connectorsWithSpecialConnections,
                                                   isAcceptOp);
        ShapeControlUtils.updateNestedShapes(getShape());
    }

    private void clearState() {
        shapeBounds = null;
        m_adjust = new Point2D(0, 0);
        m_connectorsWithSpecialConnections = null;
    }

    private WiresShape getShape() {
        return parentPickerControl.getShape();
    }

    private WiresManager getWiresManager() {
        return getShape().getWiresManager();
    }

    private ColorMapBackedPicker getPicker() {
        return parentPickerControl.getPicker();
    }

    private ILocationAcceptor getLocationAcceptor() {
        return getShape().getWiresManager().getLocationAcceptor();
    }
}
