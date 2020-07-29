    private void checkForUpdates() throws Exception {
        URL url = new URL(UIDefaults.LATEST_VERSION_URL);
        BufferedReader in = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            int status = conn.getResponseCode();
            if (status == 200) {
                in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder str = new StringBuilder();
                while (true) {
                    String line = in.readLine();
                    if (line == null) break;
                    str.append(line);
                }
                net.mjrz.fm.Version v = net.mjrz.fm.Version.getVersion();
                if (v.isVersionGreater(str.toString())) {
                    int n = JOptionPane.showConfirmDialog(this, "A updated version is available\nDo you want to download the latest version?", "Message", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (n == JOptionPane.YES_OPTION) {
                        java.awt.Desktop d = Desktop.getDesktop();
                        if (Desktop.isDesktopSupported()) {
                            d.browse(new URI("http://www.ifreebudget.com/dl.html"));
                        }
                    }
                } else {
                    SwingUtilities.invokeLater(new Runnable() {

                        public void run() {
                            JOptionPane.showMessageDialog(AboutDialog.this, "There are no new updates");
                        }
                    });
                }
            } else {
                SwingUtilities.invokeLater(new Runnable() {

                    public void run() {
                        JOptionPane.showMessageDialog(AboutDialog.this, "Unable to get latest version, Please check network connection", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                });
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
