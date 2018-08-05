    private void compressDirectory(String directoryPath) {
        byte[] buffer = new byte[4096];
        byte[] extra = new byte[0];
        File dir = new File(directoryPath);
        int bytes_read;
        try {
            if (dir.isDirectory()) {
                String[] entries = dir.list();
                if (entries.length == 0) {
                    ZipEntry entry = new ZipEntry(dir.getPath() + "/");
                    out.putNextEntry(entry);
                }
                for (int i = 0; i < entries.length; i++) {
                    File f = new File(dir, entries[i]);
                    compressDirectory(f.getAbsolutePath());
                }
            } else {
                in = new FileInputStream(dir);
                ZipEntry entry = new ZipEntry(dir.getPath());
                out.putNextEntry(entry);
                while ((bytes_read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytes_read);
                }
                in.close();
            }
        } catch (Exception c) {
            c.printStackTrace();
        }
    }
