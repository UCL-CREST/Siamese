    public static boolean zip(File[] filesToZip, File zipFile) {
        byte[] buf = new byte[2048];
        ZipOutputStream out = null;
        FileInputStream in = null;
        try {
            out = new ZipOutputStream(new FileOutputStream(zipFile));
            for (int i = 0; i < filesToZip.length; i++) {
                in = new FileInputStream(filesToZip[i]);
                out.putNextEntry(new ZipEntry(filesToZip[i].getName()));
                int len;
                while ((len = in.read(buf)) != -1) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
            out.close();
        } catch (IOException e) {
            System.err.println("Can't zip()");
            e.printStackTrace();
            safeClose(out);
            safeClose(in);
            return false;
        }
        return true;
    }
