        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (Desktop.isDesktopSupported()) {
                    if (workspace.getDataSource().length() > 0) {
                        Desktop.getDesktop().open(new File(workspace.getDataSource()));
                    }
                }
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
