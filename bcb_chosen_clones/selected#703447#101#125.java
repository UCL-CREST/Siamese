    private static void openWebadmin(int p) {
        if (Desktop.isDesktopSupported()) {
            log.info("java desktop is supported");
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                log.info("java desktop browse is supported");
                URI uri;
                try {
                    uri = new URI("http://localhost:" + p + "/web/index.html");
                    try {
                        log.info("opening browser with URI:" + uri);
                        desktop.browse(uri);
                    } catch (IOException e) {
                        log.error("exception opening browser", e);
                    }
                } catch (URISyntaxException e) {
                    log.error("exception constructing webadmin URI", e);
                }
            } else {
                log.info("java desktop browse is not supported");
            }
        } else {
            log.info("java desktop is not supported");
        }
    }
