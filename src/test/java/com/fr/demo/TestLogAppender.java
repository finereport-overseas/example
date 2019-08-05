package com.fr.demo;


import com.fr.third.apache.log4j.AppenderSkeleton;
import com.fr.third.apache.log4j.Level;
import com.fr.third.apache.log4j.spi.LoggingEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/18 0018.
 */
public class TestLogAppender extends AppenderSkeleton {
    private List<String> errorList = new ArrayList<String>();

    public TestLogAppender() {
        this.layout = new com.fr.third.apache.log4j.PatternLayout("%d{HH:mm:ss} %t %p [%c] %m%n");
    }

    protected void append(LoggingEvent event) {
        this.subAppend(event);
    }

    public boolean requiresLayout() {
        return true;
    }

    public synchronized void close() {
        if (this.closed) {
            return;
        }
        errorList.clear();
        this.closed = true;

    }

    public void subAppend(LoggingEvent event) {
        Level level = event.getLevel();
        String msg = this.layout.format(event);
        System.out.println(msg);
        if (Level.ERROR.equals(level)) {
            errorList.add(msg);
        }
    }

    public List<String> getErrorList() {
        return errorList;
    }
}

