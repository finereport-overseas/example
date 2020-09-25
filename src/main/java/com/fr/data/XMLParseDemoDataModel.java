package com.fr.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import com.fr.log.FineLoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.fr.general.ComparatorUtils;
import com.fr.general.data.TableDataException;

/**
 * XMLParseDemoDataModel
 *
 * DataModel defines the way to fetch data.
 *
 * Here use init() to grab all data once, and build a two-dimensional table.
 */
public class XMLParseDemoDataModel extends AbstractDataModel {
    // data type
    public static final int COLUMN_TYPE_STRING = 0;
    public static final int COLUMN_TYPE_INTEGER = 1;
    public static final int COLUMN_TYPE_BOOLEAN = 2;

    // data grabbed from XML file
    protected List row_list = null;

    // the path to actual data nodes
    private String[] xPath;
    // nodes containing data
    private XMLColumnNameType4Demo[] columns;

    private String filePath;

    public XMLParseDemoDataModel(String filename, String[] xPath,
                                 XMLColumnNameType4Demo[] columns) {
        this.filePath = filename;
        this.xPath = xPath;
        this.columns = columns;
    }

    /**
     * get the number of columns
     */
    public int getColumnCount() throws TableDataException {
        return columns.length;
    }

    /**
     * get the column name by index
     */
    public String getColumnName(int columnIndex) throws TableDataException {
        if (columnIndex < 0 || columnIndex >= columns.length)
            return null;
        String columnName = columns[columnIndex] == null ? null
                : columns[columnIndex].getName();

        return columnName;
    }

    /**
     * get the number of rows
     */
    public int getRowCount() throws TableDataException {
        this.init();
        return row_list.size();
    }

    /**
     * get the value at (row, column)
     */
    public Object getValueAt(int rowIndex, int columnIndex)
            throws TableDataException {
        this.init();
        if (rowIndex < 0 || rowIndex >= row_list.size() || columnIndex < 0
                || columnIndex >= columns.length)
            return null;
        return ((Object[]) row_list.get(rowIndex))[columnIndex];
    }

    /**
     * release resources after finishing data fetching
     */
    public void release() throws Exception {
        if (this.row_list != null) {
            this.row_list.clear();
            this.row_list = null;
        }
    }

    /** ************************************************** */
    /** ***********DataModel implemented above*************** */
    /** ************************************************** */

    /** ************************************************** */
    /** ************Parse XML file below**************** */
    /** ************************************************** */

    // grab the data once
    protected void init() throws TableDataException {
        if (this.row_list != null)
            return;

        this.row_list = new ArrayList();
        try {
            // use SAX to parse XML file
            SAXParserFactory f = SAXParserFactory.newInstance();
            SAXParser parser = f.newSAXParser();

            parser.parse(new File(XMLParseDemoDataModel.this.filePath),
                    new DemoHandler());
        } catch (Exception e) {
            e.printStackTrace();
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }

    /**
     * The parser will invoke startElement() when it finds the start tag.
     * It will use characters() to read the value.
     * If it meets the end tag, endElement() will be called.
     */
    private class DemoHandler extends DefaultHandler {
        private List levelList = new ArrayList(); // record the path to current node
        private Object[] values; // cache data
        private int recordIndex = -1; // current column being recorded, -1 means no need to record

        public void startElement(String uri, String localName, String qName,
                                 Attributes attributes) throws SAXException {
            // add current node to the path
            levelList.add(qName);

            if (isRecordWrapTag()) {
                // initialize values
                values = new Object[XMLParseDemoDataModel.this.columns.length];
            } else if (needReadRecord()) {
                // get the column index of this tag
                recordIndex = getColumnIndex(qName);
            }
        }

        public void characters(char[] ch, int start, int length)
                throws SAXException {
            if (recordIndex > -1) {
                // read value
                String text = new String(ch, start, length);
                XMLColumnNameType4Demo type = XMLParseDemoDataModel.this.columns[recordIndex];
                Object value = null;
                if (type.getType() == COLUMN_TYPE_STRING) {
                    value = text;
                }
                if (type.getType() == COLUMN_TYPE_INTEGER) {
                    value = new Integer(text);
                } else if (type.getType() == COLUMN_TYPE_BOOLEAN) {
                    value = new Boolean(text);
                }
                // put the value into the right place
                values[recordIndex] = value;
            }
        }

        public void endElement(String uri, String localName, String qName)
                throws SAXException {
            try {
                if (isRecordWrapTag()) {
                    // add the record to the list
                    XMLParseDemoDataModel.this.row_list.add(values);
                    values = null;
                } else if (needReadRecord()) {
                    // the record is not complete
                    recordIndex = -1;
                }
            } finally {
                levelList.remove(levelList.size() - 1);
            }
        }

        // whether this tag is one layer above the tags need to be recorded
        private boolean isRecordWrapTag() {
            if (levelList.size() == XMLParseDemoDataModel.this.xPath.length
                    && compareXPath()) {
                return true;
            }

            return false;
        }

        // whether this tag needs to be recorded
        private boolean needReadRecord() {
            if (levelList.size() == (XMLParseDemoDataModel.this.xPath.length + 1)
                    && compareXPath()) {
                return true;
            }

            return false;
        }

        // whether the levelList matches the xpath
        private boolean compareXPath() {
            String[] xPath = XMLParseDemoDataModel.this.xPath;
            for (int i = 0; i < xPath.length; i++) {
                if (!ComparatorUtils.equals(xPath[i], levelList.get(i))) {
                    return false;
                }
            }

            return true;
        }

        // get the column index according to column name
        private int getColumnIndex(String columnName) {
            XMLColumnNameType4Demo[] nts = XMLParseDemoDataModel.this.columns;
            for (int i = 0; i < nts.length; i++) {
                if (ComparatorUtils.equals(nts[i].getName(), columnName)) {
                    return i;
                }
            }

            return -1;
        }
    }
}