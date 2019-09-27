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

package com.ait.lienzo.client.core.palette;

import com.ait.lienzo.client.core.config.LienzoCore;
import com.ait.lienzo.client.core.shape.json.AbstractFactory;
import com.ait.lienzo.client.core.shape.json.IFactory;
import com.ait.lienzo.client.core.shape.json.IJSONSerializable;
import com.ait.lienzo.client.core.shape.json.validators.ValidationContext;
import com.ait.lienzo.client.core.shape.json.validators.ValidationException;
import com.ait.lienzo.shared.core.types.PaletteType;
import com.ait.lienzo.tools.client.collection.MetaData;

public abstract class AbstractPaletteBase<T extends AbstractPaletteBase<T>> implements IJSONSerializable<T>
{
    private final MetaData    m_meta;

    private final PaletteType m_type;

    protected AbstractPaletteBase(final PaletteType type)
    {
        m_type = type;

        m_meta = new MetaData();
    }


    protected AbstractPaletteBase(final PaletteType type, final Object node, final ValidationContext ctx) throws ValidationException
    {
        // @FIXME re-enable htis later (mdp)
        m_type = type;
//
//        if (null == node)
//        {
            // m_attr = new Attributes(this);

            m_meta = new MetaData();
//
//            return;
//        }
//        JSONValue aval = node.get("attributes");
//
//        if (null == aval)
//        {
//            m_attr = new Attributes(this);
//        }
//        else
//        {
//            JSONObject aobj = aval.isObject();
//
//            if (null == aobj)
//            {
//                m_attr = new Attributes(this);
//            }
//            else
//            {
//                JavaScriptObject ajso = aobj.getJavaScriptObject();
//
//                if (null == ajso)
//                {
//                    m_attr = new Attributes(this);
//                }
//                else
//                {
//                    m_attr = new Attributes(ajso, this);
//                }
//            }
//        }
//        JSONValue mval = node.get("meta");
//
//        if (null == mval)
//        {
//            m_meta = new MetaData();
//        }
//        else
//        {
//            JSONObject mobj = mval.isObject();
//
//            if (null == mobj)
//            {
//                m_meta = new MetaData();
//            }
//            else
//            {
//                JavaScriptObject mjso = mobj.getJavaScriptObject();
//
//                if (null == mjso)
//                {
//                    m_meta = new MetaData();
//                }
//                else
//                {
//                    NObjectJSO jso = mjso.cast();
//
//                    m_meta = new MetaData(jso);
//                }
//            }
//        }
    }

    public final MetaData getMetaData()
    {
        return m_meta;
    }

    // @FIXME serialisation (mdp)
    @Override
    public final String toJSONString()
    {
        return null; //toJSONObject().toString();
    }

//    @Override
//    public JSONObject toJSONObject()
//    {
//        JSONObject object = new JSONObject();
//
//        object.put("type", new JSONString(m_type.getValue()));
//
//        if (false == getMetaData().isEmpty())
//        {
//            // @FIXME (mdp)
//            // object.putString("meta", new JSONObject(getMetaData().getJSO()));
//        }
//        //object.putString("attributes", new JSONObject(getAttributes().getJSO()));
//
//        return object;
//    }

    @Override
    public IFactory<?> getFactory()
    {
        return LienzoCore.get().getFactory(m_type);
    }

    protected abstract static class AbstractPalettebaseFactory<T extends AbstractPaletteBase<T>> extends AbstractFactory<T>
    {
        protected AbstractPalettebaseFactory(final PaletteType type)
        {
            super(type.getValue());
        }
    }
}
