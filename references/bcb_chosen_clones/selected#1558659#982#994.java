    public static void browse(String url) {
        Desktop desktop = null;
        if (Desktop.isDesktopSupported()) desktop = Desktop.getDesktop();
        if (desktop != null) {
            try {
                desktop.browse(new java.net.URI(url));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.toString(), ex.getMessage(), JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(null, Language.get("ERROR_BROWSE_MSG").replaceAll("%u", url), Language.get("ERROR_BROWSE_TITLE"), JOptionPane.WARNING_MESSAGE);
        }
    }
