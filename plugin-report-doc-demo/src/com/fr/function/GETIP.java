package com.fr.function;

import com.fr.script.AbstractFunction;

import java.net.InetAddress;

public class GETIP extends AbstractFunction {

    @Override
    public Object run(Object[] objects) {
        try {
            InetAddress ia = InetAddress.getLocalHost();
            return ia.getHostAddress();
        } catch (Exception e) {
            return e.getMessage();

        }
    }

    public static InetAddress getInetAddress() {
        return null;
    }
}