    public void copy(File source, File destination) {
        try {
            FileInputStream fileInputStream = new FileInputStream(source);
            FileOutputStream fileOutputStream = new FileOutputStream(destination);
            FileChannel inputChannel = fileInputStream.getChannel();
            FileChannel outputChannel = fileOutputStream.getChannel();
            transfer(inputChannel, outputChannel, source.length(), false);
            fileInputStream.close();
            fileOutputStream.close();
            destination.setLastModified(source.lastModified());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
