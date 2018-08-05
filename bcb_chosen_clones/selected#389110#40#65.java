    public static void saveZipFile(String srFile, String dtFile) {
        try {
            BufferedInputStream origin = null;
            OutputStream dest = new FileOutputStream(dtFile);
            CheckedOutputStream checksum = new CheckedOutputStream(dest, new Adler32());
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(checksum));
            byte data[] = new byte[BUFFER];
            File f = new File(srFile);
            String[] files = f.list();
            for (int i = 0; i < files.length; i++) {
                String fullname = f.getName() + "/" + files[i];
                InputStream fi = new FileInputStream(fullname);
                origin = new BufferedInputStream(fi, BUFFER);
                ZipEntry entry = new ZipEntry(fullname);
                out.putNextEntry(entry);
                int count;
                while ((count = origin.read(data, 0, BUFFER)) != -1) {
                    out.write(data, 0, count);
                }
                origin.close();
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
