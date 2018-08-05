    private void checkServerStarted() {
        if (checkDone) {
            return;
        }
        try {
            URL jsServerURL = new URL("http://localhost:9876");
            if (jsServerURL.getHost().equals("localhost")) {
                HttpServer server = new HttpServer();
                if (ping(server, jsServerURL)) {
                    return;
                }
                System.out.println("Starting JS Test Driver server ...");
                String[] args = { "--port", "9876", "--verbose" };
                JsTestDriver.main(args);
                System.out.println("Server started");
                Thread.sleep(1000);
                if (checkConnectedBrowsers(server, jsServerURL).equals(EMPTY_BROWSER_LIST)) {
                    if (Desktop.isDesktopSupported()) {
                        System.out.println("Capturing the default browser ...");
                        Desktop.getDesktop().browse(new URL(jsServerURL, "/capture?id=100&timeout=10").toURI());
                        for (int i = 0; i < 10; ++i) {
                            Thread.sleep(500);
                            String browserList = checkConnectedBrowsers(server, jsServerURL);
                            if (!browserList.equals(EMPTY_BROWSER_LIST)) {
                                System.out.println("Captured browsers:" + browserList);
                                return;
                            }
                        }
                        System.out.println("Unable to capture a browser");
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            checkDone = true;
        }
    }
