    public void getBrowser() {
        if (Desktop.isDesktopSupported()) {
            Desktop desk = Desktop.getDesktop();
            if (desk.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desk.browse(new URI("http://code.google.com/p/gpsnutzung/"));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
    }
