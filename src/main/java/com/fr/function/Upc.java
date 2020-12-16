package com.fr.function;

import com.fr.script.AbstractFunction;
import org.krysalis.barcode4j.impl.upcean.UPCABean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;
import org.krysalis.barcode4j.tools.UnitConv;

import java.awt.image.BufferedImage;

public class Upc extends AbstractFunction {
    public Object run(Object[] args) {
        if (args == null || args.length < 2) {
            return "The number of arguments should be 2.";
        }
        try {
            // create a UPC generator
            UPCABean bean = new UPCABean();
            // set the height of barcode
            final int dpi = Integer.parseInt(args[1].toString());
            bean.setModuleWidth(UnitConv.in2mm(1.0f / dpi));
            bean.doQuietZone(false);
            BitmapCanvasProvider canvas = new BitmapCanvasProvider(dpi,
                    BufferedImage.TYPE_BYTE_BINARY, false, 0);
            // generate the barcode
            bean.generateBarcode(canvas, String.valueOf(args[0]));
            canvas.finish();
            // return the image
            return canvas.getBufferedImage();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return args[0];
    }
}