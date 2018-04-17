    private void btn_acceptActionPerformed(java.awt.event.ActionEvent evt) {
        if (btn_accept.getText().equals("Accept")) {
            startDownloading();
            Protocol.acceptTransfer(sender, filename, firstByteToSend);
            btn_accept.setText("Open file");
            btn_accept.setEnabled(false);
            btn_refuse.setText("Cancel");
            cb_Resume.setEnabled(false);
            tf_savePath.setEnabled(false);
            btn_browseSavePath.setEnabled(false);
        } else {
            if (destfile != null) {
                Desktop desktop = null;
                if (Desktop.isDesktopSupported()) {
                    desktop = Desktop.getDesktop();
                    try {
                        desktop.open(destfile.getCanonicalFile());
                    } catch (Exception e) {
                    }
                }
            }
        }
    }
