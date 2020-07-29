    public static void openBrowser(URI uri) {
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(uri);
                } else {
                    log.error(OPEN_THE_DEFAULT_BROWSER_NOT_SUPPORTED);
                    JOptionPane.showMessageDialog(MainFrame.getInstance(), OPEN_THE_DEFAULT_BROWSER_NOT_SUPPORTED, COULD_NOT_OPEN_BROWSER, JOptionPane.ERROR_MESSAGE);
                }
            } else {
                log.error(DESKTOP_INTERACTION_NOT_SUPPORTED);
                JOptionPane.showMessageDialog(MainFrame.getInstance(), DESKTOP_INTERACTION_NOT_SUPPORTED, COULD_NOT_OPEN_BROWSER, JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            log.error(COULD_NOT_OPEN_BROWSER, e);
            JOptionPane.showMessageDialog(MainFrame.getInstance(), COULD_NOT_OPEN_BROWSER, COULD_NOT_OPEN_BROWSER, JOptionPane.ERROR_MESSAGE);
        }
    }
