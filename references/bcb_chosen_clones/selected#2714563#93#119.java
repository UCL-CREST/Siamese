    @Override
    public void actionPerformed(final ActionEvent ae) {
        final Object source = ae.getSource();
        if (menuBar.getNewItem() == source) {
            productsTableModel.add(String.valueOf(System.currentTimeMillis()), String.valueOf(System.currentTimeMillis()));
            return;
        }
        if (menuBar.getExitItem() == source) {
            System.exit(0);
            return;
        }
        if (menuBar.getHelpItem() == source) {
            if (Desktop.isDesktopSupported()) {
                final Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(new URI(HELP_URI));
                } catch (final IOException e) {
                    logger.error("Could not launch the default browser for your system", e);
                } catch (final URISyntaxException e) {
                    logger.error("Invalid URI " + HELP_URI, e);
                }
            } else {
                logger.inform("Unfortunately your system doesn't support Java SE 6 Desktop API");
            }
            return;
        }
    }
