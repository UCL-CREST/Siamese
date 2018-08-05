    private void doHandleDoubleClick() {
        if (jTable.getSelectedRowCount() == 1) {
            int row = jTable.getSelectedRow();
            String path = (String) model.getValueAt(row, 1);
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().open((new File(path)).getParentFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
