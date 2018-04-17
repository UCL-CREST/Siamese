        @Override
        public void mouseClicked(final MouseEvent e) {
            if (Desktop.isDesktopSupported()) {
                final Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(new URI(uri));
                } catch (final IOException ex) {
                    Exceptions.printStackTrace(ex);
                } catch (final URISyntaxException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
