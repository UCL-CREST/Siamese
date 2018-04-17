    public static void zipDir(File zipDir, ZipOutputStream zos, File rawdatadir) throws IOException {
        if (zipDir.isFile()) {
            int bytesIn = 0;
            byte[] readBuffer = new byte[2156];
            if (!alreadyAttached.contains(zipDir.getPath())) {
                FileInputStream fis = new FileInputStream(zipDir);
                ZipEntry anEntry = new ZipEntry(zipDir.getPath());
                alreadyAttached.add(zipDir.getPath());
                zos.putNextEntry(anEntry);
                while ((bytesIn = fis.read(readBuffer)) != -1) {
                    zos.write(readBuffer, 0, bytesIn);
                }
                fis.close();
            }
        } else {
            String[] dirList = zipDir.list();
            byte[] readBuffer = new byte[2156];
            int bytesIn = 0;
            for (int i = 0; i < dirList.length; i++) {
                File f = new File(zipDir, dirList[i]);
                if (f.isDirectory()) {
                    String filePath = f.getPath();
                    zipDir(new File(filePath), zos, rawdatadir);
                    continue;
                }
                if (!alreadyAttached.contains(getRelativePath(rawdatadir, f))) {
                    FileInputStream fis = new FileInputStream(f);
                    ZipEntry anEntry = new ZipEntry(getRelativePath(rawdatadir, f));
                    alreadyAttached.add(getRelativePath(rawdatadir, f));
                    zos.putNextEntry(anEntry);
                    while ((bytesIn = fis.read(readBuffer)) != -1) {
                        zos.write(readBuffer, 0, bytesIn);
                    }
                    fis.close();
                }
            }
        }
    }
