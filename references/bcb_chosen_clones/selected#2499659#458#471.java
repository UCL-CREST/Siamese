    public int openModFolder() {
        if (!java.awt.Desktop.isDesktopSupported()) {
            logger.info("Opening local folders is not supported");
            return -1;
        }
        java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
        try {
            desktop.open(new File(ManagerOptions.getInstance().getModPath()));
        } catch (Exception e) {
            logger.error("Unable to open local folder: " + e.getMessage());
            return -1;
        }
        return 0;
    }
