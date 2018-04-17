    private static boolean openClean(URL url) {
        try {
            if (!Desktop.isDesktopSupported()) {
                return false;
            }
            Desktop desktop = Desktop.getDesktop();
            if (!desktop.isSupported(Desktop.Action.BROWSE)) {
                return false;
            }
            try {
                desktop.browse(url.toURI());
                return true;
            } catch (IOException ex) {
                return false;
            } catch (URISyntaxException ex) {
                return false;
            }
        } catch (NoClassDefFoundError ex) {
            return false;
        }
    }
