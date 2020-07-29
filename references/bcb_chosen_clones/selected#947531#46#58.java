    public void openQuickStart(int port) throws Exception {
        String url = "http://localhost:" + port + "/quickstart/quickstart/index.html";
        if (!java.awt.Desktop.isDesktopSupported()) {
            System.err.println("Desktop is not supported (fatal)");
        } else {
            java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
            if (!desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
                System.err.println("Desktop doesn't support the browse action");
            } else {
                desktop.browse(new URL(url).toURI());
            }
        }
    }
