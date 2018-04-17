    public static void uploadImage(Properties ctx, int productId, File file, String imageName, String trxName) throws OperationException {
        if (file == null) throw new OperationException("Image file cannot be null");
        if (!file.exists()) throw new OperationException("Image file does not exist");
        if (file.isDirectory()) throw new OperationException("File cannot be a directory");
        try {
            FileInputStream fileInStream = new FileInputStream(file);
            ByteArrayOutputStream byteArrStream = new ByteArrayOutputStream();
            BufferedInputStream bufferedInStream = new BufferedInputStream(fileInStream);
            byte data[] = new byte[1024];
            int read = 0;
            while ((read = bufferedInStream.read(data)) != -1) {
                byteArrStream.write(data, 0, read);
            }
            byteArrStream.flush();
            byte fileData[] = byteArrStream.toByteArray();
            bufferedInStream.close();
            byteArrStream.close();
            fileInStream.close();
            uploadImage(ctx, productId, fileData, imageName, trxName);
        } catch (IOException ex) {
            throw new OperationException("Could not read file: " + file.getAbsolutePath(), ex);
        }
    }
