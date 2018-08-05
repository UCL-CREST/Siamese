    @Override
    public void launchURL(final String pURL, final String pTarget) {
        LOG.debug("Launching URL with Java 6 \"" + pURL + "\", target \"" + pTarget + "\"");
        if (Desktop.isDesktopSupported()) {
            try {
                final URI lUri = new URI(pURL);
                Desktop.getDesktop().browse(lUri);
            } catch (final URISyntaxException e) {
                LOG.error("Invalid URL", e);
            } catch (final IOException e) {
                LOG.error("Error!", e);
            }
        } else {
            LOG.error("Desktop not supported");
        }
    }
