package org.roger600.lienzo.client;

import com.ait.lienzo.client.core.animation.AnimationCallback;
import com.ait.lienzo.client.core.animation.AnimationProperties;
import com.ait.lienzo.client.core.animation.AnimationProperty;
import com.ait.lienzo.client.core.animation.AnimationTweener;
import com.ait.lienzo.client.core.animation.IAnimation;
import com.ait.lienzo.client.core.animation.IAnimationHandle;
import com.ait.lienzo.client.core.shape.Circle;
import com.ait.lienzo.client.widget.LienzoPanel2;
import com.ait.lienzo.shared.core.types.ColorName;
import elemental2.dom.CSSProperties;
import elemental2.dom.HTMLButtonElement;
import elemental2.dom.HTMLDivElement;
import org.gwtproject.dom.style.shared.Display;
import org.roger600.lienzo.client.util.FPSCounter;

/*
    See https://developers.google.com/web/updates/2018/08/offscreen-canvas
 */
public class PerformanceMainThreadBlockingExample extends BaseExample implements Example {

    private static final double RADIUS = 50;
    private static final double DURATION = 1000;

    public PerformanceMainThreadBlockingExample(String title) {
        super(title);
    }

    private HTMLButtonElement makeMainThreadBusyButton;
    private Circle circle;
    private IAnimationHandle animationHandle;
    private FPSCounter fpsCounter;

    @Override
    public void init(LienzoPanel2 panel,
                     HTMLDivElement topDiv) {
        super.init(panel, topDiv);

        topDiv.style.width = CSSProperties.WidthUnionType.of("100%");
        topDiv.style.display = Display.INLINE_BLOCK.getCssName();
        heightOffset = 30;

        fpsCounter =
                FPSCounter
                        .toElement(displayer -> {
                            displayer.style.cssFloat = "right";
                            topDiv.appendChild(displayer);
                        }).start();

        makeMainThreadBusyButton = createButton("Make main thread busy",
                                                () -> {
                                                    makeMainThreadBusyButton.disabled = true;
                                                    makeMainThreadBusy();
                                                    makeMainThreadBusyButton.disabled = false;
                                                });
        topDiv.appendChild(makeMainThreadBusyButton);
    }

    @Override
    public void run() {
        runAnimatedCircleTest();
    }

    @Override
    public void destroy() {
        super.destroy();
        animationHandle.stop();
        animationHandle = null;
        fpsCounter.destroy();
        fpsCounter = null;
        makeMainThreadBusyButton.remove();
    }

    private void runAnimatedCircleTest() {
        layer.clear();

        circle = new Circle(RADIUS)
                .setX(200)
                .setY(200)
                .setFillColor(ColorName.BLACK)
                .setFillAlpha(1);

        animateCircle();

        layer.add(circle);
    }

    private void animateCircle() {
        double target = circle.getRadius() == RADIUS ? RADIUS * 2 : RADIUS;
        console.log("Animating circle to radius [" + target + "]");
        animationHandle = circle.animate(AnimationTweener.LINEAR,
                                         AnimationProperties.toPropertyList(AnimationProperty.Properties.RADIUS(target)),
                                         DURATION,
                                         new AnimationCallback() {
                                             @Override
                                             public void onClose(IAnimation animation, IAnimationHandle handle) {
                                                 super.onClose(animation, handle);
                                                 animateCircle();
                                             }
                                         })
                .run();
    }

    private void makeMainThreadBusy() {
        console.log("Making main thread busy...");
        fibonacci(40);
        console.log("Main thread free again");
    }

    private static int fibonacci(int num) {
        return (num <= 1) ? 1 : fibonacci(num - 1) + fibonacci(num - 2);
    }
}
