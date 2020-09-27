package com.fr.data;

import com.fr.general.data.TableDataException;
import com.fr.log.FineLoggerFactory;
import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.om.OMNode;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Joe
 * Created by Joe on 9/27/2020
 */
public class WebServiceWSDLDataDemo extends AbstractTableData {
    private String[][] data;
    public WebServiceWSDLDataDemo() {
        this.data = getWSDLData();
    }
    public int getColumnCount() throws TableDataException {
        return data[0].length;
    }

    public String getColumnName(int columnIndex) throws TableDataException {
        return data[0][columnIndex];
    }

    public int getRowCount() throws TableDataException {
        return data.length - 1;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex + 1][columnIndex];
    }

    private static String[][] getResults(OMElement element) {
        if (element == null) {
            return null;
        }
        Iterator iterator = element.getChildElements();
        List<String> list = new ArrayList<>();
        while (iterator.hasNext()) {
            OMNode omNode = (OMNode) iterator.next();
            if (omNode.getType() == OMNode.ELEMENT_NODE) {
                OMElement omElement = (OMElement) omNode;
                if (omElement.getLocalName().equals("return")) {
                    String temp = omElement.getText().trim();
                    list.add(temp);
                }
            }
        }
        String[] result1 = list.toArray(new String[list.size()]);
        String results[][] = new String[result1.length][2]; // the column number depends on your own
        String b1, b2;
        for (int i = 0; i < result1.length; i++) {
            if (result1[i].length() != 0) {
                b1 = result1[i].substring(0, result1[i].indexOf(" "));
                b2 = result1[i].substring(result1[i].indexOf(" ") + 1);
                results[i][0] = b1;
                results[i][1] = b2;
            }
        }
        return results;
    }
    // get connection and fetch data
    private static String[][] getWSDLData() {
        try {
            String url = "http://localhost:8080/axis2/services/myService?wsdl"; // 这里的url即为发布的WebService具体地址
            EndpointReference targetEPR = new EndpointReference(url);
            // create an OMFactory to create namespace, method and parameters
            OMFactory fac = OMAbstractFactory.getOMFactory();
            // namespace
            OMNamespace omNs = fac.createOMNamespace("http://service", "a");
            // method
            OMElement method = fac.createOMElement("get", omNs);
            // parameters
            Options options = new Options();
            options.setTo(targetEPR);
            options.setAction("http://service/get");
            // create a request
            ServiceClient sender = new ServiceClient();
            sender.setOptions(options);
            // send the request
            OMElement result1 = sender.sendReceive(method);
            return getResults(result1);
        } catch (org.apache.axis2.AxisFault e) {
            FineLoggerFactory.getLogger().error(e, e.getMessage());
        }
        return null;
    }
    // For test
    public static void main(String[] args) {
        String[][] result = getWSDLData();
        if (result != null) {
            int col = result[0].length;
            for (String[] aResult : result) {
                for (int j = 0; j < col; j++) {
                    System.out.print(aResult[j] + " ");
                }
                System.out.println();
            }
        }
    }
}