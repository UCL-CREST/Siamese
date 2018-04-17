    private void writeToFileAndOpen(Document document, String fileName) throws IOException {
        File outFile = new File(fileName);
        outFile.getParentFile().mkdirs();
        document.save(outFile);
        if (Desktop.isDesktopSupported()) {
            if (Desktop.getDesktop().isSupported(Action.OPEN)) {
                try {
                    Desktop.getDesktop().open(outFile);
                } catch (IOException e) {
                }
            }
        }
    }
