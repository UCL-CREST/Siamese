    private void checkForUpdates() {
        SwingWorker<String, Object> worker = new SwingWorker<String, Object>() {

            public String doInBackground() {
                ok.setEnabled(false);
                BufferedReader in = null;
                try {
                    URL url = new URL(net.mjrz.fm.ui.utils.UIDefaults.LATEST_VERSION_URL);
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
                        return str.toString();
                    } else {
                        logger.error("Unable to retrieve latest version: HTTP ERROR CODE: " + status);
                        return "";
                    }
                } catch (Exception e) {
                    logger.error("Unable to retrieve latest version: HTTP ERROR CODE: " + e.getMessage());
                    return null;
                } finally {
                    if (in != null) {
                        try {
                            in.close();
                        } catch (Exception e) {
                            logger.error(e);
                        }
                    }
                }
            }

            public void done() {
                try {
                    dispose();
                    String str = get();
                    if (str == null || str.length() == 0) {
                        JOptionPane.showMessageDialog(parent, tr("Unable to retrieve version information.\nPlease check network connectivity"), tr("Error"), JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    net.mjrz.fm.Version v = net.mjrz.fm.Version.getVersion();
                    if (v.isVersionGreater(str.toString())) {
                        String[] args = { str };
                        String msg = form.format(args);
                        int n = JOptionPane.showConfirmDialog(parent, msg + "\n" + tr("Do you want to download the latest version?"), tr("Message"), JOptionPane.YES_NO_OPTION);
                        if (n == JOptionPane.YES_OPTION) {
                            java.awt.Desktop d = Desktop.getDesktop();
                            if (Desktop.isDesktopSupported()) {
                                d.browse(new URI("http://www.mjrz.net/dl.html"));
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(UpdateCheckDialog.this, tr("No new updates are available"));
                    }
                } catch (Exception e) {
                    logger.error(MiscUtils.stackTrace2String(e));
                }
            }
        };
        worker.execute();
    }
