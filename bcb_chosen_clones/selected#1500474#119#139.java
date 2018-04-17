    private static void writeZipBytes(int rootLength, File file, ZipOutputStream out) throws Exception {
        if (file.isFile()) {
            final int BUFFER = 2048;
            byte data[] = new byte[BUFFER];
            FileInputStream fin = new FileInputStream(file);
            BufferedInputStream original = new BufferedInputStream(fin, BUFFER);
            String entryName = file.getAbsolutePath().substring(rootLength + 1);
            ZipEntry entry = new ZipEntry(entryName);
            out.putNextEntry(entry);
            int readed;
            while ((readed = original.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, readed);
            }
            original.close();
        } else {
            File[] files = file.listFiles();
            for (File _file : files) {
                writeZipBytes(rootLength, _file, out);
            }
        }
    }
