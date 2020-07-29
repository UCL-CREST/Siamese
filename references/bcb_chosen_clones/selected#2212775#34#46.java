        @Override
        public void actionPerformed(ActionEvent e) {
            if (Desktop.isDesktopSupported()) {
                final Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(new URI(HTTP_EULERGUI_SVN_SOURCEFORGE + "/html/documentation.html"));
                } catch (final IOException e1) {
                    e1.printStackTrace();
                } catch (final URISyntaxException e1) {
                    e1.printStackTrace();
                }
            }
        }
