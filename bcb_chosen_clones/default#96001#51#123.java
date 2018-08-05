    public StatusWindow(JFrame jframe) {
        this.frame = jframe;
        closeButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                frame.setVisible(false);
                running = false;
            }
        });
        Thread downloadProgressThread = new Thread() {

            public void run() {
                while (running) {
                    killTransferIfTransferDied();
                    setStatusComponents();
                    updateCompletedTable();
                    System.out.print(".");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        downloadProgressThread.start();
        abortCurrentDownloadButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                XDCCConnectionManager.currentFileTransfer.close();
            }
        });
        openFileButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                Desktop desktop = null;
                if (Desktop.isDesktopSupported()) {
                    desktop = Desktop.getDesktop();
                    try {
                        desktop.open(XDCCConnectionManager.completedFiles.get(finishedFiles.getSelectedRow()));
                    } catch (IOException e) {
                        DialogBuilder.showErrorDialog("Error", "Could not open file!");
                    }
                }
            }
        });
        openDirectoryButton.addActionListener(new ActionListener() {

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
        });
        removeFromListButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                XDCCConnectionManager.completedFiles.remove(finishedFiles.getSelectedRow());
            }
        });
        clearAllButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent actionEvent) {
                XDCCConnectionManager.completedFiles.clear();
            }
        });
    }
