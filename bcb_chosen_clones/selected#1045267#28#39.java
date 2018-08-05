    @Override
    protected void afterSave(File outFile) {
        Desktop desktop = null;
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
            try {
                desktop.open(outFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
