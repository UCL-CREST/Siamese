    public static void zipDir(File zipDir, ZipOutputStream zos, String path, ProgressMonitor pm, Map filter) {
        try {
            String[] dirList = zipDir.list();
            byte[] readBuffer = new byte[2156];
            int bytesIn = 0;
            for (int i = 0; i < dirList.length; i++) {
                File f = new File(zipDir, dirList[i]);
                if (f.isDirectory()) {
                    if (filter != null && filter.get(f.getAbsolutePath()) == null) zipDir(f, zos, path + f.getName() + "/", pm, filter);
                } else {
                    FileInputStream fis = new FileInputStream(f);
                    ZipEntry anEntry = new ZipEntry(path + f.getName());
                    zos.putNextEntry(anEntry);
                    while ((bytesIn = fis.read(readBuffer)) != -1) {
                        zos.write(readBuffer, 0, bytesIn);
                        if (pm != null) pm.addToProgress(bytesIn);
                    }
                    fis.close();
                }
            }
        } catch (Exception e) {
            Installer.getInstance().getLogger().log(StringUtils.getStackTrace(e));
        }
    }
