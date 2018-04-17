    public void LaunchPath(String path) {
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.OPEN)) {
                try {
                    File test = new File(path);
                    if (test.isDirectory() || test.isFile()) {
                        desktop.open(new File(path));
                    } else {
                        ShortcutMain.getInstance().showMessage("Does not exist : " + path);
                    }
                } catch (IOException ex) {
                    System.out.println("IOException  ::  " + ex);
                } catch (IllegalArgumentException ex) {
                    System.out.println("IllegalArgumentException  ::  " + ex);
                }
            }
        }
    }
