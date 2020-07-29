    public static boolean copyFile(File sourceFile, File destinationFile) {
        boolean copySuccessfull = false;
        FileChannel source = null;
        FileChannel destination = null;
        try {
            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destinationFile).getChannel();
            long transferedBytes = destination.transferFrom(source, 0, source.size());
            copySuccessfull = transferedBytes == source.size() ? true : false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (source != null) {
                try {
                    source.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (destination != null) {
                try {
                    destination.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return copySuccessfull;
    }
