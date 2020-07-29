    public static boolean archiveFolder(String folderPath, ZipOutputStream zos, String ignoredPathPrefix) {
        try {
            File zipDir = new File(folderPath);
            String[] dirList = zipDir.list();
            byte[] readBuffer = new byte[2156];
            int bytesIn = 0;
            for (int i = 0; i < dirList.length; i++) {
                File f = new File(zipDir, dirList[i]);
                if (f.isDirectory()) {
                    String filePath = f.getPath();
                    archiveFolder(filePath, zos, ignoredPathPrefix);
                    continue;
                }
                FileInputStream fis = new FileInputStream(f);
                String entryPath = f.getPath();
                if (entryPath.startsWith(ignoredPathPrefix)) {
                    entryPath = entryPath.substring(ignoredPathPrefix.length() + 1);
                }
                ZipEntry anEntry = new ZipEntry(entryPath.replace('\\', '/'));
                zos.putNextEntry(anEntry);
                while ((bytesIn = fis.read(readBuffer)) != -1) {
                    zos.write(readBuffer, 0, bytesIn);
                }
                fis.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
