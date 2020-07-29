        @Override
        public void action() {
            if ((null != _uri) && Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    try {
                        desktop.browse(_uri);
                        return;
                    } catch (IOException e) {
                    }
                }
            }
            JOptionPane.showMessageDialog(MainDialog.this, "Cannot open page " + _uri);
        }
