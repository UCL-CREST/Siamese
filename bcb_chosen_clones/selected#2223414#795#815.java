    private void launchFile(final File file) {
        log.debug("file=" + file.getAbsolutePath());
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    if (Desktop.isDesktopSupported()) {
                        String name = file.getName().toLowerCase();
                        if (name.endsWith(".html") || name.endsWith(".htm")) {
                            Desktop.getDesktop().browse(file.toURI());
                        } else {
                            Desktop.getDesktop().open(file);
                        }
                    }
                } catch (Exception e) {
                    log.error("Error while launching file", e);
                }
            }
        });
    }
