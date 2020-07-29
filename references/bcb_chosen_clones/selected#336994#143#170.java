            public void done() {
                try {
                    dispose();
                    String str = get();
                    if (str == null || str.length() == 0) {
                        JOptionPane.showMessageDialog(frame, tr("Unable to retrieve version information.\nPlease check network connectivity"), tr("Error"), JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    net.mjrz.fm.Version v = net.mjrz.fm.Version.getVersion();
                    if (v.isVersionGreater(str.toString())) {
                        String[] args = { str };
                        String msg = form.format(args);
                        int n = JOptionPane.showConfirmDialog(frame, msg + "\n" + tr("Do you want to download the latest version?"), tr("Message"), JOptionPane.YES_NO_OPTION);
                        if (n == JOptionPane.YES_OPTION) {
                            java.awt.Desktop d = Desktop.getDesktop();
                            if (Desktop.isDesktopSupported()) {
                                d.browse(new URI(UIDefaults.PRODUCT_DOWNLOAD_URL));
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, tr("No new updates are available"));
                    }
                } catch (Exception e) {
                    logger.error(MiscUtils.stackTrace2String(e));
                } finally {
                    frame.hideSheet();
                }
            }
