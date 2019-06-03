package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.widget.LienzoPanel2;
import com.ait.lienzo.shared.core.types.ColorName;
import elemental2.core.JsArray;
import elemental2.dom.CSSProperties;
import elemental2.dom.HTMLButtonElement;
import elemental2.dom.HTMLDivElement;
import org.gwtproject.dom.style.shared.Display;

/*
    See https://issues.jboss.org/browse/JBPM-7227
 */
public class PerformanceMultipleCanvasTestsExample extends BaseExample implements Example {

    private static final int WIDTH = 300;
    private static final int HEIGHT = 300;

    private HTMLDivElement buttonsContainer;
    private HTMLButtonElement addCanvasButton;
    private HTMLButtonElement removeCanvasButton;
    private HTMLDivElement canvasesContainer;

    private JsArray<LienzoPanel2> panels = new JsArray<>();

    public PerformanceMultipleCanvasTestsExample(String title) {
        super(title);
    }

    @Override
    public void init(LienzoPanel2 panel,
                     HTMLDivElement topDiv) {
        super.init(panel, topDiv);
        topDiv.style.width = CSSProperties.WidthUnionType.of("100%");
        topDiv.style.display = Display.INLINE_BLOCK.getCssName();

        buttonsContainer = createDiv();
        topDiv.appendChild(buttonsContainer);

        addCanvasButton = createButton("Add canvas", this::addCanvas);
        buttonsContainer.appendChild(addCanvasButton);

        removeCanvasButton = createButton("Remove canvas", this::removeCanvas);
        buttonsContainer.appendChild(removeCanvasButton);

        canvasesContainer = createDiv();
        canvasesContainer.style.cssFloat = "left";
        topDiv.appendChild(canvasesContainer);

        // Hide the default main panel...
        panel.getViewport().getElement().style.display = "none";
    }

    private void removeCanvas() {
        if (panels.length > 0) {
            LienzoPanel2 p = panels.pop();
            p.destroy();
        }
    }

    private void addCanvas() {
        HTMLDivElement canvasContainer = createDiv();
        canvasContainer.style.width = CSSProperties.WidthUnionType.of(WIDTH);
        canvasContainer.style.height = CSSProperties.HeightUnionType.of(HEIGHT);
        canvasContainer.style.border = "1px solid black";
        canvasesContainer.appendChild(canvasContainer);

        LienzoPanel2 panel = new LienzoPanel2(canvasContainer, true, WIDTH, HEIGHT);
        Rectangle rectangle =
                new Rectangle(100, 100)
                        .setX(100)
                        .setY(100)
                        .setFillColor(ColorName.BLACK)
                        .setFillAlpha(1)
                        .setDraggable(true);
        Layer layer = new Layer();
        panel.add(layer);
        layer.add(rectangle);
        layer.draw();
        panels.push(panel);
    }

    @Override
    public void run() {

    }

    @Override
    public void destroy() {
        super.destroy();
        panels.forEach((p, i, lienzoPanel2s) -> {
            p.destroy();
            return null;
        });
        buttonsContainer.remove();
        addCanvasButton.remove();
        removeCanvasButton.remove();
        canvasesContainer.remove();
    }
}
