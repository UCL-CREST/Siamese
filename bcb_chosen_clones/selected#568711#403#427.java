    public static void OpenInDefaultBrowser(Component parent, URL url) {
        if (url != null) {
            if (AbstractProperties.OPERATING_SYSTEM.startsWith("linux")) {
                try {
                    String cmd = String.format("xdg-open %s", url);
                    Runtime.getRuntime().exec(cmd);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } else if (Desktop.isDesktopSupported()) {
                final Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    try {
                        desktop.browse(url.toURI());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (URISyntaxException ex2) {
                        ex2.printStackTrace();
                    }
                }
            } else {
                JOptionPane.showMessageDialog(parent, Messages.getString("JMVUtils.browser") + url, Messages.getString("JMVUtils.error"), JOptionPane.ERROR_MESSAGE);
            }
        }
    }
