    private void openStorageFolder() {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.OPEN)) {
                try {
                    desktop.open(new File(juicyGet.getStorageFolder()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
