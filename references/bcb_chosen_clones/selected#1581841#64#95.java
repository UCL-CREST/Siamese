    @Override
    public byte[] readData(byte[] options, boolean transferMetaData) throws Throwable {
        byte[] data = null;
        long startTime = System.currentTimeMillis();
        long transferredBytesNum = 0;
        long elapsedTime = 0;
        Properties opts = PropertiesUtils.deserializeProperties(options);
        String filePath = opts.getProperty(TRANSFER_OPTION_FILEPATH);
        File file = new File(filePath);
        if (transferMetaData) {
            file = new File(file.getParentFile(), file.getName() + META_DATA_FILE_SUFIX);
        }
        if (file.exists()) {
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int br;
            while ((br = bis.read(buffer)) > 0) {
                baos.write(buffer, 0, br);
                if (!transferMetaData) {
                    transferredBytesNum += br;
                    elapsedTime = System.currentTimeMillis() - startTime;
                    fireOnProgressEvent(transferredBytesNum, elapsedTime);
                }
            }
            baos.close();
            bis.close();
            data = baos.toByteArray();
        }
        return data;
    }
