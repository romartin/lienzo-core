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

package com.ait.lienzo.client.core.shape.storage;

import java.util.Collections;
import java.util.Iterator;

import com.ait.lienzo.client.core.shape.json.AbstractFactory;
import com.ait.lienzo.client.core.shape.json.IJSONSerializable;
import com.ait.lienzo.client.core.shape.json.validators.ValidationContext;
import com.ait.lienzo.client.core.shape.json.validators.ValidationException;
import com.ait.lienzo.client.core.types.BoundingBox;
import com.ait.tooling.nativetools.client.NObjectJSO;
import com.ait.tooling.nativetools.client.collection.MetaData;
import com.ait.tooling.nativetools.client.collection.NFastArrayList;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;

public abstract class AbstractStorageEngine<M> implements IStorageEngine<M>
{
    private final MetaData          m_meta;

    private final StorageEngineType m_type;

    protected AbstractStorageEngine(final StorageEngineType type)
    {
        m_type = type;

        m_meta = new MetaData();
    }

    protected AbstractStorageEngine(final StorageEngineType type, final JSONObject node, final ValidationContext ctx) throws ValidationException
    {
        m_type = type;

        final JSONValue mval = node.get("meta");

        if (null == mval)
        {
            m_meta = new MetaData();
        }
        else
        {
            final JSONObject mobj = mval.isObject();

            if (null == mobj)
            {
                m_meta = new MetaData();
            }
            else
            {
                final JavaScriptObject mjso = mobj.getJavaScriptObject();

                if (null == mjso)
                {
                    m_meta = new MetaData();
                }
                else
                {
                    final NObjectJSO jso = mjso.cast();

                    m_meta = new MetaData(jso);
                }
            }
        }
    }

    @Override
    public MetaData getMetaData()
    {
        return m_meta;
    }

    public String toJSONString()
    {
        return toJSONObject().toString();
    }

    @Override
    public JSONObject toJSONObject()
    {
        final JSONObject object = new JSONObject();

        object.put("type", new JSONString(getStorageEngineType().getValue()));

        if (false == getMetaData().isEmpty())
        {
            object.put("meta", new JSONObject(getMetaData().getJSO()));
        }
        return object;
    }

    @Override
    public void migrate(final IStorageEngine<M> storage)
    {
        if (null != storage)
        {
            final NFastArrayList<M> list = storage.getChildren();

            if (null != list)
            {
                final int size = list.size();

                for (int i = 0; i < size; i++)
                {
                    add(list.get(i));
                }
            }
        }
    }

    @Override
    public StorageEngineType getStorageEngineType()
    {
        return m_type;
    }

    @Override
    public Iterator<M> iterator(final BoundingBox bounds)
    {
        return Collections.unmodifiableList(getChildren(bounds).toList()).iterator();
    }

    @Override
    public Iterator<M> iterator()
    {
        return Collections.unmodifiableList(getChildren().toList()).iterator();
    }

    protected static abstract class AbstractStorageEngineFactory<S extends IJSONSerializable<S>> extends AbstractFactory<S>
    {
        protected AbstractStorageEngineFactory(final StorageEngineType type)
        {
            super(type.getValue());
        }
    }
}
