    private static void recurseFilesInFiles(ZipOutputStream jos, File file, String pathName, List<File> isFileList) throws IOException, FileNotFoundException {
        boolean zipFlg = false;
        if (isFileList != null && isFileList.size() != 0) {
            for (int i = 0; i < isFileList.size(); i++) {
                if (isFileList.get(i).getPath().equals(file.getPath())) {
                    zipFlg = true;
                }
            }
        }
        if (!zipFlg) {
            return;
        }
        if (file.isDirectory()) {
            pathName = pathName + file.getName() + "/";
            jos.putNextEntry(new ZipEntry(pathName));
            String fileNames[] = file.list();
            if (fileNames != null) {
                for (int i = 0; i < fileNames.length; i++) recurseFilesInFiles(jos, new File(file, fileNames[i]), pathName, isFileList);
            }
        } else {
            ZipEntry jarEntry = new ZipEntry(pathName + file.getName());
            FileInputStream fin = new FileInputStream(file);
            BufferedInputStream in = new BufferedInputStream(fin);
            jos.putNextEntry(jarEntry);
            int len;
            while ((len = in.read(buf)) >= 0) jos.write(buf, 0, len);
            in.close();
            jos.closeEntry();
        }
    }
