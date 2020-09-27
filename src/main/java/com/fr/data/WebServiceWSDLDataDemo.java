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
    // 获取列的名称为数组中第一行的值
    public String getColumnName(int columnIndex) throws TableDataException {
        return data[0][columnIndex];
    }
    // 获取行数为数据的长度-1
    public int getRowCount() throws TableDataException {
        return data.length - 1;
    }
    // 获取值
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex + 1][columnIndex];
    }
    // 取数
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
        String results[][] = new String[result1.length][2]; // 这里的列数根据自己取出的列数而定，行数当然是有多少取多少
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
    // 获取连接并取数
    private static String[][] getWSDLData() {
        try {
            String url = "http://localhost:8080/axis2/services/myService?wsdl"; // 这里的url即为发布的WebService具体地址
            EndpointReference targetEPR = new EndpointReference(url);
// 创建一个OMFactory，下面的namespace、方法与参数均需由它创建
            OMFactory fac = OMAbstractFactory.getOMFactory();
// 命名空间namespace
            OMNamespace omNs = fac.createOMNamespace("http://service", "a");
// 方法
            OMElement method = fac.createOMElement("get", omNs); // 对应方法名
// 参数
            Options options = new Options();
            options.setTo(targetEPR);
            options.setAction("http://service/get");
// 构建请求
            ServiceClient sender = new ServiceClient();
            sender.setOptions(options);
// 发送请求
            OMElement result1 = sender.sendReceive(method);
            return getResults(result1);
        } catch (org.apache.axis2.AxisFault e) {
            FineLoggerFactory.getLogger().error(e, e.getMessage());
        }
        return null;
    }
    // 测试
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