    public void connected(String address, int port) {
        connected = true;
        try {
            if (localConnection) {
                byte key[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
                si.setEncryptionKey(key);
            } else {
                saveData(address, port);
                MessageDigest mds = MessageDigest.getInstance("SHA");
                mds.update(connectionPassword.getBytes("UTF-8"));
                si.setEncryptionKey(mds.digest());
            }
            if (!si.login(username, password)) {
                si.disconnect();
                connected = false;
                showErrorMessage(this, "Authentication Failure");
                restore();
                return;
            }
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    connectionLabel.setText("");
                    progressLabel = new JLabel("Loading... Please wait.");
                    progressLabel.setOpaque(true);
                    progressLabel.setBackground(Color.white);
                    replaceComponent(progressLabel);
                    cancelButton.setEnabled(true);
                    xx.remove(helpButton);
                }
            });
        } catch (Exception e) {
            System.out.println("connected: Exception: " + e + "\r\n");
        }
        ;
    }
