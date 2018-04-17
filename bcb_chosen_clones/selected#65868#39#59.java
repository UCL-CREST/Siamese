    public boolean addToZip(String filename, String entryName) {
        if (m_mode != ZM_WRITE) return false;
        try {
            FileInputStream fi = new FileInputStream(new File(filename));
            zo.putNextEntry(new ZipEntry(entryName));
            byte[] buff = new byte[10000];
            int read;
            read = fi.read(buff);
            boolean eof = (read == -1);
            while (!eof) {
                zo.write(buff, 0, read);
                read = fi.read(buff);
                eof = (read == -1);
            }
            zo.closeEntry();
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }
