package com.fr.data;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.io.StringBufferInputStream;

public class XMLRead extends AbstractTableData {
    // the column names
    private String[] columnNames = { "id", "name", "MemoryFreeSize",
            "MemoryTotalSize", "MemoryUsage" };
    // to save the data
    private ArrayList valueList = null;

    public int getColumnCount() {
        return 5;
    }

    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    public int getRowCount() {
        init();
        return valueList.size();
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        init();
        return ((Object[]) valueList.get(rowIndex))[columnIndex];
    }

    private void init() {
        // make sure no more repeated running
        if (valueList != null) {
            return;
        }
        valueList = new ArrayList();
        String sql = "select * from xmltest";
        String[] name = { "MemoryFreeSize", "MemoryTotalSize", "MemoryUsage" };
        Connection conn = this.getConncetion();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            // store data with objects
            Object[] objArray = null;
            while (rs.next()) {
                objArray = new Object[5];
                String[] xmldata = null;
                // ID
                objArray[0] = rs.getObject(1);
                // NAME
                objArray[1] = rs.getObject(2);
                // add a parent node
                InputStream in = new StringBufferInputStream("<demo>"
                        + rs.getObject(3).toString() + "</demo>");
                GetXmlData getxmldata = new GetXmlData();
                // read the xml stream and return the value array
                xmldata = getxmldata.readerXMLSource(in, name);
                // store the value in valueList
                objArray[2] = xmldata[0];
                objArray[3] = xmldata[1];
                objArray[4] = xmldata[2];
                valueList.add(objArray);
            }
            // release the data source
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Connection getConncetion() {
        // change to your own connection configuration
        String driverName = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://122.112.183.57:3306/joe";
        String username = "joe";
        String password = "123456";
        Connection con = null;

        try {
            Class.forName(driverName);
            con = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return con;

    }

    public void release() throws Exception {
        super.release();
        this.valueList = null;
    }
}