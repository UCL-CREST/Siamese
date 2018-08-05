    private static void zipFiles(Stack<File> parentDirs, File[] files, ZipOutputStream out) throws IOException {
        byte[] buf = new byte[1024];
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                parentDirs.push(files[i]);
                zipFiles(parentDirs, files[i].listFiles(), out);
                parentDirs.pop();
            } else {
                FileInputStream in = new FileInputStream(files[i]);
                String path = "";
                for (File parentDir : parentDirs) {
                    path += parentDir.getName() + File.separator;
                }
                out.putNextEntry(new ZipEntry(path + files[i].getName()));
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
        }
    }
