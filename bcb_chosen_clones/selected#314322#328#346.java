    private void zipTempDirectoryToStream(OutputStream out) throws IOException {
        byte b[] = new byte[512];
        ZipOutputStream zout = new ZipOutputStream(out);
        File f = new File(Config.CONTEXT.getRealPath(backupTempFilePath));
        String[] s = f.list();
        for (int i = 0; i < s.length; i++) {
            InputStream in = new BufferedInputStream(new FileInputStream(f = new File(Config.CONTEXT.getRealPath(backupTempFilePath + "/" + s[i]))));
            ZipEntry e = new ZipEntry(s[i].replace(File.separatorChar, '/'));
            zout.putNextEntry(e);
            int len = 0;
            while ((len = in.read(b)) != -1) {
                zout.write(b, 0, len);
            }
            zout.closeEntry();
            in.close();
        }
        zout.close();
        out.close();
    }
