    @Override
    public void actionPerformed(ActionEvent event) {
        FilesUploadedByOthers files = model.getFilesUploadedByOthers();
        synchronized (files) {
            int index = table.getSelectedRow();
            final Entry entry = files.get(index);
            boolean supported = Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Action.OPEN);
            if (!supported) {
                JOptionPane.showConfirmDialog(table, "Not supported on your plattform");
                return;
            }
            try {
                Desktop.getDesktop().open(entry.getPath().getParentFile());
            } catch (IOException e) {
                JOptionPane.showConfirmDialog(table, "Can't open folder: " + e.getMessage());
            }
        }
    }
