    public static void launchDirectory(String dir) {
        boolean worked = false;
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().open(new File(dir));
                worked = true;
            }
        } catch (IOException ioe) {
            worked = false;
        }
        if (worked) return;
        if (Main.OS.startsWith("Windows")) {
            try {
                Runtime.getRuntime().exec(new String[] { "explorer", dir });
            } catch (IOException ioe) {
                System.out.println("Got erroragain : " + ioe.getMessage());
            }
        } else {
            String[] viewers = new String[] { "konqueror", "nautilus", "thunar", "emelfm", "rox-filer", "pcmanfm", "xnc", "mc" };
            try {
                for (int i = 0; i < viewers.length; i++) {
                    try {
                        if (Runtime.getRuntime().exec(new String[] { "which", viewers[i] }).waitFor() == 0) {
                            Runtime.getRuntime().exec(new String[] { viewers[i], "file:" + dir });
                            break;
                        }
                    } catch (InterruptedException ie) {
                        System.out.println("Got error: " + ie.getMessage());
                    }
                }
            } catch (IOException ioe) {
                System.out.println("Got error again: " + ioe.getMessage());
            }
        }
    }
