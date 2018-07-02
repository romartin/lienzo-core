/*
   Copyright (c) 2017 Ahome' Innovation Technologies. All rights reserved.

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
// TODO - review DSJ

package com.ait.lienzo.client.core.shape.wires;

import java.util.Map;
import java.util.Objects;

import com.ait.lienzo.client.core.event.IAttributesChangedBatcher;
import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.wires.MagnetManager.Magnets;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeEndEvent;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeEndHandler;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeStartEvent;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeStartHandler;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeStepEvent;
import com.ait.lienzo.client.core.shape.wires.event.WiresResizeStepHandler;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresShapeControl;
import com.ait.lienzo.client.core.shape.wires.handlers.impl.WiresShapeHandler;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.shared.core.types.EventPropagationMode;
import com.ait.tooling.nativetools.client.event.HandlerRegistrationManager;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

public class WiresShape extends WiresContainer
{

    private final MultiPath                   m_drawnObject;

    private final LayoutContainer             m_innerLayoutContainer;

    private Magnets                     m_magnets;

    private WiresShapeControlHandleList m_ctrls;

    private boolean                     m_resizable;

    private WiresShapeControl           m_control;

    public WiresShape(final MultiPath path)
    {
        this(path, new WiresLayoutContainer());
    }

    public WiresShape(final MultiPath path, final LayoutContainer layoutContainer)
    {
        super(layoutContainer.getGroup());
        this.m_drawnObject = path;
        this.m_innerLayoutContainer = layoutContainer;
        this.m_ctrls = null;
        init();

    }

    WiresShape(final MultiPath path, final LayoutContainer layoutContainer, final HandlerManager manager, final HandlerRegistrationManager registrationManager, final IAttributesChangedBatcher attributesChangedBatcher)
    {
        super(layoutContainer.getGroup(), manager, registrationManager, attributesChangedBatcher);
        this.m_drawnObject = path;
        this.m_ctrls = null;
        this.m_innerLayoutContainer = layoutContainer;
        init();
    }

    @Override
    public WiresShape setLocation(final Point2D p) {
        super.setLocation(p);
        return this;
    }

    public WiresShape addChild(final IPrimitive<?> child)
    {
        m_innerLayoutContainer.add(child);
        return this;
    }

    public WiresShape addChild(final IPrimitive<?> child, final LayoutContainer.Layout layout)
    {
        m_innerLayoutContainer.add(child, layout);
        return this;
    }

    public WiresShape removeChild(final IPrimitive<?> child)
    {
        m_innerLayoutContainer.remove(child);
        return this;
    }

    public WiresShapeControlHandleList getControls()
    {
        return m_ctrls;
    }

    public IControlHandleList loadControls(final IControlHandle.ControlHandleType type)
    {
        _loadControls(type);
        return getControls();
    }

    @Override
    public WiresShape setDraggable(final boolean draggable)
    {
        super.setDraggable(draggable);
        return this;
    }

    public WiresShape setResizable(final boolean resizable)
    {
        this.m_resizable = resizable;
        return this;
    }

    public boolean isResizable()
    {
        return m_resizable;
    }

    /**
     * If the shape's path parts/points have been updated programmatically (not via human events interactions),
     * you can call this method to update the children layouts, controls and magnets.
     * The WiresResizeEvent event is not fired as this method is supposed to be called by the developer.
     */
    public void refresh()
    {
        _loadControls(IControlHandle.ControlHandleStandardType.RESIZE);

        if (null != getControls())
        {
            getControls().refresh();
        }
    }

    public void setControl( final WiresShapeControl control ) {
        m_control = control;
    }

    public WiresShapeControl getControl() {
        return m_control;
    }

    public MultiPath getPath()
    {
        return m_drawnObject;
    }

    public Magnets getMagnets()
    {
        return m_magnets;
    }

    public void setMagnets(Magnets magnets)
    {
        this.m_magnets = magnets;
    }

    public void removeFromParent()
    {
        if (getParent() != null)
        {
            getParent().remove(this);
        }
    }

    public final HandlerRegistration addWiresResizeStartHandler(final WiresResizeStartHandler handler)
    {
        Objects.requireNonNull(handler);

        return getHandlerManager().addHandler(WiresResizeStartEvent.TYPE, handler);
    }

    public final HandlerRegistration addWiresResizeStepHandler(final WiresResizeStepHandler handler)
    {
        Objects.requireNonNull(handler);

        return getHandlerManager().addHandler(WiresResizeStepEvent.TYPE, handler);
    }

    public final HandlerRegistration addWiresResizeEndHandler(final WiresResizeEndHandler handler)
    {
        Objects.requireNonNull(handler);

        return getHandlerManager().addHandler(WiresResizeEndEvent.TYPE,
                                              new WiresResizeEndHandler() {
                                                  @Override
                                                  public void onShapeResizeEnd(WiresResizeEndEvent event) {
                                                      handler.onShapeResizeEnd(event);
                                                      m_innerLayoutContainer.refresh();
                                                      refresh();
                                                  }
                                              });
    }

    public String uuid()
    {
        return getGroup().uuid();
    }

    private void init()
    {
        m_resizable = true;

        m_innerLayoutContainer.getGroup().setEventPropagationMode(EventPropagationMode.FIRST_ANCESTOR);

        m_innerLayoutContainer.add(getPath());

        BoundingBox box = getPath().refresh().getBoundingBox();

        m_innerLayoutContainer.setOffset(new Point2D(box.getX(), box.getY())).setSize(box.getWidth(), box.getHeight()).execute();
    }

    private void _loadControls(final IControlHandle.ControlHandleType type)
    {
        if (null != getControls())
        {
            this.getControls().destroy();

            this.m_ctrls = null;
        }

        Map<IControlHandle.ControlHandleType, IControlHandleList> handles = getPath().getControlHandles(type);

        if (null != handles)
        {
            IControlHandleList controls = handles.get(type);

            if ((null != controls) && (controls.isActive()))
            {
                this.m_ctrls = createControlHandles(type, (ControlHandleList) controls);
            }
        }
    }

    protected WiresShapeControlHandleList createControlHandles(IControlHandle.ControlHandleType type, ControlHandleList controls)
    {
        return new WiresShapeControlHandleList(this, type, controls);
    }

    @Override
    public void shapeMoved() {
        super.shapeMoved();
        if (getMagnets() != null)
        {
            getControl().getMagnetsControl().shapeMoved();
        }

    }

    @Override
    public void destroy()
    {
        super.destroy();

        removeFromParent();

        m_innerLayoutContainer.destroy();

        if (null != getControls())
        {
            getControls().destroy();
            m_ctrls = null;
        }

        if (null != getMagnets())
        {
            getMagnets().destroy();
            m_magnets = null;
        }

        if (null != getControl())
        {
            getControl().reset();
            m_control = null;
        }
    }

    LayoutContainer getLayoutContainer()
    {
        return m_innerLayoutContainer;
    }

    @Override public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }

        WiresShape that = (WiresShape) o;

        return getGroup().uuid() == that.getGroup().uuid();
    }

    @Override public int hashCode()
    {
        return getGroup().uuid().hashCode();
    }
}
