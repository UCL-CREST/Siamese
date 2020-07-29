            public void actionPerformed(ActionEvent e) {
                boolean showTestSuiteReport = resultsPane.getCurrentRunName().equals("Run1");
                String resDir = TestEngineConfiguration.getInstance().getString("reporting.generated_report_path");
                String baseDir = System.getProperty("user.dir");
                String filename = baseDir + File.separator + resDir + File.separator + (showTestSuiteReport ? "index.html" : "campaign.html");
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
