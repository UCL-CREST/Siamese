    public static void buildDeb(File debFile, File controlFile, File dataFile) throws IOException {
        long now = new Date().getTime() / 1000;
        OutputStream deb = new FileOutputStream(debFile);
        deb.write("!<arch>\n".getBytes());
        startFileEntry(deb, DEBIAN_BINARY_NAME, now, DEBIAN_BINARY_CONTENT.length());
        deb.write(DEBIAN_BINARY_CONTENT.getBytes());
        endFileEntry(deb, DEBIAN_BINARY_CONTENT.length());
        startFileEntry(deb, CONTROL_NAME, now, controlFile.length());
        FileInputStream control = new FileInputStream(controlFile);
        byte[] buffer = new byte[1024];
        while (true) {
            int read = control.read(buffer);
            if (read == -1) break;
            deb.write(buffer, 0, read);
        }
        control.close();
        endFileEntry(deb, controlFile.length());
        startFileEntry(deb, DATA_NAME, now, dataFile.length());
        FileInputStream data = new FileInputStream(dataFile);
        while (true) {
            int read = data.read(buffer);
            if (read == -1) break;
            deb.write(buffer, 0, read);
        }
        data.close();
        endFileEntry(deb, dataFile.length());
        deb.close();
    }
