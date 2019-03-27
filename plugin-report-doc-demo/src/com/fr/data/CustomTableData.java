package com.fr.data;


/**
 * @author fanruan
 */
public class CustomTableData extends AbstractTableData {

    /**
     * 获取数据集的列数
     *
     * @return 数据集的列
     */
    @Override
    public int getColumnCount() {
        return 0;
    }

    /**
     * 获取数据集指定列的列名
     *
     * @param columnIndex 指定列的索引
     * @return 指定列的列名
     */
    @Override
    public String getColumnName(int columnIndex) {
        return null;
    }

    /**
     * 获取数据集的行数
     *
     * @return 数据集数据行数
     */
    @Override
    public int getRowCount() {
        return 0;
    }

    /**
     * 获取数据集指定位置上的值
     *
     * @param rowIndex    指定的行索引
     * @param columnIndex 指定的列索引
     * @return 指定位置的值
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return null;
    }
}
