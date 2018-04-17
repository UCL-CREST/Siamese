    private static void addFileToZip(String path, String srcFile, ZipOutputStream zip, String prefix, String suffix) throws Exception {
        File folder = new File(srcFile);
        if (folder.isDirectory()) {
            addFolderToZip(path, srcFile, zip, prefix, suffix);
        } else {
            if (isFileNameMatch(folder.getName(), prefix, suffix)) {
                FileInputStream fis = new FileInputStream(srcFile);
                zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
                IOUtils.copy(fis, zip);
                fis.close();
            }
        }
    }
