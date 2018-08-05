    public static void copyFile(File source, File destination) throws IOException {
        if (source == null) {
            String message = Logging.getMessage("nullValue.SourceIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        if (destination == null) {
            String message = Logging.getMessage("nullValue.DestinationIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel fic, foc;
        try {
            fis = new FileInputStream(source);
            fic = fis.getChannel();
            fos = new FileOutputStream(destination);
            foc = fos.getChannel();
            foc.transferFrom(fic, 0, fic.size());
            fos.flush();
            fis.close();
            fos.close();
        } finally {
            WWIO.closeStream(fis, source.getPath());
            WWIO.closeStream(fos, destination.getPath());
        }
    }
