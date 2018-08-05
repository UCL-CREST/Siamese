    @Override
    public void addSeries(MediaSeries series) {
        if (series instanceof DicomVideoSeries || series instanceof DicomEncapDocSeries) {
            if (AbstractProperties.OPERATING_SYSTEM.startsWith("linux")) {
                FileExtractor extractor = (FileExtractor) series;
                File file = extractor.getExtractFile();
                if (file != null) {
                    try {
                        String cmd = String.format("xdg-open %s", file.getAbsolutePath());
                        Runtime.getRuntime().exec(cmd);
                    } catch (IOException e1) {
                        LOGGER.error("Cannot open {} with the default system application", file.getName());
                    }
                }
            } else if (AbstractProperties.OPERATING_SYSTEM.startsWith("win")) {
                FileExtractor extractor = (FileExtractor) series;
                File file = extractor.getExtractFile();
                startAssociatedProgram(file.getAbsolutePath());
            } else if (Desktop.isDesktopSupported()) {
                final Desktop desktop = Desktop.getDesktop();
                if (desktop.isSupported(Desktop.Action.OPEN)) {
                    FileExtractor extractor = (FileExtractor) series;
                    File file = extractor.getExtractFile();
                    if (file != null) {
                        try {
                            desktop.open(file);
                        } catch (IOException e1) {
                            LOGGER.error("Cannot open {} with the default system application", file.getName());
                        }
                    }
                }
            }
        }
    }
