        @Override
        public void actionPerformed(ActionEvent evt) {
            if (Desktop.isDesktopSupported()) {
                try {
                    URI uri = new URI(evt.getActionCommand());
                    Desktop.getDesktop().browse(uri);
                } catch (Exception e) {
                }
            }
        }
