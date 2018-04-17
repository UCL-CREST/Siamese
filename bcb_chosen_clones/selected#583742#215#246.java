    private static void zipFile(String filePath, ZipOutputStream zos, int storePolicy, String rootDir) throws IOException {
        File ffilePath = new File(filePath);
        String path = "";
        switch(storePolicy) {
            case STORE_FULL_PATH_IN_ZIP:
                path = ffilePath.getAbsolutePath();
                break;
            case STORE_NAME_ONLY_IN_ZIP:
                ffilePath.getName();
                break;
            case STORE_RELATIVE_PATH_IN_ZIP:
                File f = new File("");
                String pathToHere = f.getAbsolutePath();
                path = ffilePath.getAbsolutePath();
                path = path.substring(path.indexOf(pathToHere + File.separator) + pathToHere.length());
                break;
            case STORE_PATH_FROM_ZIP_ROOT:
                path = ffilePath.getAbsolutePath();
                String tmpDir = rootDir + File.separator;
                path = path.substring(path.indexOf(tmpDir) + tmpDir.length());
                break;
            default:
                break;
        }
        FileInputStream fileStream = new FileInputStream(filePath);
        BufferedInputStream bis = new BufferedInputStream(fileStream);
        ZipEntry fileEntry = new ZipEntry(path);
        zos.putNextEntry(fileEntry);
        byte[] data = new byte[BUFFER_SIZE];
        int byteCount;
        while ((byteCount = bis.read(data, 0, BUFFER_SIZE)) > -1) zos.write(data, 0, byteCount);
    }
