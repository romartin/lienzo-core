/*
   Copyright (c) 2014,2015,2016 Ahome' Innovation Technologies. All rights reserved.

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

package com.ait.lienzo.client.core.shape;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.Predicate;

import com.ait.lienzo.client.core.Attribute;
import com.ait.lienzo.client.core.Context2D;
import com.ait.lienzo.client.core.shape.json.IContainerFactory;
import com.ait.lienzo.client.core.shape.json.IJSONSerializable;
import com.ait.lienzo.client.core.shape.json.JSONDeserializer;
import com.ait.lienzo.client.core.shape.json.validators.ValidationContext;
import com.ait.lienzo.client.core.shape.json.validators.ValidationException;
import com.ait.lienzo.client.core.shape.storage.IStorageEngine;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.lienzo.client.core.types.BoundingPoints;
import com.ait.lienzo.shared.core.types.NodeType;
import com.ait.tooling.nativetools.client.collection.NFastArrayList;
import com.google.gwt.json.client.JSONObject;

/**
 * ContainerNode acts as a Collection holder for primitives.
 * 
 * <ul>
 * <li>A ContainerNode may contain {@link Layer} or {@link Group}.</li>
 * <li>A Container handles collection operations such as add, remove and removeAll.</li>
 * </ul>
 * 
 * @param <T>
 */
public abstract class ContainerNode<M extends IDrawable<?>, T extends ContainerNode<M, T>>extends Node<T>implements IContainer<T, M>
{
    private BoundingBox       m_bbox;

    private IPathClipper      m_clip;

    private IStorageEngine<M> m_stor;

    protected ContainerNode(final NodeType type, final IStorageEngine<M> storage)
    {
        super(type);

        setStorageEngine(storage);
    }

    protected ContainerNode(final NodeType type, final JSONObject node, final ValidationContext ctx) throws ValidationException
    {
        super(type, node, ctx);
    }

    @Override
    public T copy()
    {
        final Node<?> node = copyUnchecked();

        if (null == node)
        {
            return null;
        }
        if (getNodeType() != node.getNodeType())
        {
            return null;
        }
        return node.cast();
    }

    /**
     * Returns a {@link NFastArrayList} containing all children.
     */
    @Override
    public NFastArrayList<M> getChildNodes()
    {
        return getStorageEngine().getChildren();
    }

    @Override
    public NFastArrayList<M> getChildNodes(final BoundingBox bounds)
    {
        return getStorageEngine().getChildren(bounds);
    }

    @Override
    public int length()
    {
        return getStorageEngine().size();
    }

    @Override
    public IStorageEngine<M> getStorageEngine()
    {
        if (null == m_stor)
        {
            m_stor = getDefaultStorageEngine();
        }
        return m_stor;
    }

    @Override
    public T setStorageEngine(final IStorageEngine<M> storage)
    {
        if ((null != storage) && (null != m_stor))
        {
            storage.migrate(m_stor);
        }
        m_stor = storage;

        return cast();
    }

    @Override
    public T setStorageBounds(BoundingBox bounds)
    {
        m_bbox = bounds;

        return cast();
    }

    @Override
    public BoundingBox getStorageBounds()
    {
        return m_bbox;
    }

    /**
     * Adds a primitive to the collection.
     * <p>
     * It should be noted that this operation will not have an apparent effect for an already rendered (drawn) Container.
     * In other words, if the Container has already been drawn and a new primitive is added, you'll need to invoke draw() on the
     * Container. This is done to enhance performance, otherwise, for every add we would have draws impacting performance.
     */
    @Override
    public T add(final M child)
    {
        final Node<?> node = child.asNode();

        node.setParent(this);

        getStorageEngine().add(child);

        return cast();
    }

    /**
     * Removes a primitive from the container.
     * <p>
     * It should be noted that this operation will not have an apparent effect for an already rendered (drawn) Container.
     * In other words, if the Container has already been drawn and a new primitive is added, you'll need to invoke draw() on the
     * Container. This is done to enhance performance, otherwise, for every add we would have draws impacting performance.
     */
    @Override
    public T remove(final M child)
    {
        final Node<?> node = child.asNode();

        node.setParent(null);

        getStorageEngine().remove(child);

        return cast();
    }

    /**
     * Removes all primitives from the collection.
     * <p>
     * It should be noted that this operation will not have an apparent effect for an already rendered (drawn) Container.
     * In other words, if the Container has already been drawn and a new primitive is added, you'll need to invoke draw() on the
     * Container. This is done to enhance performance, otherwise, for every add we would have draws impacting performance.
     */
    @Override
    public T removeAll()
    {
        getStorageEngine().clear();

        return cast();
    }

