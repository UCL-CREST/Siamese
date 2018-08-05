    private void launchLink(String link) {
        boolean hasResult = true;
        if (java.awt.Desktop.isDesktopSupported()) {
            java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
            if (desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
                try {
                    try {
                        desktop.browse(new java.net.URI(link));
                    } catch (IOException ex) {
                        hasResult = false;
                    }
                } catch (URISyntaxException ex) {
                    hasResult = false;
                }
            } else {
                hasResult = false;
            }
        } else {
            hasResult = false;
        }
        if (hasResult == false) {
            java.util.Properties sys = System.getProperties();
            String os = sys.getProperty("os.name").toLowerCase();
            try {
                if (os.contains("windows") == true) {
                    Process proc = Runtime.getRuntime().exec("cmd /c start " + link);
                } else {
                    Process proc = Runtime.getRuntime().exec("start " + link);
                }
            } catch (java.io.IOException e) {
            }
        }
    }
