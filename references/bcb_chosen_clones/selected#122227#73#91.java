    protected static void launchUsingDesktop(String url) {
        if (!java.awt.Desktop.isDesktopSupported()) {
            Xholon.getLogger().error("unable to display url: " + url + " java.awt.Desktop.isDesktopSupported() = false");
            return;
        }
        Desktop desktop = Desktop.getDesktop();
        if (!desktop.isSupported(java.awt.Desktop.Action.BROWSE)) {
            Xholon.getLogger().error("unable to display url: " + url + " desktop.isSupported(java.awt.Desktop.Action.BROWSE) = false");
            return;
        }
        try {
            URI uri = new java.net.URI(url);
            desktop.browse(uri);
        } catch (URISyntaxException e1) {
            Xholon.getLogger().error("unable to display url: " + url + " " + e1.toString());
        } catch (IOException e1) {
            Xholon.getLogger().error("unable to display url: " + url + " " + e1.toString());
        }
    }
