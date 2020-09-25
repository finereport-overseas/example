package com.fr.data;

import com.fr.stable.xml.XMLReadable;
import com.fr.stable.xml.XMLableReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * @author fanruan
 */
public class GetXmlData {
    /**
     * the return value String
     */
    private String[] value = new String[3];
    /**
     * the name array to query
     */
    private String[] name = null;

    protected String[] readerXMLSource(InputStream in, String[] name)
            throws Exception {
        this.name = name;
        InputStreamReader reader = new InputStreamReader(in, "utf-8");
        readXMLSource(reader);
        return value;
    }

    protected void readXMLSource(Reader reader) throws Exception {
        XMLableReader xmlReader = XMLableReader.createXMLableReader(reader);
        if (xmlReader != null) {
            xmlReader.readXMLObject(new Content());

        }
    }

    private class Content implements XMLReadable {
        @Override
        public void readXML(XMLableReader reader) {
            if (reader.isChildNode()) {
                if ("Field".equals(reader.getTagName())) {
                    Field field = new Field();
                    reader.readXMLObject(field);
                    // get its value according to the name
                    if (name[0].equals(field.name)) {
                        value[0] = field.value;
                    } else if (name[1].equals(field.name)) {
                        value[1] = field.value;
                    } else if (name[2].equals(field.name)) {
                        value[2] = field.value;
                    }
                }
            }
        }
    }

    /**
     * the structure of every field
     */
    private class Field implements XMLReadable {
        private String name;
        private String type;
        private String value;

        @Override
        public void readXML(XMLableReader reader) {
            if (reader.isChildNode()) {
                String tagName = reader.getTagName();
                if ("Name".equals(tagName)) {
                    this.name = reader.getElementValue();
                } else if ("Type".equals(tagName)) {
                    this.type = reader.getElementValue();
                } else if ("Value".equals(tagName)) {
                    this.value = reader.getElementValue();
                }
            }
        }
    }
}