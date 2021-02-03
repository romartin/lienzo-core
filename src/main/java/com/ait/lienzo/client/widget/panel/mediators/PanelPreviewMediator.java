/*
 * Copyright 2019 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ait.lienzo.client.widget.panel.mediators;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.ait.lienzo.client.core.mediator.MouseBoxZoomMediator;
import com.ait.lienzo.client.core.shape.Rectangle;
import com.ait.lienzo.client.core.shape.Viewport;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.client.core.types.Transform;
import com.ait.lienzo.client.widget.LienzoPanelImpl;
import com.ait.lienzo.client.widget.panel.Bounds;
import com.ait.lienzo.client.widget.panel.LienzoBoundsPanel;
import com.ait.lienzo.client.widget.panel.impl.PreviewLayer;
import com.ait.lienzo.client.widget.panel.impl.ScrollablePanel;
import com.ait.lienzo.client.widget.panel.util.PanelTransformUtils;
import com.ait.lienzo.shared.core.types.ColorName;
import elemental2.dom.CSSStyleDeclaration;
import elemental2.dom.DomGlobal;
import elemental2.dom.HTMLDivElement;

public class PanelPreviewMediator extends AbstractPanelMediator<PanelPreviewMediator> {

    private static final double PREVIEW_AREA_PADDING = 50d;
    private static final double PREVIEW_AREA_LOCATION = 250d;
    private static final double PREVIEW_AREA_SIZE = 350d;
    static final String PREVIEW_BG_COLOR = "#FFFFFF";

    private final MouseBoxZoomMediator mediator;
    private final Consumer<HTMLDivElement> divElementConsumer;
    private final LienzoPanelImpl previewPanel;
    private final PreviewLayer previewLayer;
    private Supplier<BoundingBox> area;
    private double maxScale;

    public static PanelPreviewMediator build(final ScrollablePanel panel) {
        return new PanelPreviewMediator(() -> panel, previewPanel -> panel.getDomElementContainer().appendChild(previewPanel))
                .setArea(() -> BoundingBox.fromDoubles(PREVIEW_AREA_PADDING,
                                                       PREVIEW_AREA_PADDING,
                                                       panel.getElement().offsetWidth - PREVIEW_AREA_PADDING,
                                                       panel.getElement().offsetHeight - PREVIEW_AREA_PADDING));
    }

    public PanelPreviewMediator(final Supplier<LienzoBoundsPanel> panelSupplier,
                                final Consumer<HTMLDivElement> previewPanel) {
        this(panelSupplier,
             previewPanel,
             () -> new LienzoPanelImpl((HTMLDivElement) DomGlobal.document.createElement("div"), 1, 1));
    }

    public PanelPreviewMediator(final Supplier<LienzoBoundsPanel> panelSupplier,
                                final Consumer<HTMLDivElement> divElementConsumer,
                                final Supplier<LienzoPanelImpl> previewPanelBuilder) {
        super(panelSupplier);
        this.divElementConsumer = divElementConsumer;
        this.previewPanel = previewPanelBuilder.get();
        this.area = () -> BoundingBox.fromDoubles(PREVIEW_AREA_LOCATION,
                                                  PREVIEW_AREA_LOCATION,
                                                  PREVIEW_AREA_LOCATION + PREVIEW_AREA_SIZE,
                                                  PREVIEW_AREA_LOCATION + PREVIEW_AREA_SIZE);
        this.previewLayer = new PreviewLayer(getBackgroundBounds(), getVisibleBounds());
        this.mediator = new MouseBoxZoomMediator();
        this.maxScale = Double.MAX_VALUE;
        init();
    }

    public PanelPreviewMediator setArea(final Supplier<BoundingBox> box) {
        this.area = box;
        return this;
    }

    public PanelPreviewMediator setMaxScale(final double maxScale) {
        this.maxScale = maxScale;
        return this;
    }

    @Override
    protected void onEnable() {
        final LienzoBoundsPanel panel = getPanel();
        final int panelWidthPx = panel.getWidePx();
        final int panelHeightPx = panel.getHighPx();
        final CSSStyleDeclaration style = previewPanel.getElement().style;
        style.position = "absolute";
        style.top = 0 + "px";
        style.left = 0 + "px";
        style.borderStyle = "none";
        style.backgroundColor = PREVIEW_BG_COLOR;
        previewPanel.setPixelSize(panelWidthPx, panelHeightPx);
        previewPanel.setVisible(true);

        final BoundingBox areaBox = area.get();
        final double fitLevel = PanelTransformUtils.computeZoomLevelFitToWidth(areaBox.getWidth(),
                                                                               areaBox.getHeight(),
                                                                               panel);

        final Viewport viewport = previewLayer.getViewport();
        final Transform transform = new Transform();
        transform.translate(areaBox.getX(), areaBox.getY());
        transform.scale(fitLevel);
        viewport.setTransform(transform);

        getLayer().drawWithTransforms(previewLayer.getContext(),
                                      1,
                                      BoundingBox.fromDoubles(0, 0, panelWidthPx, panelHeightPx),
                                      () -> transform);

        mediator.setMaxScale(maxScale);
        mediator.setEnabled(true);
        getLayer().setVisible(false);
    }

    @Override
    public void onDisable() {
        mediator.cancel();
        mediator.setEnabled(false);
        previewLayer.clear();
        previewPanel.setPixelSize(1, 1);
        previewPanel.setVisible(false);
        previewLayer.getViewport().setTransform(new Transform());
        getLayer().setVisible(true);
    }

    public boolean isEnabled() {
        return mediator.isEnabled();
    }

    @Override
    public void onRemoveHandler() {
        super.onRemoveHandler();
        mediator.setOnTransform(null);
        previewPanel.removeFromParent();
        area = null;
    }

    private void init() {
        previewLayer.setListening(true).setTransformable(true);
        previewPanel.add(previewLayer);
        mediator.setEnabled(false);
        mediator
                .setRectangle(new Rectangle(1, 1).setStrokeColor(ColorName.RED))
                .setOnTransform(transform -> {
                    getLayer().getViewport().setTransform(transform.copy());
                    disable();
                });
        previewLayer.getViewport().getMediators().push(mediator);
        previewPanel.setVisible(false);

        divElementConsumer.accept(previewPanel.getElement());
    }

    MouseBoxZoomMediator getMediator() {
        return mediator;
    }

    LienzoPanelImpl getPreviewPanel() {
        return previewPanel;
    }

    PreviewLayer getPreviewLayer() {
        return previewLayer;
    }

    private Supplier<Bounds> getVisibleBounds() {
        return () -> {
            final BoundingBox areaBox = area.get();
            return Bounds.build(areaBox.getX(),
                                areaBox.getY(),
                                areaBox.getWidth(),
                                areaBox.getHeight());
        };
    }

    private Supplier<Bounds> getBackgroundBounds() {
        return () -> Bounds.build(0,
                                  0,
                                  getPanel().getWidePx(),
                                  getPanel().getHighPx());
    }
}
