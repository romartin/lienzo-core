package org.roger600.lienzo.client.util;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

@JsType(isNative = true, namespace = JsPackage.GLOBAL)
public class URL {

    public static native String createObjectURL(Object o);
}
