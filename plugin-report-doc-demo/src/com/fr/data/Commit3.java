package com.fr.data;

import com.fr.cache.Attachment;
import com.fr.data.impl.SubmitJobValue;
import com.fr.general.FArray;
import com.fr.general.FRLogger;
import com.fr.script.Calculator;
import com.fr.stable.xml.FRFile;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * @author fanruan
 */
public class Commit3 implements SubmitJob {
    private Object attach;
    /**
     * 定义文件路径
     */
    private SubmitJobValue filePath;

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public void doJob(Calculator ca) {
        FRLogger.getLogger().info("begin to upload file...");
        final Object attachO = attach;
        if (attachO instanceof FArray && ((FArray) attachO).length() != 0) {
            new Thread() {
                @Override
                public void run() {
                    int i;
                    FArray attachmentList = (FArray) attachO;
                    for (i = 0; i < attachmentList.length(); i++) {
                        if (!(attachmentList.elementAt(i) instanceof Attachment)) {
                            continue;
                        } else {
                            FRLogger.getLogger().info("filePath.value:" + filePath.getValue().toString());
                            FRLogger.getLogger().info("filePath.valueState:" + filePath.getValueState() +
                                    "注：valueState 0,1,2,3 分别表示 默认值，插入行，值改变，删除行");

                            String FilePath = filePath.getValue().toString();
                            String FileName = ((Attachment) (attachmentList.elementAt(i))).getFilename();
                            String Path = FilePath + "\\" + FileName;
                            File fileDir = new File(FilePath);
                            if (!fileDir.exists()) {
                                fileDir.mkdirs();
                            }
                            try {
                                //新建文件夹，并且写入内
                                mkfile(FilePath, FileName, new ByteArrayInputStream(
                                        ((Attachment) (attachmentList.elementAt(i))).getBytes()));
                            } catch (Exception e) {
                                Logger.getLogger("FR").log(Level.WARNING,
                                        e.getMessage() + "/nmkfileerror", e);
                            }
                        }
                    }
                }
            }.start();
        } else if (attach instanceof FRFile) {
            String filepath = filePath.getValue().toString();
            String filename = ((FRFile) attach).getFileName();
            File fileDir = new File(filepath);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            try {
                //新建文件夹，并且写入内
                mkfile(filepath, filename, new ByteArrayInputStream(
                        ((FRFile) attach).getBytes()));
            } catch (Exception e) {
                Logger.getLogger("FR").log(Level.WARNING,
                        e.getMessage() + "/nmkfileerror", e);
            }
        }
    }

    private static void mkfile(String path, String filename, InputStream source) throws IOException {
        File fileout = new File(path, filename);

        // 检查是否存在
        if (fileout.exists()) {
            // 删除文件
            fileout.delete();
            FRLogger.getLogger().info("old file deleted");
        }
        // 在当前目录下建立一个名为FileName的文件
        if (fileout.createNewFile()) {
            FRLogger.getLogger().info(path + filename + "created!!");
        }
        FileOutputStream outputStream = new FileOutputStream(fileout);
        byte[] bytes = new byte[1024];
        int read = source.read(bytes);
        //把source写入新建的文件
        while (read != -1) {
            outputStream.write(bytes, 0, read);
            outputStream.flush();
            read = source.read(bytes);
        }

        outputStream.close();
    }

    @Override
    public void readXML(XMLableReader reader) {
    }

    @Override
    public void writeXML(XMLPrintWriter writer) {
    }

    @Override
    public void doFinish(Calculator arg0) {
    }

    @Override
    public String getJobType() {
        return null;
    }
}
