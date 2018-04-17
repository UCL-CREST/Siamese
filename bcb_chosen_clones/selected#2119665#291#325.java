    private void UploadActionPerformed(ActionEvent evt) {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("*.jpg", "jpg");
        chooser.setFileFilter(filter);
        File dir = new File(System.getProperty("user.home"));
        chooser.setCurrentDirectory(dir);
        Component parent = null;
        int returnVal = chooser.showOpenDialog(parent);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            UserStatus.setText("Bitte warten");
        }
        try {
            Pic.setVisible(false);
            FTPClient client = new FTPClient();
            client.connect("showus.de");
            client.login("web2", "kcinnay88");
            client.enterLocalActiveMode();
            client.setFileTransferMode(FTPClient.BINARY_FILE_TYPE);
            client.setFileType(FTPClient.BINARY_FILE_TYPE);
            int reply = client.getReplyCode();
            System.out.println("Connect returned: " + reply);
            FileInputStream in = new FileInputStream(chooser.getSelectedFile().getAbsolutePath());
            System.out.println("Uploading File");
            client.storeFile("/html/Applet/user/" + Config.id + ".jpg", in);
            client.logout();
            in.close();
            System.out.println("done");
            UserStatus.setText("Upload fertig, Bild wird aktuallisiert");
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            UserStatus.setText("Fehler beim Upload");
            e.printStackTrace();
        }
    }
