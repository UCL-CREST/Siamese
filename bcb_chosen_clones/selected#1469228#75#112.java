    public final void run() {
        String serverVersion = "";
        try {
            URL version = new URL("http://www.digiextractor.de/version");
            BufferedReader in = new BufferedReader(new InputStreamReader(version.openStream()));
            serverVersion = in.readLine();
            in.close();
        } catch (IOException ioe) {
            LOGGER.log(Level.SEVERE, "Error getting Version from Server", ioe);
            return;
        }
        if (!serverVersion.matches("\\d+\\.\\d+\\.\\d+.*")) {
            if (!silentMode) {
                JOptionPane.showMessageDialog(parentFrame, Messages.getString("WebUpdater.1"), Messages.getString("WebUpdater.2"), JOptionPane.ERROR_MESSAGE);
            }
            return;
        }
        if (!serverHasNewerVersion(serverVersion)) {
            if (!silentMode) {
                JOptionPane.showMessageDialog(parentFrame, Messages.getString("WebUpdater.4"));
            }
        } else {
            Object[] options = { Messages.getString("WebUpdater.5"), Messages.getString("WebUpdater.6") };
            int n = JOptionPane.showOptionDialog(parentFrame, String.format(Messages.getString("WebUpdater.7"), "", serverVersion), Messages.getString("WebUpdater.9"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (n == JOptionPane.YES_OPTION) {
                try {
                    URI site = new URI("http://www.digiextractor.de");
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().browse(site);
                    } else {
                        JOptionPane.showMessageDialog(parentFrame, Messages.getString("WebUpdater.11"), Messages.getString("WebUpdater.12"), JOptionPane.WARNING_MESSAGE);
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.SEVERE, "Error opening Browser", e);
                }
            }
        }
    }
