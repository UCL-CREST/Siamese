    private static void addFilesToZip(ZipOutputStream out, File[] files) {
        byte[] buf = new byte[1024];
        if (files == null) {
            return;
        }
        for (int j = 0; j < files.length; j++) {
            File file = files[j];
            FileInputStream in;
            try {
                in = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                continue;
            }
            try {
                ZipEntry entry = new ZipEntry(file.getName());
                entry.setTime(file.lastModified());
                out.putNextEntry(entry);
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
