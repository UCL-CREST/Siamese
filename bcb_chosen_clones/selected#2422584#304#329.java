    public static void zipFile(File nf, ZipOutputContainer outc) throws IOException {
        ZipEntry ze = null;
        String p = null;
        InputStream b = null;
        ZipOutputStream out = null;
        String zfn;
        try {
            zfn = nf.toString();
            int i = zfn.lastIndexOf("/");
            if (i < 0) i = zfn.lastIndexOf("\\");
            if (i > -1) zfn = zfn.substring(i + 1, zfn.length());
            ze = new ZipEntry(zfn);
            ze.setTime(nf.lastModified());
            b = new FileInputStream(nf);
            out = outc.getOut();
            out.putNextEntry(ze);
            StreamUtil.write(b, out);
            b.close();
            out.closeEntry();
        } finally {
            if (out != null) try {
                out.close();
            } catch (Exception io) {
            }
        }
    }
