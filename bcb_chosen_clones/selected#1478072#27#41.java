    @Override
    public void actionPerformed(ActionEvent e) {
        if (!Desktop.isDesktopSupported()) {
            log.warn("Desktop is not supported.");
            return;
        }
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.browse(new URI(messageUrl));
        } catch (IOException e1) {
            log.error("Can not open url " + messageUrl + " in default browser");
        } catch (URISyntaxException e1) {
            log.error("Can not obtain URI of URL: " + messageUrl);
        }
    }
