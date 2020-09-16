package com.fr.log;


public class LogApi {
    public static void main(String[] args) {
        FineLoggerFactory.getLogger().info( "This is level info");    //display when server log level is info
        FineLoggerFactory.getLogger().warn("This is level warning");   //display when server log level is info,warning
        FineLoggerFactory.getLogger().error("This is level error");   //display when server log level is info,warning,error,10.0 cancelled server level log
    }
}