    /**
     * Used internally. Draws the node in the current Context2D
     * without applying the transformation-related attributes 
     * (e.g. X, Y, ROTATION, SCALE, SHEAR, OFFSET and TRANSFORM.)
     * <p> 
     * Groups should draw their children in the current context.
     */
    @Override
    protected void drawWithoutTransforms(final Context2D context, double alpha, final BoundingBox bounds)
    {
        if ((context.isSelection()) && (false == isListening()))
        {
            return;
        }
        alpha = alpha * getAttributes().getAlpha();

        if (alpha <= 0)
        {
            return;
        }
        BoundingBox bbox = getStorageBounds();

        if (null == bbox)
        {
            bbox = bounds;
        }
        final NFastArrayList<M> list = getChildNodes(bbox);

        final int size = list.size();

        final IPathClipper clip = getPathClipper();

        if ((null != clip) && (clip.isActive()))
        {
            context.save();

            clip.clip(context);

            for (int i = 0; i < size; i++)
            {
                list.get(i).drawWithTransforms(context, alpha, bbox);
            }
            context.restore();
        }
        else
        {
            for (int i = 0; i < size; i++)
            {
                list.get(i).drawWithTransforms(context, alpha, bbox);
            }
        }
    }

    @Override
    public BoundingBox getBoundingBox()
    {
        final BoundingBox bbox = new BoundingBox();

        final NFastArrayList<M> list = getChildNodes();

        final int size = list.size();

        for (int i = 0; i < size; i++)
        {
            final BoundingPoints bpts = list.get(i).getBoundingPoints();

            if (null != bpts)
            {
                bbox.add(bpts.getArray());
            }
        }
        return bbox;
    }

    // TODO - do this for containers

    @Override
    public List<Attribute> getBoundingBoxAttributes()
    {
        return new ArrayList<Attribute>(0);
    }

    /**
     * Moves the {@link Layer} up
     */
    @Override
    public T moveUp(final M node)
    {
        getStorageEngine().moveUp(node);

        return cast();
    }

    /**
     * Moves the {@link Layer} down
     */
    @Override
    public T moveDown(final M node)
    {
        getStorageEngine().moveDown(node);

        return cast();
    }

    /**
     * Moves the {@link Layer} to the top
     */
    @Override
    public T moveToTop(final M node)
    {
        getStorageEngine().moveToTop(node);

        return cast();
    }

    /**
     * Moves the {@link Layer} to the bottom
     */
    @Override
    public T moveToBottom(final M node)
    {
        getStorageEngine().moveToBottom(node);

        return cast();
    }

    @Override
    public final Iterable<Node<?>> findByID(String id)
    {
        if ((null == id) || ((id = id.trim()).isEmpty()))
        {
            return new ArrayList<Node<?>>(0);
        }
        final String look = id;

        return find(new Predicate<Node<?>>()
        {
            @Override
            public boolean test(Node<?> node)
            {
                if (null == node)
                {
                    return false;
                }
                String id = node.getAttributes().getID();

                if ((null != id) && (false == (id = id.trim()).isEmpty()))
                {
                    return id.equals(look);
                }
                return false;
            }
        });
    }

    abstract protected void find(Predicate<Node<?>> predicate, LinkedHashSet<Node<?>> buff);

    @Override
    public final Iterable<Node<?>> find(final Predicate<Node<?>> predicate)
    {
        final LinkedHashSet<Node<?>> buff = new LinkedHashSet<Node<?>>();

        find(predicate, buff);

        return buff;
    }

    @Override
    public T setPathClipper(final IPathClipper clipper)
    {
        m_clip = clipper;

        return cast();
    }

    @Override
    public IPathClipper getPathClipper()
    {
        return m_clip;
    }

    @Override
    public ContainerNode<?, ?> asContainerNode()
    {
        return this;
    }

    @Override
    public IContainer<?, ?> asContainer()
    {
        return this;
    }

    public static abstract class ContainerNodeFactory<C extends IJSONSerializable<C> & IContainer<C, ?>>extends NodeFactory<C>implements IContainerFactory
    {
        protected ContainerNodeFactory(final NodeType type)
        {
            this(type.getValue());
        }

        protected ContainerNodeFactory(final String typeName)
        {
            super(typeName);
        }

        protected abstract C container(JSONObject node, ValidationContext ctx) throws ValidationException;

        @Override
        public C create(final JSONObject node, final ValidationContext ctx) throws ValidationException
        {
            final C container = container(node, ctx);

            JSONDeserializer.get().deserializeChildren(container, node, this, ctx);

            return container;
        }
    }
}