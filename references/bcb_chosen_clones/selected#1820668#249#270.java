    private void zipAndEmailData() throws IOException {
        byte b[] = new byte[512];
        ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipDataFileName));
        InputStream in = new FileInputStream(GAME_DATA_FILENAME);
        ZipEntry e = new ZipEntry(GAME_DATA_FILENAME.replace(File.separatorChar, '/'));
        zout.putNextEntry(e);
        int len = 0;
        while ((len = in.read(b)) != -1) {
            zout.write(b, 0, len);
        }
        zout.closeEntry();
        in = new FileInputStream(TURN_DATA_FILENAME);
        e = new ZipEntry(TURN_DATA_FILENAME.replace(File.separatorChar, '/'));
        zout.putNextEntry(e);
        len = 0;
        while ((len = in.read(b)) != -1) {
            zout.write(b, 0, len);
        }
        zout.closeEntry();
        zout.close();
        new Email(null, players.getNextViewingPlayer().address(), "Player " + players.viewingPlayerNumber() + " has completed his turn", DEFAULT_MESSAGE_BODY, zipDataFileName, Configs.readConfigsFromFile(), false);
    }
