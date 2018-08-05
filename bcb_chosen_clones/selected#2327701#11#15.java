    public static void openFileWithDesktopApp(File cropDestinationFile) throws IOException {
        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(cropDestinationFile);
        }
    }
