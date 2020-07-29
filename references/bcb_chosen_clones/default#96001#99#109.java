            public void actionPerformed(ActionEvent actionEvent) {
                Desktop desktop = null;
                if (Desktop.isDesktopSupported()) {
                    desktop = Desktop.getDesktop();
                    try {
                        desktop.open(XDCCConnectionManager.completedFiles.get(finishedFiles.getSelectedRow()).getParentFile());
                    } catch (IOException e) {
                        DialogBuilder.showErrorDialog("Error", "Could not open folder!");
                    }
                }
            }
