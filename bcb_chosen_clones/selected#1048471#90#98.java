    public void openFile(final String url) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new File(url));
            }
        } catch (Exception ex) {
            this.getLogger().log(Level.SEVERE, null, ex);
        }
    }
