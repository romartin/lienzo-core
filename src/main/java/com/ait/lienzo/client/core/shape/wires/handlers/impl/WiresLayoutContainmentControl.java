package com.ait.lienzo.client.core.shape.wires.handlers.impl;

import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresContainmentControl;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresLayoutControl;
import com.ait.lienzo.client.core.shape.wires.picker.ColorMapBackedPicker;
import com.ait.lienzo.client.core.types.Point2D;

public class WiresLayoutContainmentControl implements WiresContainmentControl {

    private final WiresContainmentControl containmentControl;
    private final WiresLayoutControl layoutControl;

    public WiresLayoutContainmentControl(final WiresShape shape,
                                         final ColorMapBackedPicker.PickerOptions pickerOptions) {
        final WiresContainmentControlImpl containmentControl =
                new WiresContainmentControlImpl(shape,
                                                pickerOptions);
        this.containmentControl = containmentControl;
        this.layoutControl = new WiresLayoutControlImpl(containmentControl.getParentPickerControl());
    }

    @Override
    public WiresContainmentControl setEnabled(final boolean enabled) {
        return containmentControl.setEnabled(enabled);
    }

    @Override
    public void onMoveStart(final double x,
                            final double y) {
        containmentControl.onMoveStart(x,
                                       y);
        layoutControl.onMoveStart(x,
                                  y);
    }

    @Override
    public boolean onMove(final double dx,
                          final double dy) {
        final boolean contAdj = containmentControl.onMove(dx,
                                                          dy);
        if (isAllow()) {
            layoutControl.onMove(dx,
                                 dy);
        }
        return contAdj;
    }

    @Override
    public boolean onMoveComplete() {
        containmentControl.onMoveComplete();
        layoutControl.onMoveComplete();
        return false;
    }

    @Override
    public Point2D getAdjust() {
        return containmentControl.getAdjust();
    }

    @Override
    public boolean isAllow() {
        return containmentControl.isAllow();
    }

    @Override
    public boolean accept() {
        return containmentControl.accept();
    }

    @Override
    public Point2D getCandidateLocation() {
        return containmentControl.getCandidateLocation();
    }

    @Override
    public void execute() {
        containmentControl.execute();
        layoutControl.execute();
    }

    @Override
    public void clear() {
        containmentControl.execute();
        layoutControl.clear();
    }

    @Override
    public void reset() {
        containmentControl.reset();
        layoutControl.reset();
    }
}
