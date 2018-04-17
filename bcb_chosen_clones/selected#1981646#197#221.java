    public void writeArchive(File archive) {
        String dpPath = getPath();
        String[] filenames = new String[] { "datapackage.xml", "routes.xml", "stations.xml", "streetalias.xml" };
        byte[] buf = new byte[1024];
        try {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(archive));
            zos.setLevel(Deflater.DEFAULT_COMPRESSION);
            for (int i = 0; i < filenames.length; i++) {
                FileInputStream in = new FileInputStream(dpPath + filenames[i]);
                zos.putNextEntry(new ZipEntry(filenames[i]));
                int len;
                while ((len = in.read(buf)) > 0) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                in.close();
                System.out.println("wrote " + filenames[i]);
            }
            FPUtil.zipDir(dpPath + "route", zos, dpPath.length());
            legDB_.dumpToZip(zos);
            zos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
