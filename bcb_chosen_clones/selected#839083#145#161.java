    public void zipFile(ZipOutputStream zout, String dirName, String fname) throws LTSException {
        File f = new File(dirName, fname);
        try {
            ZipEntry zentry = new ZipEntry(fname);
            zout.putNextEntry(zentry);
            FileInputStream fis = new FileInputStream(f);
            byte[] buf = new byte[4096];
            int count = 0;
            do {
                count = fis.read(buf);
                if (count > 0) zout.write(buf, 0, count);
            } while (count > 0);
            fis.close();
        } catch (IOException e) {
            throw new LTSException("Error reading file, " + f.toString(), e);
        }
    }
