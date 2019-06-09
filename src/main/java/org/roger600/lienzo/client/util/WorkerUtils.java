package org.roger600.lienzo.client.util;

import elemental2.dom.Blob;
import elemental2.dom.Blob.ConstructorBlobPartsArrayUnionType;
import elemental2.dom.BlobPropertyBag;
import elemental2.dom.Worker;

public class WorkerUtils {

    public static Worker runWorker(String jsScript,
                                   Object message) {
        Worker worker = createWorker(jsScript);
        worker.postMessage(message);
        return worker;
    }

    public static Worker createWorker(String jsScript) {
        BlobPropertyBag blobPropertyBag = BlobPropertyBag.create();
        blobPropertyBag.setType("text/javascript");
        String s = getImportScript() + jsScript;
        String url = URL.createObjectURL(new Blob(new ConstructorBlobPartsArrayUnionType[]{ConstructorBlobPartsArrayUnionType.of(s)},
                                                  blobPropertyBag));
        return new Worker(url);
    }

    private static String getImportScript() {
        return "var CLOSURE_BASE_PATH = \"http://127.0.0.1:8080/\";"
                + "importScripts(CLOSURE_BASE_PATH + 'app.js');";
    }
}
