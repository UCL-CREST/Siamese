    @SuppressWarnings("unchecked")
    public void processOutput(File file) throws IOException {
        ZipOutputStream zipOut = null;
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            zipOut = new ZipOutputStream(out);
            zipOut.setLevel(1);
            Enumeration keys = zipentries.keys();
            while (keys.hasMoreElements()) {
                Object key = keys.nextElement();
                byte[] data = (byte[]) zipentries.get(key);
                ZipEntry entry = new ZipEntry((String) key);
                zipOut.putNextEntry(entry);
                zipOut.write(data, 0, data.length);
            }
        } finally {
            try {
                zipOut.close();
            } catch (Exception e) {
            }
            try {
                out.close();
            } catch (Exception e) {
            }
        }
    }
