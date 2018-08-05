    private void open(URI uri) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            try {
                desktop.browse(uri);
            } catch (IOException e) {
                p("IOException ");
                e.printStackTrace();
            }
        } else {
            System.out.println("Desktop.isDesktopSupported() - NOT");
        }
    }
