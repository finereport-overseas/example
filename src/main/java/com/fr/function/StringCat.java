package com.fr.function;

import com.fr.script.AbstractFunction;

public class StringCat extends AbstractFunction {
    /**
     * The entrance of StringCat()
     * @param args the arguments in StringCat()
     * @return the calculated result
     */
    public Object run(Object[] args) {
        StringBuilder stringBuilder = new StringBuilder();
        Object para;
        for (int i = 0; i < args.length; i++) {
            para = args[i];
            // Transfer the parameter to String, and concatenate
            stringBuilder.append(para.toString());
        }
        return stringBuilder.toString();
    }
}