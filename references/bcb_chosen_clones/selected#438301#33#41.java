        public void mouseClicked(MouseEvent event) {
            if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                return;
            }
            try {
                Desktop.getDesktop().browse(new URI(AboutInformation.getInstance().getUrl()));
            } catch (Exception e) {
            }
        }
