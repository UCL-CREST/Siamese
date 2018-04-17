    private void foundNewVersion() {
        updater = new UpdaterView();
        updater.setLabelText("Initiating Updater...");
        updater.setProgress(0);
        updater.setLocationRelativeTo(null);
        updater.setVisible(true);
        URL pathUrl = ClassLoader.getSystemResource("img/icon.png");
        String path = pathUrl.toString();
        path = path.substring(4, path.length() - 14);
        try {
            file = new File(new URI(path));
            updaterFile = new File(new URI(path.substring(0, path.length() - 4) + "Updater.jar"));
            if (updaterFile.exists()) {
                updaterFile.delete();
            }
            updater.setProgress(25);
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    try {
                        FileChannel in = (new FileInputStream(file)).getChannel();
                        FileChannel out = (new FileOutputStream(updaterFile)).getChannel();
                        in.transferTo(0, file.length(), out);
                        updater.setProgress(50);
                        in.close();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    startUpdater();
                }
            });
        } catch (URISyntaxException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Update error! Could not create Updater. Check folder permission.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
