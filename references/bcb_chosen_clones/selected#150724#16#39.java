    public boolean doZipFile(String fileName, String outfile, String zipentry) {
        BufferedInputStream origin = null;
        try {
            if (!(new File(fileName)).isFile()) return false;
            FileOutputStream dest = new FileOutputStream(outfile);
            CheckedOutputStream checksum = new CheckedOutputStream(dest, new Adler32());
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(checksum));
            byte data[] = new byte[BUFFER];
            FileInputStream fi = new FileInputStream(fileName);
            origin = new BufferedInputStream(fi, BUFFER);
            ZipEntry entry = new ZipEntry(FileUtil.getFileName(zipentry));
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            origin.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
