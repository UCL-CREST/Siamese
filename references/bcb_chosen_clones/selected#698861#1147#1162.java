    private void HelpMIG(String str) {
        boolean browseOK = false;
        if (java.awt.Desktop.isDesktopSupported()) {
            java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
            if (desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
                browseOK = true;
                try {
                    java.net.URI uri = new java.net.URI("file://" + CEq_system.MIG_dir + str);
                    desktop.browse(uri);
                } catch (Exception e) {
                    browseOK = false;
                }
            }
        }
        if (!browseOK) CEq_0_gui.info(CEq_environment.mig(), "MIG language");
    }
