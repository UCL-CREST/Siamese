    public static void archieveFiles(List<FileSystemNode> files, ZipOutputStream out, String pathFromRoot) throws Exception {
        byte[] buffer = new byte[4096];
        for (FileSystemNode file : files) {
            if (file.isFolder()) {
                archieveFiles(file.getFileNodes(), out, pathFromRoot + file.getName() + "/");
                continue;
            }
            out.putNextEntry(new ZipEntry(pathFromRoot + file.getName()));
            InputStream in = new BufferedInputStream(file.getContent());
            for (int length = 0; (length = in.read(buffer)) > 0; ) {
                out.write(buffer, 0, length);
            }
            out.closeEntry();
            in.close();
        }
    }
