package com.ait.lienzo.client.core.types;

import com.ait.lienzo.client.core.Context2D;
import com.ait.lienzo.client.core.NativeContext2D;
import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.widget.panel.LienzoPanel;
import com.ait.lienzo.tools.client.collection.NFastArrayList;
import com.ait.lienzo.tools.client.event.EventType;
import com.ait.lienzo.tools.client.event.MouseEventUtil;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLCanvasElement;
import elemental2.dom.MouseEvent;
import elemental2.dom.MouseEventInit;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(namespace = JsPackage.GLOBAL)
public class JsLienzo {

    public LienzoPanel panel;
    public Layer layer;

    public Rectangle RECT(String id, double x, double y, double width, double height) {
        return new Rectangle(width, height)
                .setID(id)
                .setAlpha(1)
                .setStrokeAlpha(1)
                .setStrokeWidth(1)
                .setX(x)
                .setY(y);
    }

    public void click(IPrimitive<?> shape) {
        Point2D location = shape.getComputedLocation();
        double x = location.getX();
        double y = location.getY();
        clickAt(x, y);

    }

    public void clickAt(double x, double y) {
        dispatchEvent(EventType.CLICKED.getType(), x, y);
    }

    public void move(IPrimitive<?> shape, double tx, double ty) {
        Point2D location = shape.getComputedLocation();
        double x = location.getX();
        double y = location.getY();
        moveAt(x, y, tx, ty);
    }

    public void moveAt(double sx, double sy, double tx, double ty) {
        dispatchEvent(EventType.MOUSE_DOWN.getType(), sx, sy);
        dispatchEvent(EventType.MOUSE_MOVE.getType(), sx, sy);
        //dispatchEvent(EventType.MOUSE_MOVE.getType(), (tx - sx) / 2, (ty - sy) / 2);
        dispatchEvent(EventType.MOUSE_MOVE.getType(), tx, ty);
        dispatchEvent(EventType.MOUSE_UP.getType(), tx, ty);
    }

    public int getPanelOffsetLeft() {
        int result = panel.getElement().offsetLeft;
        return result;
    }

    public int getPanelOffsetTop() {
        int result = panel.getElement().offsetTop;
        return result;
    }

    public MouseEvent dispatchEvent(String type,
                                    double clientX,
                                    double clientY) {
        MouseEventInit mouseEventInit = MouseEventInit.create();
        mouseEventInit.setView(DomGlobal.window);
        double x = clientX + getPanelOffsetLeft();
        double y = clientY + getPanelOffsetTop();
        mouseEventInit.setClientX(x);
        mouseEventInit.setClientY(y);
        mouseEventInit.setScreenX(y);
        mouseEventInit.setScreenY(y);
        mouseEventInit.setButton(MouseEventUtil.BUTTON_LEFT);
        MouseEvent event = new MouseEvent(type, mouseEventInit);
        boolean cancelled = !panel.getElement().dispatchEvent(event);
        if (cancelled) {
            // A handler called preventDefault.
        } else {
            // None of the handlers called preventDefault.
        }
        return event;
    }

    public HTMLCanvasElement getCanvas() {
        HTMLCanvasElement canvasElement = layer.getCanvasElement();
        return canvasElement;
    }

    public void add(IPrimitive<?> shape) {
        layer.add(shape);
    }

    public IPrimitive<?> getShape(String id) {
        NFastArrayList<IPrimitive<?>> shapes = layer.getChildNodes();
        if (null != shapes)
        {
            for (IPrimitive<?> shape : shapes.asList())
            {
                String shapeID = shape.getID();
                if (id.equals(shapeID)) {
                    return shape;
                }
            }
        }
        return null;
    }

    public void draw() {
        layer.draw();
    }

    public NativeContext2D getNativeContent() {
        Context2D context = layer.getContext();
        NativeContext2D nativeContext = context.getNativeContext();
        return nativeContext;
    }


}
