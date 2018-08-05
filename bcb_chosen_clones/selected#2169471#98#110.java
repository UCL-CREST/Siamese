    public static long toFile(final DigitalObject object, final File file) {
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            long bytesCopied = IOUtils.copyLarge(object.getContent().getInputStream(), fOut);
            fOut.close();
            return bytesCopied;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
