    public static void openURL(String url) {
        Desktop desktop;
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
            try {
                if (url.toLowerCase().startsWith("mailto")) {
                    desktop.mail(new URL(url).toURI());
                } else {
                    desktop.browse(new URL(url).toURI());
                }
            } catch (Exception e) {
                logger.error("Error when trying to open URL: " + e.getMessage(), e);
                ErrorDialogHandler.showErrorDialog(NavigationPanel.getInstance(), ErrorLevel.FATAL, errMsg, "URL not available: " + e.getMessage(), "COMPONENT", e);
            }
        } else {
            String osName = System.getProperty("os.name");
            try {
                if (osName.startsWith("Mac OS")) {
                    Class fileMgr = Class.forName("com.apple.eio.FileManager");
                    Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] { String.class });
                    openURL.invoke(null, new Object[] { url });
                } else if (osName.startsWith("Windows")) {
                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
                } else {
                    boolean found = false;
                    for (String browser : browsers) {
                        if (!found) {
                            found = Runtime.getRuntime().exec(new String[] { "which", browser }).waitFor() == 0;
                            if (found) {
                                Runtime.getRuntime().exec(new String[] { browser, url });
                            }
                        }
                    }
                    if (!found) {
                        throw new Exception(Arrays.toString(browsers));
                    }
                }
            } catch (Exception e) {
                logger.error("Error when trying to open web page: " + e.getMessage(), e);
                ErrorDialogHandler.showErrorDialog(NavigationPanel.getInstance(), ErrorLevel.FATAL, errMsg, "Web page not available - could not load web browser: " + e.getMessage(), "COMPONENT", e);
            }
        }
    }
