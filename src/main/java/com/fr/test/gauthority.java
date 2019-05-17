package com.fr.test;

import com.fr.base.FRContext;
import com.fr.base.Formula;
import com.fr.general.FArray;
import com.fr.json.JSONObject;
import com.fr.script.AbstractFunction;
import com.fr.script.Calculator;
import com.fr.stable.Primitive;

public class gauthority extends AbstractFunction {
    public gauthority() {
    }

    public Object run(Object[] args) {
        int[] newArgs = new int[args.length];

        for (int i = 0; i < args.length; ++i) {
            if (!(args[i] instanceof Integer) || (Integer) args[i] <= 0) {
                return Primitive.ERROR_NAME;
            }

            newArgs[i] = (Integer) args[i];
        }

        FArray res = new FArray();
        Calculator ca = this.getCalculator();
        Formula f = new Formula("$fr_userposition");

        try {
            Object dp = ca.eval(f);
            if (dp instanceof FArray) {
                FArray fa = (FArray) dp;

                for (int i = 0; i < fa.length(); ++i) {
                    JSONObject jo = (JSONObject) fa.elementAt(i);
                    String dName = jo.getString("jobTitle");
                    if (newArgs.length == 0) {
                        res.add(dName);
                    } else {
                        String[] dNames = dName.split(",");
                        res.add(this.buildRes(dNames, newArgs));
                    }
                }
            }
        } catch (Exception var12) {
            FRContext.getLogger().error(var12.getMessage(), var12);
        }

        return res;
    }

    private String buildRes(String[] dNames, int[] args) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < args.length; ++i) {
            int index = args[i];
            if (dNames.length >= index) {
                sb.append(dNames[index - 1]).append(",");
            }
        }

        return sb.substring(0, sb.length() > 0 ? sb.length() - 1 : 0);
    }

    public Type getType() {
        return OTHER;
    }

    public String getCN() {
        return "GETUSERDEPARTMENTS():返回角色部门\n示例:\nGETUSERDEPARTMENTS():返回角色所有部门，若多个部门则数组\nGETUSERDEPARTMENTS(3,2):返回角色该部门的第三层和第二层名字，\n若多个部门则返回数组，若没有第三层则只显示第二层";
    }

    public String getEN() {
        return "";
    }
}
