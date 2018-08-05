    public void doImport(File f, boolean checkHosp) throws Exception {
        connector.getConnection().setAutoCommit(false);
        File base = f.getParentFile();
        ZipInputStream in = new ZipInputStream(new FileInputStream(f));
        ZipEntry entry;
        while ((entry = in.getNextEntry()) != null) {
            String outFileName = entry.getName();
            File outFile = new File(base, outFileName);
            OutputStream out = new FileOutputStream(outFile, false);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) out.write(buf, 0, len);
            out.close();
        }
        in.close();
        importDirectory(base, checkHosp);
        connector.getConnection().commit();
    }
