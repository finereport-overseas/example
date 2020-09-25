package com.fr.data;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import com.fr.base.Parameter;
import com.fr.general.data.DataModel;
import com.fr.script.Calculator;
import com.fr.stable.ParameterProvider;
import com.fr.stable.StringUtils;
import com.fr.config.holder.impl.xml.XmlColConf;

/**
 *  XMLDemoTableData
 *
 *  This demo use $filename to parse different XML file
 *
 *  AbstractParameterTableData is the basic implementation of a parameterize class dataset
 */
public class XMLDemoTableData extends AbstractParameterTableData {

    public XMLDemoTableData() {
        // define a parameter and give it a default value
        Parameter[] parameters = new Parameter[1];
        parameters[0] = new Parameter("filename", "Northwind");
        setParameters(parameters);
    }

    /**
     * return the object to fetch data
     */
    @SuppressWarnings("unchecked")
    public DataModel createDataModel(Calculator calculator) {
        // get the parameters passed in
        ParameterProvider[] params = super.processParameters(calculator);

        // get the file according to the parameter
        String filename = null;
        for (int i = 0; i < params.length; i++) {
            if (params[i] == null) continue;

            if ("filename".equals(params[i].getName())) {
                filename = (String)params[i].getValue();
            }
        }

        String filePath;
        if (StringUtils.isBlank(filename)) {
            filePath = "D:\\OtherProjects\\example\\webroot\\WEB-INF\\files\\DefaultFile.xml";
        } else {
            filePath = "D:\\OtherProjects\\example\\webroot\\WEB-INF\\files\\" + filename + ".xml";
        }

        List list=new ArrayList();
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        InputStream in;
        try {
            in = new BufferedInputStream(new FileInputStream(new File(filePath)));
            XMLEventReader reader = inputFactory.createXMLEventReader(in);
            // read column names
            readCol(reader,list);
            in.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        XMLColumnNameType4Demo[] columns=(XMLColumnNameType4Demo[])list.toArray(new XMLColumnNameType4Demo[0]);

        // define the xpath
        String[] xpath = new String[2];
        xpath[0] = "Northwind";
        xpath[1] = "Customers";

        return new XMLParseDemoDataModel(filePath, xpath, columns);
    }
    private int deep=0;
    private static final int COL_DEEP=3;
    private boolean flag=false;
    private void readCol(XMLEventReader reader,List list) throws XMLStreamException {
        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                // Deep is used to control the layers.
                deep++;
                // This means we have entered the layer of columns
                if(deep==COL_DEEP){
                    flag=true;
                }
                if(deep<COL_DEEP&&flag){
                    return;
                }
                if(deep!=COL_DEEP){
                    continue;
                }

                XMLColumnNameType4Demo column=new XMLColumnNameType4Demo(event.asStartElement().getName().toString(), XMLParseDemoDataModel.COLUMN_TYPE_STRING);
                list.add(column);
                readCol(reader,list);
            } else if (event.isCharacters()) {
                // do nothing
            } else if (event.isEndElement()) {
                deep--;
                return;
            }
        }
    }
}