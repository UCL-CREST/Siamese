    private static void recZip(ZipOutputStream zos, File zipBaseDir, String relPath) throws IOException {
        File f = new File(zipBaseDir, relPath);
        if (f.exists()) {
            if (f.isDirectory()) {
                String[] flist = f.list();
                for (int i = 0; i < flist.length; i++) {
                    String childRelPath = relPath.length() > 0 ? relPath + File.separator + flist[i] : flist[i];
                    recZip(zos, zipBaseDir, childRelPath);
                }
            } else {
                InputStream in = new BufferedInputStream(new FileInputStream(f));
                zos.putNextEntry(new ZipEntry(relPath));
                int len;
                byte[] buf = new byte[1024];
                while ((len = in.read(buf)) > 0) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                in.close();
            }
        }
    }
