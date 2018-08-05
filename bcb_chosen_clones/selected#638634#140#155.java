    private boolean copy(File in, File out) {
        try {
            FileInputStream fis = new FileInputStream(in);
            FileOutputStream fos = new FileOutputStream(out);
            FileChannel readableChannel = fis.getChannel();
            FileChannel writableChannel = fos.getChannel();
            writableChannel.truncate(0);
            writableChannel.transferFrom(readableChannel, 0, readableChannel.size());
            fis.close();
            fos.close();
            return true;
        } catch (IOException ioe) {
            guiBuilder.showError("Copy Error", "IOException during copy", ioe.getMessage());
            return false;
        }
    }
