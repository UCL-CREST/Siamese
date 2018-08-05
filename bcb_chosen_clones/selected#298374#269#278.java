    public static void browse(String urlStr) {
        if (Desktop.isDesktopSupported()) {
            try {
                Desktop.getDesktop().browse(new URI(urlStr));
            } catch (Exception e) {
            }
        } else {
            JOptionPane.showMessageDialog(MainWindow.mw, "Cannot open the URL. Kindly open this url\n" + "from your browser:\n" + urlStr);
        }
    }
