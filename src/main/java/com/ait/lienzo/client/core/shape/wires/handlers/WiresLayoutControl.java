package com.ait.lienzo.client.core.shape.wires.handlers;

import com.ait.lienzo.client.core.shape.wires.WiresShape;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresControl;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresMoveControl;
import com.ait.lienzo.client.core.types.Point2D;

public interface WiresLayoutControl extends WiresMoveControl, WiresControl {

    Point2D getLayoutCandidateLocation();

}
