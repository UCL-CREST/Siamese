        public void actionPerformed(ActionEvent e) {
            if (Desktop.isDesktopSupported()) {
                try {
                    Desktop.getDesktop().browse(this.uri);
                } catch (IOException err) {
                }
            }
        }
