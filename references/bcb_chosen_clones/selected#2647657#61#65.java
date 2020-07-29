    public void openUrl(URI uri) throws IOException {
        if (!Desktop.isDesktopSupported()) throw new IOException("Desktop not supported");
        Desktop desktop = Desktop.getDesktop();
        desktop.browse(uri);
    }
