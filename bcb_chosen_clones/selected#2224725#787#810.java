    public void compress(String filename) {
        try {
            File fin = new File(filename);
            FileInputStream uncompressed = new FileInputStream(fin);
            File fout = new File(filename + ".zip");
            ZipOutputStream compressed = new ZipOutputStream(new FileOutputStream(fout));
            String shortname;
            int lastslash = filename.lastIndexOf(FSEP);
            if (lastslash != -1) {
                shortname = filename.substring(lastslash + 1);
            } else {
                shortname = filename;
            }
            compressed.putNextEntry(new ZipEntry(shortname));
            byte[] buf = new byte[4096];
            int len;
            while ((len = uncompressed.read(buf)) > 0) {
                compressed.write(buf, 0, len);
            }
            compressed.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
