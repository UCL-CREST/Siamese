    private void makeZip(ArrayList<String> filePathList, ArrayList<String> fileNameList, String outputZipFileName) throws IOException {
        byte[] buf = new byte[1024];
        File outputZipFile = new File(outputZipFileName);
        File outputZipParentFile = outputZipFile.getParentFile();
        if (!outputZipParentFile.exists()) {
            outputZipParentFile.mkdirs();
        }
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outputZipFile));
        int len = -1;
        for (int i = 0; i < filePathList.size(); i++) {
            if (filePathList.get(i) != null) {
                FileInputStream in = new FileInputStream(filePathList.get(i));
                out.putNextEntry(new org.apache.tools.zip.ZipEntry(fileNameList.get(i)));
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.closeEntry();
                in.close();
            }
        }
        out.close();
    }
