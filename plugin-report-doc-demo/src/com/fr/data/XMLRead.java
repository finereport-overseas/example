package com.fr.data;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * @author fanruan
 */
public class XMLRead extends AbstractTableData {
    /**
     * 列名数组，保存程序数据集所有列名
     */
    private String[] columnNames = {"id", "name", "MemoryFreeSize",
            "MemoryTotalSize", "MemoryUsage"};
    /**
     * 保存表数据
     */
    private ArrayList valueList = null;

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public int getRowCount() {
        init();
        return valueList.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        init();
        return ((Object[]) valueList.get(rowIndex))[columnIndex];
    }

    private void init() {
        // 确保只被执行一次
        if (valueList != null) {
            return;
        }
        valueList = new ArrayList();
        String sql = "SELECT * FROM xmltest";
        String[] name = {"MemoryFreeSize", "MemoryTotalSize", "MemoryUsage"};
        Connection conn = this.getConnection();
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            // 用对象保存数据
            Object[] objArray;
            while (rs.next()) {
                objArray = new Object[5];
                String[] xmlData;
                objArray[0] = rs.getObject(1);
                objArray[1] = rs.getObject(2);
                InputStream in;
                String str = "中文stream";
                in = new ByteArrayInputStream(str.getBytes("UTF-8"));
                GetXmlData getXMLData = new GetXmlData();
                // 对xml流进行解析，返回的为name对应的value值数组
                xmlData = getXMLData.readerXMLSource(in, name);
                // 将解析后的值存于最终结果ArrayList中
                objArray[2] = xmlData[0];
                objArray[3] = xmlData[1];
                objArray[4] = xmlData[2];
                valueList.add(objArray);
            }
            // 释放数据源
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Connection getConnection() {
        String driverName = "oracle.jdbc.driver.OracleDriver";
        String url = "jdbc:oracle:thin:@env.finedevelop.com:55702:fr";
        String username = "system";
        String password = "123";
        Connection con;

        try {
            Class.forName(driverName);
            con = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return con;

    }

    /**
     * 释放一些资源，因为可能会有重复调用，所以需释放valueList，将上次查询的结果释放掉
     *
     * @throws Exception e
     */
    @Override
    public void release() throws Exception {
        super.release();
        this.valueList = null;
    }
}