    private static void recurseFiles(ZipOutputStream jos, File file, String pathName, List<File> exceptFileList) throws IOException, FileNotFoundException {
        if (exceptFileList != null && exceptFileList.size() != 0) {
            for (int i = 0; i < exceptFileList.size(); i++) {
                if (exceptFileList.get(i).getPath().equals(file.getPath())) {
                    return;
                }
            }
        }
        if (file.isDirectory()) {
            pathName = pathName + file.getName() + "/";
            jos.putNextEntry(new ZipEntry(pathName));
            String fileNames[] = file.list();
            if (fileNames != null) {
                for (int i = 0; i < fileNames.length; i++) recurseFiles(jos, new File(file, fileNames[i]), pathName, exceptFileList);
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
