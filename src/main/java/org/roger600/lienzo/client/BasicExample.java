package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.shared.core.types.ColorName;

public class BasicExample extends BaseExample implements Example {

    public BasicExample(final String title) {
        super(title);
    }

    @Override
    public void run() {

        Rectangle r = new Rectangle(100, 100);
        r.setX(300)
                .setY(300)
                .setFillColor(ColorName.BLACK)
                .setStrokeColor(ColorName.BLACK)
                .setStrokeWidth(1.5);

        r.setDraggable(true);

        layer.add(r);

        layer.draw();
    }

    @Override
    public void onResize() {
        super.onResize();
    }
}
