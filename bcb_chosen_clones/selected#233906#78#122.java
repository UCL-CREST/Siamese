    private static void _zipFolder(File baseFolder, String path, ZipOutputStream zos) throws Exception {
        File currentFolder;
        File[] contents;
        int n, nn;
        File content;
        ZipEntry ze;
        BufferedInputStream bis;
        byte buffer[] = new byte[1000];
        int bufferSize = buffer.length;
        int bytesRead;
        try {
            currentFolder = new File(baseFolder, path);
            if (!currentFolder.exists()) throw new Exception("Folder '" + baseFolder + "' doesn't exist.");
            if (!currentFolder.isDirectory()) throw new Exception("'" + currentFolder + "' is not a folder.");
            if (path == null) path = "";
            if (path.length() > 0) path += "/";
            contents = currentFolder.listFiles();
            nn = contents.length;
            for (n = 0; n < nn; n++) {
                content = contents[n];
                if (content.isDirectory()) {
                    _zipFolder(baseFolder, path + content.getName(), zos);
                } else {
                    ze = new ZipEntry(path + content.getName());
                    try {
                        zos.putNextEntry(ze);
                        bis = new BufferedInputStream(new FileInputStream(content));
                        for (; ; ) {
                            bytesRead = bis.read(buffer, 0, bufferSize);
                            if (bytesRead <= 0) break;
                            zos.write(buffer, 0, bytesRead);
                        }
                        bis.close();
                        zos.closeEntry();
                    } catch (ZipException e) {
                        if (!e.getMessage().startsWith("duplicate entry")) throw new Exception(ParseError.parseError("Error zipping '" + content.getAbsolutePath() + "': ", e));
                    }
                }
            }
        } catch (Exception e) {
            String s1;
            if (baseFolder == null) s1 = "null"; else s1 = baseFolder.getAbsolutePath();
            throw new Exception(ParseError.parseError("ApplicationHelper._zipFolder('" + s1 + "','" + path + "',ZipOutputStream)", e));
        }
    }
