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

import com.ait.lienzo.client.core.event.IAttributesChangedBatcher;
import com.ait.lienzo.client.core.shape.IPrimitive;
import com.ait.lienzo.client.core.shape.MultiPath;
import com.ait.lienzo.client.core.shape.wires.MagnetManager.Magnets;
import com.ait.lienzo.client.core.shape.wires.event.*;
import com.ait.lienzo.client.core.shape.wires.handlers.WiresShapeControl;
import com.ait.lienzo.client.core.shape.wires.layouts.ILayoutContainer;
import com.ait.lienzo.client.core.shape.wires.layouts.impl.AbstractLayoutContainer;
import com.ait.lienzo.client.core.shape.wires.layouts.impl.CardinalLayoutContainer;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.client.core.types.Point2D;
import com.ait.lienzo.shared.core.types.EventPropagationMode;
import com.ait.tooling.nativetools.client.event.HandlerRegistrationManager;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.Map;
import java.util.Objects;

public class WiresShape extends WiresContainer
{

    private MultiPath                   m_drawnObject;

    private Magnets                     m_magnets;

    private AbstractLayoutContainer<?> m_innerILayoutContainer;

    private WiresShapeControlHandleList m_ctrls;

    private boolean                     m_resizable;

    private WiresShapeControl           m_control;

    public WiresShape(final MultiPath path)
    {
        this(path, new CardinalLayoutContainer());
    }

    public WiresShape(final MultiPath path, final AbstractLayoutContainer<?> ILayoutContainer)
    {
        super(ILayoutContainer.asGroup());
        this.m_drawnObject = path;
        this.m_innerILayoutContainer = ILayoutContainer;
        this.m_ctrls = null;
        init();

    }

    WiresShape(final MultiPath path, final AbstractLayoutContainer<?> ILayoutContainer, final HandlerManager manager, final HandlerRegistrationManager registrationManager, final IAttributesChangedBatcher attributesChangedBatcher)
    {
        super(ILayoutContainer.asGroup(), manager, registrationManager, attributesChangedBatcher);
        this.m_drawnObject = path;
        this.m_ctrls = null;
        this.m_innerILayoutContainer = ILayoutContainer;
        init();
    }

    @Override
    public WiresShape setLocation(final Point2D p) {
        super.setLocation(p);
        return this;
    }

    public WiresShape addChild(final IPrimitive<?> child)
    {
        m_innerILayoutContainer.add(child);
        return this;
    }

    public WiresShape removeChild(final IPrimitive<?> child)
    {
        m_innerILayoutContainer.remove(child);
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

    void setWiresShapeControl( final WiresShapeControl control ) {
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
                                                      m_innerILayoutContainer.refresh();
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

        m_innerILayoutContainer.forBoundingBox(new ILayoutContainer.BoundingBoxSupplier() {
            @Override
            public BoundingBox get() {
                return WiresShape.this.getPath().refresh().getBoundingBox();
            }
        });

        m_innerILayoutContainer.asGroup().setEventPropagationMode(EventPropagationMode.FIRST_ANCESTOR);

        m_innerILayoutContainer.add(getPath());

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

    protected void preDestroy()
    {
        super.preDestroy();
        // TODO m_innerILayoutContainer.destroy();
        removeHandlers();
        removeFromParent();
    }

    private void removeHandlers()
    {
        if (null != getControls())
        {
            getControls().destroy();
        }
    }

    public ILayoutContainer getLayoutContainer()
    {
        return m_innerILayoutContainer;
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
