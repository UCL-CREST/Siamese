    @Override
    public void runCode() {
        if (Desktop.isDesktopSupported()) {
            final Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(new URI(HELP_URI));
            } catch (final IOException e) {
                LogEngine.error("Could not launch the default browser for your system", e);
            } catch (final URISyntaxException e) {
                LogEngine.error("Invalid URI " + HELP_URI, e);
            }
        } else {
            LogEngine.inform("Unfortunately your system doesn't support Java SE 6 Desktop API", null);
        }
    }
