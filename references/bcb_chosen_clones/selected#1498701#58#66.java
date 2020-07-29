    private void handleUpload(CommonsMultipartFile file, String newFileName, String uploadDir) throws IOException, FileNotFoundException {
        File dirPath = new File(uploadDir);
        if (!dirPath.exists()) {
            dirPath.mkdirs();
        }
        InputStream stream = file.getInputStream();
        OutputStream bos = new FileOutputStream(uploadDir + newFileName);
        IOUtils.copy(stream, bos);
    }
