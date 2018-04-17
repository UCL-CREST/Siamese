    public boolean addEntry(File file) {
        if (file == null) {
            log.warning("addEntry - No File");
            return false;
        }
        if (!file.exists() || file.isDirectory()) {
            log.warning("addEntry - not added - " + file + ", Exists=" + file.exists() + ", Directory=" + file.isDirectory());
            return false;
        }
        log.fine("addEntry - " + file);
        String name = file.getName();
        byte[] data = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024 * 8];
            int length = -1;
            while ((length = fis.read(buffer)) != -1) {
                os.write(buffer, 0, length);
            }
            fis.close();
            data = os.toByteArray();
            os.close();
        } catch (IOException ioe) {
            log.log(Level.SEVERE, "addEntry (file)", ioe);
        }
        return addEntry(name, data);
    }
