package com.fr.data;

import com.fr.script.Calculator;

/**
 * @author fanruan
 */
public class DemoTotalSubmitJob extends TotalSubmitJob {

    /**
     * 同一提交事件，在一个提交事务内只执行一次
     *
     * @param data 以二维表排列的所有提交数据
     */
    @Override
    protected void doTotalJob(Data data, Calculator calculator) {
        // 获取列的数量，每一列对应一个添加的属性
        data.getColumnCount();
        for (int i = 0; i < data.getColumnCount(); i++) {
            // 获取对应的属性名称
            System.out.println(data.getColumnName(i));
        }

        // getRowCount 获取一共多少行数据
        for (int i = 0; i < data.getRowCount(); i++) {
            System.out.print("ROW " + i + " {");
            for (int j = 0; j < data.getColumnCount(); j++) {
                if (j > 0) {
                    System.out.print(", ");
                }
                // 获取对应位置的值
                Object value = data.getValueAt(i, j);
                if (value instanceof JobValue) {
                    JobValue ce = (JobValue) value;
                    // JobValue的getValueState()方法获取此对应单元格的状态
                    if (ce.getValueState() == JobValue.VALUE_STATE_CHANGED) {
                        // 此单元格的值在报表初始化后被修改过
                        System.out.println();
                    } else if (ce.getValueState() == JobValue.VALUE_STATE_INSERT) {
                        // 此单元格是在报表初始化后新增的(例如执行了插入行操作)
                        System.out.println();
                    } else if (ce.getValueState() == JobValue.VALUE_STATE_DELETED) {
                        // 此单元格所在的记录被执行了删除操作
                        System.out.println();
                    } else if (ce.getValueState() == JobValue.VALUE_STATE_DEFAULT) {
                        // 此单元格在报表初始化后没有变化
                        System.out.println();
                    }
                    // 通过JobValue的getValue方法获得单元格的值
                    value = ce.getValue();
                }

                System.out.print(data.getColumnName(j) + " : " + value);
            }
            System.out.print("}");
            System.out.println();
        }
    }
}
