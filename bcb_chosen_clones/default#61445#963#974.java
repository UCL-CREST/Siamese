        public void actionPerformed(ActionEvent ae) {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop d = Desktop.getDesktop();
                    d.browse(new URI(JCards.SITE_URL));
                } catch (Exception e) {
                    Methods.displaySimpleError(framer, "Can't access website; check internet connection?\nPlease visit '" + JCards.GPL_URL + "' to view GPL");
                }
            } else {
                Methods.displaySimpleAlert(framer, "Please visit '" + JCards.SITE_URL + "' to check latest version");
            }
        }
