    public static void openBrowser(URI uri) {
        boolean openBrowserSuccess = false;
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(uri);
                    openBrowserSuccess = true;
                } else {
                    log.error(OPEN_THE_DEFAULT_BROWSER_NOT_SUPPORTED);
                    JOptionPane.showMessageDialog(null, OPEN_THE_DEFAULT_BROWSER_NOT_SUPPORTED, COULD_NOT_OPEN_BROWSER, JOptionPane.ERROR_MESSAGE);
                }
            } else {
                log.error(DESKTOP_INTERACTION_NOT_SUPPORTED);
                JOptionPane.showMessageDialog(null, DESKTOP_INTERACTION_NOT_SUPPORTED, COULD_NOT_OPEN_BROWSER, JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            log.error(COULD_NOT_OPEN_BROWSER, e);
            JOptionPane.showMessageDialog(null, COULD_NOT_OPEN_BROWSER, "Error...", JOptionPane.ERROR_MESSAGE);
        }
        if (!openBrowserSuccess) {
            JOptionPane.showInputDialog(null, "Please open the following address with the browser of your choice:", COULD_NOT_OPEN_BROWSER, JOptionPane.INFORMATION_MESSAGE, null, null, uri);
        }
    }
