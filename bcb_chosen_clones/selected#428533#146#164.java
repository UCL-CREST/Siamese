    static void addDirectory(JarOutputStream out, File dictDir, String subDir) throws IOException {
        byte[] b = new byte[3000];
        int readBytes;
        File[] dictFiles = dictDir.listFiles();
        for (int i = 0; i < dictFiles.length; i++) {
            if (!dictFiles[i].isDirectory()) {
                FileInputStream fis = new FileInputStream(dictFiles[i]);
                out.putNextEntry(new ZipEntry(DictionaryDataFile.pathNameDataFiles + "/" + subDir + dictFiles[i].getName()));
                while ((readBytes = fis.read(b)) != -1) {
                    out.write(b, 0, readBytes);
                }
                fis.close();
            } else {
                File subDirFile = dictFiles[i];
                String newSubDir = subDir + subDirFile.getName() + '/';
                addDirectory(out, subDirFile, newSubDir);
            }
        }
    }
