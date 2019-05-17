//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.fr.data.impl;

import com.fr.base.Formula;
import com.fr.cache.Attachment;
import com.fr.data.SubmitJob;
import com.fr.general.FArray;
import com.fr.script.Calculator;
import com.fr.stable.UtilEvalError;
import com.fr.stable.xml.XMLPrintWriter;
import com.fr.stable.xml.XMLableReader;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author fanruan
 */
public class Commit1 implements SubmitJob {
    private Object attach;
    private String filePath;

    public Commit1() {
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    private Object getAttach(Calculator ca) {
        if (this.attach != null && this.attach instanceof Formula) {
            try {
                return ca.eval(((Formula) this.attach).getContent());
            } catch (UtilEvalError var3) {
                var3.printStackTrace();
                return "";
            }
        } else {
            return ca.resolveVariable("attach");
        }
    }

    @Override
    public void doJob(Calculator ca) {
        final Object attachO = this.getAttach(ca);
        if (attachO instanceof FArray && ((FArray) attachO).length() != 0) {
            (new Thread() {
                @Override
                public void run() {
                    FArray attachmentlist = (FArray) attachO;

                    for (int i = 0; i < attachmentlist.length(); ++i) {
                        Statement sm = null;
                        String command = null;
                        String result = null;
                        if (attachmentlist.elementAt(i) instanceof Attachment) {
                            String FilePath = Commit1.this.filePath;
                            String FileName = ((Attachment) attachmentlist.elementAt(i)).getFilename();
                            (new StringBuilder(String.valueOf(FilePath))).append("\\").append(FileName).toString();
                            File fileDir = new File(FilePath);
                            if (!fileDir.exists()) {
                                fileDir.mkdirs();
                            }

                            try {
                                Commit1.mkfile(FilePath, FileName, new ByteArrayInputStream(((Attachment) attachmentlist.elementAt(i)).getBytes()));
                            } catch (Exception var11) {
                                Logger.getLogger("FR").log(Level.WARNING, var11.getMessage() + "/nmkfileerror", var11);
                            }
                        }
                    }

                }
            }).start();
        }

    }

    @Override
    public void doFinish(Calculator calculator) {

    }

    private static void mkfile(String path, String FileName, InputStream source) throws FileNotFoundException, IOException {
        File fileout = new File(path, FileName);
        if (fileout.exists()) {
            fileout.delete();
        }

        fileout.createNewFile();
        FileOutputStream outputStream = new FileOutputStream(fileout);
        byte[] bytes = new byte[1024];

        for (int read = source.read(bytes); read != -1; read = source.read(bytes)) {
            outputStream.write(bytes, 0, read);
            outputStream.flush();
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
    public String getJobType() {
        return null;
    }
}
