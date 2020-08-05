package com.fr.data;

import com.fr.base.FRContext;
import com.fr.file.DatasourceManager;
import com.fr.log.FineLoggerFactory;
import com.fr.stable.ParameterProvider;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * A Demo for a program dataset with parameters
 */
public class ParamTableDataDemo extends AbstractTableData {
    /**
     * A String array for column names
     */
    private String[] columnNames;
    /**
     * Column numbers
     */
    private int columnNum = 10;
    /**
     * Actual column numbers of the query result.
     */
    private int colNum = 0;
    /**
     * To store the query result.
     */
    private ArrayList<Object[]> valueList = null;

    /**
     * Constructor. Define the table structure.
     */
    public ParamTableDataDemo() {
        columnNames = new String[columnNum];
        for (int i = 0; i < columnNum; i++) {
            columnNames[i] = "column#" + String.valueOf(i);
        }
    }

    /**
     * Implement the four functions.
     *
     * @return columnNum
     */
    @Override
    public int getColumnCount() {
        return columnNum;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public int getRowCount() {
        init();
        return valueList == null ? 0 : valueList.size();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        init();
        if (valueList != null && valueList.size() > rowIndex) {
            if (colNum > columnIndex) {
                return valueList.get(rowIndex)[columnIndex];
            }
        }
        return null;
    }

    /**
     * Prepare data.
     */
    private void init() {
        // Make sure it only runs once.
        if (valueList != null) {
            return;
        }
        // Save the first parameter as the tableName.
        String tableName = ((ParameterProvider) (parameters.get().toArray())[0]).getValue().toString();

        // Build the sql and print it.
        String sql = "select * from " + tableName;
        FineLoggerFactory.getLogger().info("Query SQL of ParamTableDataDemo: \n" + sql);

        valueList = new ArrayList<Object[]>();

        try {
            Connection con = getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            // Get actual column numbers.
            ResultSetMetaData rsmd = rs.getMetaData();
            colNum = rsmd.getColumnCount();
            // Save the row data into an object array.
            Object[] objArray = null;
            while (rs.next()) {
                objArray = new Object[colNum];
                for (int i = 0; i < colNum; i++) {
                    objArray[i] = rs.getObject(i + 1);
                }

                valueList.add(objArray);
            }
            // Release the resource.
            rs.close();
            stmt.close();
            con.close();


        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
    }

    /**
     * Get the connection. Change driverName and url into what you need.
     *
     * @return Connection
     */
    private Connection getConnection() {

        String driverName = "org.sqlite.JDBC";
        String url = "jdbc:sqlite:D:\\OtherProjects\\demo\\webroot\\help\\FRDemoEN.db";
        String username = "";
        String password = "";
        Connection con = null;
        try {
            Class.forName(driverName);
            con = DriverManager.getConnection(url, username, password);

        } catch (Exception e) {
            FineLoggerFactory.getLogger().error(e.getMessage(), e);
        }
        return con;
    }


    /**
     * There may be some duplicate function calling, so empty the valueList.
     *
     * @throws Exception e
     */
    @Override
    public void release() throws Exception {
        super.release();
        this.valueList = null;
    }
}