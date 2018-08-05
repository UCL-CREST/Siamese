        @Override
        public void mouseClicked(final MouseEvent e) {
            if (Desktop.isDesktopSupported()) {
                final Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(new URI(uri));
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                } catch (URISyntaxException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
