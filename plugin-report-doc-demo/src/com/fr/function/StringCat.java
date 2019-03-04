package com.fr.function;

import com.fr.script.AbstractFunction;

public class StringCat extends AbstractFunction {
    public StringCat() {
    }

    public Object run(Object[] args) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < args.length; ++i) {
            Object para = args[i];
            result.append(para.toString());
        }

        return result.toString();
    }
}
