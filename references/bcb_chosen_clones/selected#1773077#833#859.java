    public boolean compressFile() {
        boolean returns = false;
        try {
            int BUFFER = 2048;
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(getFileName() + ".zipped");
            CheckedOutputStream checksum = new CheckedOutputStream(dest, new Adler32());
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(checksum));
            byte data[] = new byte[BUFFER];
            File _file = new File(getFileName());
            FileInputStream _inFile = new FileInputStream(_file);
            origin = new BufferedInputStream(_inFile, BUFFER);
            ZipEntry entry = new ZipEntry(getFileName());
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            origin.close();
            out.close();
            System.out.println("checksum: " + checksum.getChecksum().getValue());
            returns = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returns;
    }
