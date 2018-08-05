    public static void desktopOpenAction(Frame fr, File filename) {
        Cursor oldCursor = fr.getCursor();
        fr.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            if (!Desktop.isDesktopSupported()) return;
            Desktop desktop = Desktop.getDesktop();
            File absPath = filename.getCanonicalFile();
            desktop.open(absPath);
            fr.setCursor(oldCursor);
        } catch (Throwable ex) {
            fr.setCursor(oldCursor);
            ErrDialog.errorDialog(fr, ErrUtils.getExceptionMessage(ex));
        }
    }
