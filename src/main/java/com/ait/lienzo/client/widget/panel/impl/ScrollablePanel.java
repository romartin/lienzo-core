/*
 * Copyright 2015 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ait.lienzo.client.widget.panel.impl;

import com.ait.lienzo.client.core.shape.Layer;
import com.ait.lienzo.client.core.shape.Viewport;
import com.ait.lienzo.client.core.types.Transform;
import com.ait.lienzo.client.widget.panel.Bounds;
import com.ait.lienzo.client.widget.panel.BoundsProvider;
import com.ait.lienzo.client.widget.panel.LienzoBoundsPanel;
import com.ait.lienzo.client.widget.panel.LienzoPanel;
import com.ait.lienzo.client.widget.panel.mediators.RestrictedMousePanMediator;
import com.ait.lienzo.client.widget.panel.util.LienzoPanelUtils;
import elemental2.dom.CSSProperties;
import elemental2.dom.DomGlobal;
import elemental2.dom.EventListener;
import elemental2.dom.HTMLDivElement;

import static com.ait.lienzo.client.widget.panel.util.LienzoPanelUtils.createDiv;
import static com.ait.lienzo.client.widget.panel.util.LienzoPanelUtils.setPanelSize;

public class ScrollablePanel extends LienzoBoundsPanel {

    private final HTMLDivElement domElementContainer = createDiv();
    private final HTMLDivElement internalScrollPanel = createDiv();
    private final HTMLDivElement scrollPanel = createDiv();
    private final HTMLDivElement rootPanel = createDiv();
    private EventListener mouseDownListener;
    private EventListener mouseUpListener;
    private EventListener mouseOutListener;
    private EventListener scrollListener;
    private EventListener mouseMoveListener;
    private EventListener mouseWheelListener;
    private int widePx;
    private int highPx;
    private boolean isMouseDown = false;
    private RestrictedMousePanMediator panMediator;

    public static ScrollablePanel newPanel(final HTMLDivElement parent,
                                           final BoundsProvider layerBoundsProvider) {
        final int[] panelPxSize = LienzoPanelUtils.getParentFitSize(parent);
        final int wide = panelPxSize[0];
        final int high = panelPxSize[1];
        return new ScrollablePanel(parent,
                                   LienzoPanelImpl.newPanel(createDiv(),
                                                            wide,
                                                            high),
                                   layerBoundsProvider,
                                   wide,
                                   high);
    }

    public ScrollablePanel(final HTMLDivElement parent,
                            final LienzoPanel lienzoPanel,
                            final BoundsProvider layerBoundsProvider,
                            final int wide,
                            final int high) {
        super(lienzoPanel,
              layerBoundsProvider);
        setupPanels(parent);
        setPxSize(wide, high);
    }

    @Override
    public LienzoBoundsPanel set(final Layer layer) {
        super.set(layer);
        scrollPanel.style.position = "relative";
        scrollPanel.style.overflow = "scroll";
        internalScrollPanel.style.position = "absolute";
        domElementContainer.style.position = "absolute";
        domElementContainer.style.zIndex = CSSProperties.ZIndexUnionType.of(1);
        synchronizeScrollSize();
        // TODO setupMouseDragSynchronization();
        return this;
    }

    private void setupMouseDragSynchronization()
    {
        if (null != getViewport())
        {
            // TODO: Remove this mediator once destroying the panel.
            panMediator = new RestrictedMousePanMediator(this)
            {
                @Override
                protected void onMouseMove(int x, int y)
                {
                    refreshScrollPosition();
                }
            };
            getViewport().getMediators().push(panMediator);
            // TODO: getViewport().addViewportTransformChangedHandler(new ViewportScaleChangeHandler(ScrollablePanelHandler.this, getViewport().getTransform()))
        }
    }

    public Bounds getVisibleBounds() {
        if (null != getViewport()) {
            final Viewport viewport = getViewport();
            Transform transform = viewport.getTransform();
            if (transform == null) {
                viewport.setTransform(transform = new Transform());
            }
            final double x = transform.getTranslateX() / transform.getScaleX();
            final double y = transform.getTranslateY() / transform.getScaleY();
            final Bounds bounds = Bounds.empty();
            bounds.setX(x);
            bounds.setY(y);
            bounds.setHeight(Math.max(0, viewport.getHeight() / transform.getScaleX()));
            bounds.setWidth(Math.max(0, viewport.getWidth() / transform.getScaleY()));
            return bounds;
        }
        return null;
    }

    void addScrollEventListener(EventListener eventListener) {
        scrollPanel.addEventListener("scroll", eventListener);
    }

    void removeScrollEventListener(EventListener eventListener) {
        scrollPanel.removeEventListener("scroll", eventListener);
    }

    // TODO: Custom GWT event handling

    /*public final HandlerRegistration addLienzoPanelBoundsChangedEventHandler(final LienzoPanelBoundsChangedEventHandler handler)
    {
        Objects.requireNonNull(handler);

        return m_events.addHandler(LienzoPanelBoundsChangedEvent.TYPE, handler);
    }

    void fireLienzoPanelBoundsChangedEvent()
    {
        m_events.fireEvent(new LienzoPanelBoundsChangedEvent());
    }

    public final HandlerRegistration addLienzoPanelScrollEventHandler(final LienzoPanelScrollEventHandler handler)
    {
        Objects.requireNonNull(handler);

        return m_events.addHandler(LienzoPanelScrollEvent.TYPE, handler);
    }

    void fireLienzoPanelScrollEvent(final double pctX,
                                    final double pctY)
    {
        m_events.fireEvent(new LienzoPanelScrollEvent(pctX, pctY));
    }

    public final HandlerRegistration addLienzoPanelResizeEventHandler(final LienzoPanelResizeEventHandler handler)
    {
        Objects.requireNonNull(handler);

        return m_events.addHandler(LienzoPanelResizeEvent.TYPE, handler);
    }

    void fireLienzoPanelResizeEvent(final double width,
                                    final double height)
    {
        m_events.fireEvent(new LienzoPanelResizeEvent(width, height));
    }

    public final HandlerRegistration addLienzoPanelScaleChangedEventHandler(final LienzoPanelScaleChangedEventHandler handler)
    {
        Objects.requireNonNull(handler);

        return m_events.addHandler(LienzoPanelScaleChangedEvent.TYPE, handler);
    }

    void fireLienzoPanelScaleChangedEvent()
    {
        final Transform transform = getLayer().getViewport().getTransform();
        m_events.fireEvent(new LienzoPanelScaleChangedEvent(new Point2D(transform.getScaleX(),
                                                                        transform.getScaleY())));
    }*/

    private void fitToParentSize() {
        HTMLDivElement parent = (HTMLDivElement) rootPanel.parentNode;
        int offsetWidth = parent.offsetWidth;
        int offsetHeight = parent.offsetHeight;
        if (offsetWidth > 0 && offsetHeight > 0) {
            setPxSize(offsetWidth, offsetHeight);
        }
    }

    public void setPxSize(final int widePx,
                          final int highPx) {
        this.widePx = widePx;
        this.highPx = highPx;
        updatePanelsSizes(widePx, highPx);
        // TODO fireLienzoPanelResizeEvent(widePx, highPx);
    }

    @Override
    public HTMLDivElement getElement() {
        return rootPanel;
    }

    @Override
    public int getWidePx() {
        return widePx;
    }

    @Override
    public int getHighPx() {
        return highPx;
    }

    @Override
    public LienzoBoundsPanel onRefresh() {
        synchronizeScrollSize();
        refreshScrollPosition();
        batch();
        return this;
    }

    @Override
    public void onResize() {
        DomGlobal.console.log("Resiziiing");
        super.onResize();
        if (isContainerStillOpened()) {
            onScroll();
            ScrollablePanel.this.fitToParentSize();
            ScrollablePanel.this.refresh();
        }
    }

    private boolean isContainerStillOpened() {
        return rootPanel.parentNode != null;
    }

    @Override
    protected void doDestroy() {
        removeHandlers();
        rootPanel.remove();
        isMouseDown = false;
    }

    private void setupPanels(final HTMLDivElement parent) {
        // DOM tree.
        scrollPanel.appendChild(internalScrollPanel);
        domElementContainer.appendChild(getLienzoPanel().getElement());
        rootPanel.appendChild(domElementContainer);
        rootPanel.appendChild(scrollPanel);
        rootPanel.style.outlineStyle = "none";
        parent.appendChild(rootPanel);
        // Event listeners.
        mouseDownListener = e -> ScrollablePanel.this.onStart();
        mouseUpListener = e -> ScrollablePanel.this.onComplete();
        mouseOutListener = e -> ScrollablePanel.this.onComplete();
        scrollListener = e -> ScrollablePanel.this.onScroll();
        mouseMoveListener = e -> ScrollablePanel.this.enablePointerEvents();
        mouseWheelListener = e -> ScrollablePanel.this.disablePointerEvents();
        // Attach event listeners.
        rootPanel.addEventListener("mousedown", mouseDownListener);
        rootPanel.addEventListener("mouseup", mouseUpListener);
        rootPanel.addEventListener("mouseout", mouseOutListener);
        rootPanel.addEventListener("mousemove", mouseMoveListener);
        domElementContainer.addEventListener("mousewheel", mouseWheelListener);
        addScrollEventListener(scrollListener);
    }

    @Override
    public LienzoPanelImpl getLienzoPanel()
    {
        return (LienzoPanelImpl) super.getLienzoPanel();
    }

    private void removeHandlers() {
        rootPanel.removeEventListener("mousedown", mouseDownListener);
        rootPanel.removeEventListener("mouseup", mouseUpListener);
        rootPanel.removeEventListener("mouseout", mouseOutListener);
        rootPanel.removeEventListener("mousemove", mouseMoveListener);
        domElementContainer.removeEventListener("mousewheel", mouseWheelListener);
        removeScrollEventListener(scrollListener);
    }

    private void enablePointerEvents() {
        domElementContainer.style.pointerEvents = "initial";
    }

    private void disablePointerEvents() {
        domElementContainer.style.pointerEvents = "none";
    }

    private void onStart() {
        isMouseDown = true;
        rootPanel.focus();
    }

    private void onComplete() {
        if (isMouseDown) {
            isMouseDown = false;
            refresh();
        }
    }

    private void onScroll() {
        DomGlobal.console.log("Scroooolling");
        // Prevent DOMElements scrolling into view when they receive the focus
        domElementContainer.scrollTop = 0;
        domElementContainer.scrollLeft = 0;
        if (null != getLayer()) {
            // If some layer is attached, apply the right translation given from scroll state
            final double sh = getHorizontalScrollRate();
            final double sv = getVerticalScrollRate();
            applyScrollRateToLayer(sh, sv);
        }
    }

    private void synchronizeScrollSize() {
        final double width = calculateInternalScrollPanelWidth();
        final double height = calculateInternalScrollPanelHeight();
        setPanelSize(internalScrollPanel, (int) width, (int) height);
        // TODO getPanel().fireLienzoPanelBoundsChangedEvent();
    }

    private void refreshScrollPosition() {
        final double rx = currentRelativeX();
        final double ry = currentRelativeY();
        setHorizontalScrollRate(rx);
        setVerticalScrollRate(ry);
    }

    private void setHorizontalScrollRate(final double rx) {
        final int scrollWidth = scrollPanel.scrollWidth;
        final int clientWidth = scrollPanel.clientWidth;
        final int max = scrollWidth - clientWidth;
        final double sleft = (max * rx) / 100;
        scrollPanel.scrollLeft = sleft;
    }

    private void setVerticalScrollRate(final double ry) {

        final int scrollHeight = scrollPanel.scrollHeight;
        final int clientHeight = scrollPanel.clientHeight;
        final int max = scrollHeight - clientHeight;
        final double stop = (max * ry) / 100;
        scrollPanel.scrollTop = stop;
    }

    private double calculateInternalScrollPanelWidth() {
        final double absWidth = maxBoundX() - minBoundX();
        if (getViewport() != null && deltaX() != 0) {
            final double scaleX = getViewport().getTransform().getScaleX();
            final double width = absWidth * scaleX;
            return width;
        }
        return 1;
    }

    private double calculateInternalScrollPanelHeight() {
        final double absHeight = maxBoundY() - minBoundY();
        if (getViewport() != null && deltaY() != 0) {
            final double scaleY = getViewport().getTransform().getScaleY();
            final double height = absHeight * scaleY;
            return height;
        }
        return 1;
    }

    public double getHorizontalScrollRate() {
        final double scrollLeft = scrollPanel.scrollLeft;
        final int scrollWidth = scrollPanel.scrollWidth;
        final int clientWidth = scrollPanel.clientWidth;
        final int level = scrollWidth - clientWidth;
        return level == 0 ? 0d : 100d * scrollLeft / level;
    }

    public double getVerticalScrollRate() {
        final double scrollTop = scrollPanel.scrollTop;
        final int scrollHeight = scrollPanel.scrollHeight;
        final int clientHeight = scrollPanel.clientHeight;
        final int level = scrollHeight - clientHeight;
        return level == 0 ? 0d : 100d * scrollTop / level;
    }

    private void updatePanelsSizes(final int widePx,
                                   final int highPx) {
        setPanelSize(scrollPanel, widePx, highPx);
        // TODO: Scrollbar's width/height is actually so... so divs overlap and it's not possible to click on scrollbars
        // final int scrollbarWidth = scrollbarWidth();
        // final int scrollbarHeight = scrollbarHeight();
        final int scrollbarWidth = 10;
        final int scrollbarHeight = 10;
        final int w = widePx - scrollbarWidth;
        final int h = highPx - scrollbarHeight;
        DomGlobal.console.log("Ext panel [" + widePx + ", " + highPx + "]");
        DomGlobal.console.log("Int panel [" + w + ", " + h + "]");
        setPanelSize(domElementContainer, w, h);
        getLienzoPanel().setPixelSize(w, h);
    }

    private int scrollbarWidth() {
        return scrollPanel.offsetWidth - scrollPanel.clientWidth;
    }

    private int scrollbarHeight() {
        return scrollPanel.offsetHeight - scrollPanel.clientHeight;
    }

    public void applyScrollRateToLayer(final double px,
                                       final double py) {
        final double cx = currentPositionX(px);
        final double cy = currentPositionY(py);
        final Transform oldTransform = getViewport().getTransform();
        final double dx = cx - (oldTransform.getTranslateX() / oldTransform.getScaleX());
        final double dy = cy - (oldTransform.getTranslateY() / oldTransform.getScaleY());
        final Transform newTransform = oldTransform.copy().translate(dx, dy);
        getViewport().setTransform(newTransform);
        getLayer().batch();
        // TODO getPanel().fireLienzoPanelScrollEvent(percentageX, percentageY);
    }

    // -- Scroll Bounds --
    // -----------------------

    private double maxBoundX() {
        return maxBoundX(getBounds());
    }

    private double maxBoundY() {
        return maxBoundY(getBounds());
    }

    private double minBoundX() {
        return minBoundX(getBounds());
    }

    private double minBoundY() {
        return minBoundY(getBounds());
    }

    // -- Scroll Position--
    // -----------------------

    private double currentRelativeX() {

        final double delta = deltaX();

        return delta == 0d ? 0d : 100 * currentX() / delta;
    }

    private double currentRelativeY() {

        final double delta = deltaY();

        return delta == 0d ? 0d : 100 * currentY() / delta;
    }

    private double currentPositionX(final Double level) {

        final double position = deltaX() * level / 100;

        return -(minBoundX() + position);
    }

    private double currentPositionY(final Double level) {

        final double position = deltaY() * level / 100;

        return -(minBoundY() + position);
    }

    private double deltaX() {
        return maxBoundX() - minBoundX() - getVisibleBounds().getWidth();
    }

    private double deltaY() {
        return maxBoundY() - minBoundY() - getVisibleBounds().getHeight();
    }

    private Double currentX() {
        return -(getTransform().getTranslateX() / getTransform().getScaleX() + minBoundX());
    }

    private Double currentY() {
        return -(getTransform().getTranslateY() / getTransform().getScaleY() + minBoundY());
    }

    // -- Helper methods --
    // -----------------------

    private static double maxBoundX(Bounds bounds) {
        final double value = bounds.getX() + bounds.getWidth();
        return max(value);
    }

    private static double maxBoundY(Bounds bounds) {
        final double value = bounds.getY() + bounds.getHeight();
        return max(value);
    }

    private static double minBoundX(Bounds bounds) {
        final double value = bounds.getX();
        return min(value);
    }

    private static double minBoundY(Bounds bounds) {
        final double value = bounds.getY();
        return min(value);
    }

    private static double max(double value) {
        return value >= 0 ? value : 0d;
    }

    private static double min(double value) {
        return value > 0 ? 0d : value;
    }
}
