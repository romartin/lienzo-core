package org.roger600.lienzo.client;

import jsinterop.annotations.JsFunction;
import jsinterop.annotations.JsMethod;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

// goog.module.get('SomeJob$impl')
@JsType(isNative = false, namespace = JsPackage.GLOBAL)
public class SomeJob {

    @JsFunction
    interface RunCallback {

        void run(String s);
    }

    @JsFunction
    interface Callback {

        void run();
    }

    @JsMethod(namespace = JsPackage.GLOBAL)
    private static native int setTimeout(Callback callback, int delayMs);

    public static void run(RunCallback callback) {
        callback.run("Hello from SomeJob start");
        fibonacci(40, callback);
        callback.run("Hello from SomeJob end");
    }

    public static int fibonacci(int num, RunCallback callback) {
        // callback.run("" + num);
        return (num <= 1) ? 1 : fibonacci(num - 1, callback) + fibonacci(num - 2, callback);
    }
}
