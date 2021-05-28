package com.ait.lienzo.client.core.types;

import com.ait.lienzo.client.core.Context2D;
import com.ait.lienzo.client.core.NativeContext2D;
import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.shape.Layer;
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

    public void doubleClick(IPrimitive<?> shape) {
        Point2D location = shape.getComputedLocation();
        double x = location.getX();
        double y = location.getY();
        doubleClickAt(x, y);
    }

    public void click(IPrimitive<?> shape) {
        Point2D location = shape.getComputedLocation();
        double x = location.getX();
        double y = location.getY();
        clickAt(x, y);
    }

    public void over(IPrimitive<?> shape) {
        Point2D location = shape.getComputedLocation();
        double x = location.getX();
        double y = location.getY();
        mouseOver(x, y);
    }

    public void out(IPrimitive<?> shape) {
        Point2D location = shape.getComputedLocation();
        double x = location.getX();
        double y = location.getY();
        mouseOut(x, y);
    }

    public void clickAt(double x, double y) {
        dispatchEvent(EventType.CLICKED.getType(), x, y);
    }

    public void doubleClickAt(double x, double y) {
        dispatchEvent(EventType.DOUBLE_CLICKED.getType(), x, y);
    }

    public void move(IPrimitive<?> shape, double tx, double ty) {
        Point2D location = shape.getComputedLocation();
        double x = location.getX();
        double y = location.getY();
        drag(x, y, tx, ty);
    }

    public void mouseOver(double x, double y) {
        dispatchEvent(EventType.MOUSE_OVER.getType(), x, y);
    }

    public void mouseOut(double x, double y) {
        dispatchEvent(EventType.MOUSE_OUT.getType(), x, y);
    }

    public void mouseDown(double x, double y) {
        dispatchEvent(EventType.MOUSE_DOWN.getType(), x, y);
    }

    public void mouseMove(double x, double y) {
        dispatchEvent(EventType.MOUSE_MOVE.getType(), x, y);

    }
    public void mouseUp(double x, double y) {
        dispatchEvent(EventType.MOUSE_UP.getType(), x, y);
    }

    public void drag(double sx, double sy, double tx, double ty) {
        mouseDown(sx, sy);
        mouseMove(sx, sy);
        //dispatchEvent(EventType.MOUSE_MOVE.getType(), (tx - sx) / 2, (ty - sy) / 2);
        mouseMove(tx, ty);
        mouseUp(tx, ty);
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
