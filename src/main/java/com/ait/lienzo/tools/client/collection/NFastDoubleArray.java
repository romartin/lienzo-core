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

package com.ait.lienzo.tools.client.collection;

import elemental2.core.JsArray;
import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;
import jsinterop.base.Js;
import jsinterop.base.JsArrayLike;

@JsType(isNative = true, name = "Array", namespace = JsPackage.GLOBAL)
public class NFastDoubleArray extends NArrayBase<Double>
        //extends NFastPrimitiveArrayBase<NFastDoubleArray, NFastDoubleArrayJSO>
{
//    public NFastDoubleArray(final NFastDoubleArrayJSO jso)
//    {
//        super((null == jso) ? NFastDoubleArrayJSO.makeFromString() : jso);
//    }

    public NFastDoubleArray()
    {
        //super(NFastDoubleArrayJSO.makeFromString());
    }

    @JsOverlay
    public static final NFastDoubleArray makeFromDoubles(double... list) {
        NFastDoubleArray array = new NFastDoubleArray();
        for ( double d : list)
        {
            array.push(d);
        }

        return array;
    }


    @JsOverlay
    public static final NFastDoubleArray make2P(final double i, final double i1)
    {
        NFastDoubleArray array = new NFastDoubleArray();
        array.push( i, i1);
        return array;
    }

    /**
     * Return the primitive found at the specified index.
     * @param index
     * @return
     */
    @JsOverlay
    public final Double get(final int index)
    {
        // @TODO is this really necessary to incurr the cost to be defensive. Surely better to avoid bad code in the first place? (mdp)
        if ((index >= 0) && (index < size()))
        {
            return getAt(index);
        }
        return null;
    }

    /**
     * Add a value to the List
     * @param value
     */
    @JsOverlay
    public final NFastDoubleArray add(final Double value)
    {
        push(value);

        return this;
    }

    /**
     * Add a value to the List
     * @param value
     */
    @JsOverlay
    public final NFastDoubleArray set(final int i, final Double value)
    {
        setAt(i, value);

        return this;
    }

    /**
     * Return true if the List contains the passed in value.
     *
     * @param value
     * @return boolean
     */
    @JsOverlay
    public final boolean contains(final Double value)
    {
        return indexOf(value) > 0;
    }

    public native int indexOf(Double obj);

    @JsOverlay
    public final double[] toArray()
    {
        // yes this is horrible, but JS doesn't differentiate between Double and double and it avoids an array copy.
        double[] d = Js.uncheckedCast(JsArray.from((JsArrayLike<Double>) this));
        return d;
//        final int size = size();
//
//        final double[] array = new double[size];
//
//        for (int i = 0; i < size; i++)
//        {
//            array[i] = get(i);
//        }
//        return array;
    }
}
