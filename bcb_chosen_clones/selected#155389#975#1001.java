    private void openFileInBrowser_action(final List<String> items) {
        if (!java.awt.Desktop.isDesktopSupported()) {
            return;
        }
        if (!Desktop.getDesktop().isSupported(java.awt.Desktop.Action.BROWSE)) {
            return;
        }
        final String browserAddress = Core.frostSettings.getValue(SettingsClass.BROWSER_ADDRESS);
        if (browserAddress.length() == 0) {
            System.out.println("DEBUG - Borser address not configured");
            return;
        }
        if (items == null || items.size() < 1) {
            return;
        }
        for (final String key : items) {
            try {
                final URI browserURI = new URI(browserAddress);
                final URI uri = new URI(browserURI.getScheme(), browserURI.getSchemeSpecificPart() + key, null);
                Desktop.getDesktop().browse(uri);
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
    }
