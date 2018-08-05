        @Override
        public void actionPerformed(ActionEvent e) {
            NULogger.getLogger().info("Goto Download URL clicked..");
            for (int selectedrow : selectedrows) {
                String url = table.getValueAt(selectedrow, NUTableModel.DOWNLOADURL).toString();
                if (!Desktop.isDesktopSupported()) {
                    return;
                }
                try {
                    NULogger.getLogger().log(Level.INFO, "Opening url in browser: {0}", url);
                    Desktop.getDesktop().browse(new URI(url));
                } catch (Exception ex) {
                    NULogger.getLogger().log(Level.WARNING, "{0}: Cannot load url: {1}", new Object[] { getClass().getName(), url });
                    System.err.println(ex);
                }
            }
        }
