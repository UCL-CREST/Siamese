    private boolean checkBrowsers() throws InterruptedException, IOException {
        final int initialTimeout = 2000;
        final int stepTimeout = 500;
        System.out.println("Waiting for " + browserCount + " browser(s)");
        try {
            Thread.sleep(Math.min(initialTimeout, timeout));
            if (server.getBrowserCount() < browserCount) {
                if (startBrowser && Desktop.isDesktopSupported() && (server.getBrowserCount() == 0)) {
                    System.out.println("Starting the default browser ...");
                    Desktop.getDesktop().browse(new URL(server.getHostURL(), "/start.html").toURI());
                }
                for (int i = initialTimeout; i < timeout; i += stepTimeout) {
                    Thread.sleep(stepTimeout);
                    if (server.getBrowserCount() >= browserCount) {
                        System.out.println("Captured browsers");
                        return true;
                    }
                }
                System.err.println("Unable to capture at least " + browserCount + " browser(s)");
                return false;
            }
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        System.out.println("Have " + server.getBrowserCount() + " browsers connected");
        return true;
    }
