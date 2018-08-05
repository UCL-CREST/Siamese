    public static void open(String toOpen) {
        if (toOpen == null) throw new NullPointerException();
        if (!Desktop.isDesktopSupported()) return;
        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.open(new File(toOpen));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
