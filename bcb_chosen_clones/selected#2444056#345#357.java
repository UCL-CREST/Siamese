    private void openReport(File file) {
        assert file != null;
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.open(file);
            } catch (Exception e) {
                e.printStackTrace();
                timeSlotTracker.errorLog("Error during opening document: " + e);
                timeSlotTracker.errorLog(e);
            }
        }
    }
