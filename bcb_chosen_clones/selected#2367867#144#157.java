    private void copia(FileInputStream input, FileOutputStream output) throws ErrorException {
        if (input == null || output == null) {
            throw new ErrorException("Param null");
        }
        FileChannel inChannel = input.getChannel();
        FileChannel outChannel = output.getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
            inChannel.close();
            outChannel.close();
        } catch (IOException e) {
            throw new ErrorException("Casino nella copia del file");
        }
    }
