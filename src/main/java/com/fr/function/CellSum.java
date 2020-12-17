package com.fr.function;

import com.fr.base.Utils;
import com.fr.script.AbstractFunction;

public class CellSum extends AbstractFunction {
    public Object run(Object[] args) {
        // invoke the embedded sum method
        String sum = Utils.objectToNumber(new SUM().run(args), false)
                .toString();
        String result = "The cell is: " + this.getCalculator().getCurrentColumnRow()
                + ". Sum is: " + sum;
        return result;
    }
}