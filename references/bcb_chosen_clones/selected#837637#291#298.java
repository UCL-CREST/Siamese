        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() <= 0 && !Desktop.isDesktopSupported()) return;
            try {
                Desktop.getDesktop().browse(new URI(_url));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
