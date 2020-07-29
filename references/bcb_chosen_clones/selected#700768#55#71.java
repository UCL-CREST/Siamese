    public static void moveOutputAsmFile(File inputLocation, File outputLocation) throws Exception {
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(inputLocation);
            outputStream = new FileOutputStream(outputLocation);
            byte buffer[] = new byte[1024];
            while (inputStream.available() > 0) {
                int read = inputStream.read(buffer);
                outputStream.write(buffer, 0, read);
            }
            inputLocation.delete();
        } finally {
            IOUtil.closeAndIgnoreErrors(inputStream);
            IOUtil.closeAndIgnoreErrors(outputStream);
        }
    }
