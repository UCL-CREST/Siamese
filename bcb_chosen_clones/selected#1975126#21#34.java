    public static void openUrl(Component parentComponent, String url) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            URI uri = null;
            try {
                uri = new URI(url);
                desktop.browse(uri);
            } catch (IOException ioe) {
                JOptionPane.showMessageDialog(parentComponent, ioe.getMessage(), Messages.getString("VcMainFrame.msgTitleError"), JOptionPane.ERROR_MESSAGE);
            } catch (URISyntaxException use) {
                JOptionPane.showMessageDialog(parentComponent, use.getMessage(), Messages.getString("VcMainFrame.msgTitleError"), JOptionPane.ERROR_MESSAGE);
            }
        }
    }
