    public static void browse(URI issueUrl) {
        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(issueUrl);
            } catch (IOException e) {
                LOG.throwing(SwingUtils.class.getName(), "open url", e);
            }
        }
    }
