package com.fr.data;
import javax.naming.*;
import java.util.*;
import examples.ejb.ejb20.basic.beanManaged.*;
public class DataModelDemo extends AbstractTableData {
    private String[] columnNames;
    private ArrayList valueList = null;
    public DataModelDemo() {
        String[] columnNames = { "Name", "Score" };
        this.columnNames = columnNames;
    }
    // implement the other four methods
    public int getColumnCount() {
        return columnNames.length;
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
    // get the data ready
    public void init() {
        // make sure it's only be executed once
        if (valueList != null) {
            return;
        }
        // save the result set
        valueList = new ArrayList();
        Context ctx = null;
        Account ac = null;
        AccountHome home = null;
        try {
            // Contact the AccountBean container (the "AccountHome") through
            // JNDI.
            ctx = new InitialContext();
            home = (AccountHome) ctx
                    .lookup("java:/comp/env/BeanManagedAccountEJB");
            double balanceGreaterThan = 100;
            Collection col = home.findBigAccounts(balanceGreaterThan);
            if (col != null) {
                // save data with objects
                Object[] objArray = null;
                Iterator iter = col.iterator();
                while (iter.hasNext()) {
                    Account bigAccount = (Account) iter.next();
                    objArray = new Object[2];
                    objArray[0] = bigAccount.getPrimaryKey();
                    objArray[1] = new Double(bigAccount.balance());
                    //  add the data in objArray to valuelist 
                    valueList.add(objArray);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
