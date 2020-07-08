package com.fr.output;

import com.fr.io.utils.ResourceIOUtils;
import com.fr.schedule.base.bean.output.OutputClass;
import com.fr.schedule.base.constant.ScheduleConstants;
import com.fr.schedule.feature.output.OutputActionHandler;
import com.fr.stable.ArrayUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

/**
 * Created by Zed on 2018/9/11.
 */
public class OutputExcel extends OutputActionHandler<OutputClass> {

    @Override
    public void doAction(OutputClass action, Map<String, Object> map) throws Exception {
        String[] files = (String[]) map.get(ScheduleConstants.OUTPUT_FILES);
        if (ArrayUtils.isNotEmpty(files)) {
            for (String path : files) {
                output(path);
            }
        }
    }

    private void output(String path) {

        InputStream in = ResourceIOUtils.read(path);
        OutputStream out = null;

        if (in == null) {
            // 读文件失败
            return;
        }

        try {
            // 写文件到本地磁盘
            String newPath = "C:/test/" + ResourceIOUtils.getName(path);
            out = new BufferedOutputStream(new FileOutputStream(new File(newPath)));
            in = new BufferedInputStream(in);
            byte[] ba = new byte[in.available()];
            in.read(ba);
            out.write(ba);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                in.close();
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
