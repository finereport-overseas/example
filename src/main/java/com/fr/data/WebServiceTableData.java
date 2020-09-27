package com.fr.data;

import javax.xml.namespace.QName;

import org.apache.axis.AxisProperties;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import com.fr.data.AbstractTableData;
import com.fr.general.data.TableDataException;

public class WebServiceTableData extends AbstractTableData{
    private String[][] data;

    public WebServiceTableData() {
        this.data = this.createData();
    }

    public int getColumnCount() throws TableDataException {
        return data[0].length;
    }

    // The column names are the value of first row
    public String getColumnName(int columnIndex) throws TableDataException {
        return data[0][columnIndex];
    }

    // the number of data row
    public int getRowCount() throws TableDataException {
        return data.length - 1;
    }

    // get value
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex + 1][columnIndex];
    }

    public String[][] createData() {
        try {
            // ignore ssl verification
            AxisProperties.setProperty("axis.socketSecureFactory",  "org.apache.axis.components.net.SunFakeTrustSocketFactory");
            String endpoint = "https://localhost:8443/axis/TestWS2TDClient.jws";
            Service service = new Service();
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(new java.net.URL(endpoint));
            call.setOperationName(new QName("https://localhost:8443/axis/TestWS2TDClient.jws",
                    "getTD"));
            String[][] ret = (String[][])call.invoke(new Object[] {});
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String[][] {};
    }
}