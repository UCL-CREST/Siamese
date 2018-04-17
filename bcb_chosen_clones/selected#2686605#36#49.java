    private void onLink(String linkDesc) {
        if (linkDesc == null) return;
        if (linkDesc.contains("gpl")) {
            showGpl();
        } else if (linkDesc.contains("contacts")) {
            if (java.awt.Desktop.isDesktopSupported()) {
                try {
                    java.awt.Desktop.getDesktop().browse(URI.create("http://code.google.com/p/jgef/"));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
