    public static void loadHelpPage(String key) {
        try {
            java.awt.Desktop d = Desktop.getDesktop();
            if (Desktop.isDesktopSupported()) {
                if (key == null || key.length() == 0) {
                    d.browse(new URI(UIDefaults.PRODUCT_DOCUMENTATION_URL));
                } else {
                    String url = HELP_URL + key;
                    d.browse(new URI(url));
                }
            }
        } catch (Exception e) {
            logger.error(MiscUtils.stackTrace2String(e));
        }
    }
