    private static synchronized void zipDir(File zipDir, ZipOutputStream zos, String absolutePathToThemeDir, String rootDirName) throws IOException {
        String[] dirList = zipDir.list();
        byte[] readBuffer = new byte[2156];
        int bytesIn = 0;
        for (int i = 0; i < dirList.length; i++) {
            File f = new File(zipDir, dirList[i]);
            if (f.isDirectory()) {
                zipDir(f, zos, absolutePathToThemeDir, rootDirName);
                continue;
            }
            FileInputStream fis = new FileInputStream(f);
            String path = (StringUtils.isNotEmpty(rootDirName) ? rootDirName : "") + "/" + f.getAbsolutePath().substring(absolutePathToThemeDir.length() + 1, f.getAbsolutePath().length());
            ZipEntry anEntry = new ZipEntry(path);
            zos.putNextEntry(anEntry);
            while ((bytesIn = fis.read(readBuffer)) != -1) {
                zos.write(readBuffer, 0, bytesIn);
            }
            fis.close();
        }
    }
