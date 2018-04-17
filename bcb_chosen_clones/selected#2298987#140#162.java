    public void browseURIImpl(final URI uri, final IApplication app) throws Exception {
        boolean doneWithDesktopAPI = false;
        if (useDesktopAPI && Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(uri);
                    doneWithDesktopAPI = true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!doneWithDesktopAPI) {
            String browserCommand = app.getBrowserCommand();
            if (browserCommand == null) {
                browserCommand = pathSearcher.findInPath(BROWSER_EXES, FALLBACK_BROWSER);
                browserCommand += " \"%url%\"";
            }
            String cmd = StringHelper.replaceAll(browserCommand, URL_PATTERN, uri.toURL().toExternalForm());
            runBrowserCommand(cmd);
        }
    }
