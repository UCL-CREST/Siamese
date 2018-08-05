    public static void openURI(URI address) throws UnableToLaunchBrowserException {
        if (Desktop.isDesktopSupported()) {
            Desktop appDesktop = getDesktop();
            try {
                appDesktop.browse(address);
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } else {
            OperatingSystem system = getOperatingSystem();
            launchFallback(system, address);
        }
    }
