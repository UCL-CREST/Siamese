    public static final void url(final String str) {
        if (java.awt.Desktop.isDesktopSupported()) {
            final java.awt.Desktop desktop = java.awt.Desktop.getDesktop();
            java.net.URI uri;
            try {
                final String strUrl = JumpTo.BASE_URL + str;
                uri = new java.net.URI(strUrl);
                desktop.browse(uri);
            } catch (final URISyntaxException e) {
                e.printStackTrace();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
    }
