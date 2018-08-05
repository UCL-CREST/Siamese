    private void gotoDownloadURLButtonActionPerformed(java.awt.event.ActionEvent evt) {
        NULogger.getLogger().log(Level.INFO, "{0}: GotoDownloadURL Button clicked", getClass().getName());
        if (uploadTable.getSelectedRowCount() < 1) {
            JOptionPane.showMessageDialog(this, TranslationProvider.get("neembuuuploader.UploadHistory.noRowsSelected"));
            return;
        }
        int[] selectedrows = uploadTable.getSelectedRows();
        for (int selectedrow : selectedrows) {
            String url = uploadTable.getValueAt(selectedrow, DOWNLOADURL).toString();
            if (!Desktop.isDesktopSupported()) {
                return;
            }
            try {
                NULogger.getLogger().log(Level.INFO, "Opening url in browser: {0}", url);
                Desktop.getDesktop().browse(new URI(url));
            } catch (Exception ex) {
                NULogger.getLogger().log(Level.WARNING, "{0}: Cannot load url: {1}", new Object[] { getClass().getName(), url });
                System.err.println(ex);
            }
        }
    }
