    private void genUI() {
        showTestAPIButton.setIcon(ResourceManager.getInstance().getImageIcon("icons/testAPIDoc"));
        showTestAPIButton.setToolTipText("Show test API documentation");
        showTestAPIButton.setVisible(true);
        showTestAPIButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String filename = StaticConfiguration.TEST_API_DOC_DIR + File.separator + "index.html";
                File resultsFile = new File(filename);
                try {
                    if (Desktop.isDesktopSupported()) {
                        Desktop.getDesktop().open(resultsFile);
                    } else {
                        logger.error("Feature not supported by this platform");
                    }
                } catch (IOException ex) {
                    logger.error("Could not open " + filename);
                }
            }
        });
        this.add(showTestAPIButton);
    }
