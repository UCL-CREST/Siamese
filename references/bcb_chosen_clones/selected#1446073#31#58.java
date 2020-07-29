    public PasteOEdit(JTable tableSource, JTable tableDest, EditAdapter clipboardEdit) {
        modelDest = (ModelPlate) tableDest.getModel();
        modelSource = (ModelSourceSorted) tableSource.getModel();
        this.startRow = tableDest.getSelectedRow();
        this.numRows = clipboardEdit.getClipboardHeight();
        this.startCol = tableDest.getSelectedColumn();
        this.numCols = clipboardEdit.getClipboardWidth();
        int newNumRows = startRow + numCols;
        int newNumCols = startCol + numRows;
        if ((modelDest.getRowCount() < newNumRows) || (modelDest.getColumnCount() < newNumCols)) {
            JOptionPane.showMessageDialog(tableSource, "Attempt to paste orthogonally outside the bounds", "PAD error", JOptionPane.ERROR_MESSAGE);
        } else {
            Object[][] arrayTmpPaste = clipboardEdit.getArrayPaste();
            arrayTemp = new Object[numCols][numRows];
            arrayPaste = new Object[numCols][numRows];
            for (int i = 0; i < numCols; i++) {
                for (int j = 0; j < numRows; j++) {
                    arrayPaste[i][j] = arrayTmpPaste[j][i];
                    arrayTemp[i][j] = modelDest.getValueAt(startRow + i, startCol + j);
                    modelDest.setValueAt(arrayPaste[i][j], startRow + i, startCol + j);
                    modelSource.makeAvailable(arrayTemp[i][j]);
                    modelSource.makeUnavailable(arrayPaste[i][j], startRow + i, startCol + j);
                }
            }
        }
        tableDest.setRowSelectionInterval(startRow, newNumRows - 1);
        tableDest.setColumnSelectionInterval(startCol, newNumCols - 1);
    }
