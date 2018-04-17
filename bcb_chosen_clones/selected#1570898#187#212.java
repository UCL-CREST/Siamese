    private void openBrowser(String string) {
        Desktop desktop;
        if (Desktop.isDesktopSupported()) {
            desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    URI url = null;
                    if (string.contains("http")) {
                        url = new URI(string);
                        System.out.println("[" + string + "]");
                    } else {
                        File index = new File(string);
                        String path = index.getAbsolutePath();
                        path = "file:///" + encodePath(path);
                        url = new URI(path);
                        System.out.println("[" + path + "]");
                    }
                    desktop.browse(url);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
