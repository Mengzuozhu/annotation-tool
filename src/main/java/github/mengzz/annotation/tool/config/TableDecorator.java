package github.mengzz.annotation.tool.config;

import com.intellij.ui.TableUtil;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import github.mengzz.annotation.tool.model.AnnotationConfig;
import github.mengzz.annotation.tool.utils.ListUtil;
import lombok.Getter;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableRowSorter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author mengzz
 **/
public class TableDecorator {
    @Getter
    private List<AnnotationConfig> annotationConfigs;
    private CustomTableModel customTableModel;
    private JTable jTable;

    public TableDecorator(List<AnnotationConfig> annotationConfigs) {
        this.annotationConfigs = ListUtil.copy(annotationConfigs, AnnotationConfig.class);
    }

    public JPanel build() {
        customTableModel = new CustomTableModel();
        jTable = new JBTable(customTableModel);
        jTable.setRowSorter(new TableRowSorter<>(customTableModel));
        return ToolbarDecorator.createDecorator(jTable)
                .setAddAction(anActionButton -> add())
                .setRemoveAction(anActionButton -> remove())
                .setMoveDownAction(e -> moveDown())
                .setMoveUpAction(e -> moveUp())
                .createPanel();
    }

    public void reset(List<AnnotationConfig> annotationConfigs) {
        this.annotationConfigs = ListUtil.copy(annotationConfigs, AnnotationConfig.class);
        customTableModel.fireTableDataChanged();
    }

    private void add() {
        TableCellEditor cellEditor = jTable.getCellEditor();
        if (cellEditor != null) {
            cellEditor.stopCellEditing();
        }
        AnnotationConfig config = new AnnotationConfig("", AnnotationToolSetting.getInstance().getDefaultAttrValue(),
                "", "");
        annotationConfigs.add(config);
        customTableModel.fireTableDataChanged();
        TableUtil.editCellAt(jTable, customTableModel.getRowCount() - 1, 0);
    }

    private void remove() {
        final int[] selectedRows = jTable.getSelectedRows();
        if (selectedRows.length == 0) {
            return;
        }
        Arrays.sort(selectedRows);
        for (int i = selectedRows.length - 1; i >= 0; i--) {
            final int selectedRow = selectedRows[i];
            if (isValidRow(selectedRow)) {
                annotationConfigs.remove(selectedRow);
            }
        }
        customTableModel.fireTableDataChanged();
    }

    private void moveUp() {
        int selectedRow = jTable.getSelectedRow();
        int upIndex = selectedRow - 1;
        if (selectedRow != -1) {
            Collections.swap(annotationConfigs, selectedRow, upIndex);
        }
        jTable.setRowSelectionInterval(upIndex, upIndex);
    }

    private void moveDown() {
        int selectedRow = jTable.getSelectedRow();
        int downIndex = selectedRow + 1;
        if (selectedRow != -1) {
            Collections.swap(annotationConfigs, selectedRow, downIndex);
        }
        jTable.setRowSelectionInterval(downIndex, downIndex);
    }

    private boolean isValidRow(int i) {
        return i >= 0 && i < annotationConfigs.size();
    }

    private class CustomTableModel extends AbstractTableModel {
        @Override
        public int getRowCount() {
            return annotationConfigs.size();
        }

        @Override
        public int getColumnCount() {
            return 4;
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            final AnnotationConfig config = annotationConfigs.get(rowIndex);
            return config == null ? null : config.getValue(columnIndex);
        }

        @Override
        public String getColumnName(int columnIndex) {
            return AnnotationConfig.getColumnName(columnIndex);
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            return String.class;
        }

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return true;
        }

        @Override
        public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
            AnnotationConfig annotationConfig = annotationConfigs.get(rowIndex);
            if (annotationConfig == null) {
                return;
            }
            annotationConfig.setValue(aValue, columnIndex);
            customTableModel.fireTableDataChanged();
        }

    }

}
