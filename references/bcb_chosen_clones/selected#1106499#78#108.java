    public void setPage(URL url) {
        try {
            if (url.getProtocol().startsWith("http")) {
                if (java.awt.Desktop.isDesktopSupported()) {
                    try {
                        java.awt.Desktop.getDesktop().browse(url.toURI());
                    } catch (URISyntaxException e) {
                        e.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, url.toExternalForm(), "Please open this url:", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                if (history.size() > 50) {
                    history.remove(0);
                }
                if (history.isEmpty() || url != history.lastElement()) {
                    history.add(url);
                    historyIndex = history.size() - 1;
                    forw.setEnabled(false);
                    if (history.size() > 1) {
                        back.setEnabled(true);
                    }
                }
                editorpane.setPage(url);
                setBase();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
