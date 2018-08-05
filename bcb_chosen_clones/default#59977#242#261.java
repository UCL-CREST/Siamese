    private void tableMouseClicked(java.awt.event.MouseEvent evt) {
        count++;
        if (count % 2 == 0) {
            int column = table.getSelectedColumn();
            int row = table.getSelectedRow();
            Object value = table_b.getValueAt(row, column);
            String file_name = (String) value;
            file_name = file_name.toLowerCase();
            File f = new File(file_name);
            try {
                Desktop desktop = null;
                if (Desktop.isDesktopSupported()) {
                    desktop = Desktop.getDesktop();
                }
                desktop.open(f);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }
