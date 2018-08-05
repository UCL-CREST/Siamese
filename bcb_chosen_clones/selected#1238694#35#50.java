        @Override
        public void mouseClicked(MouseEvent e) {
            String link = fixedLink;
            if (link == null) {
                link = JHyperlinkLabel.this.getText();
            }
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                try {
                    desktop.browse(URI.create(link));
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, null, ex);
                }
            } else {
            }
        }
