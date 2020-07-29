    public static void openURL(String url) {
        try {
            if (Desktop.isDesktopSupported()) Desktop.getDesktop().browse(new URI(url));
        } catch (IOException ex) {
        } catch (URISyntaxException ex) {
        } catch (NoClassDefFoundError ncfe) {
        }
    }
