    private void copy(File sourceFile, File destinationFile) {
        try {
            FileChannel in = new FileInputStream(sourceFile).getChannel();
            FileChannel out = new FileOutputStream(destinationFile).getChannel();
            try {
                in.transferTo(0, in.size(), out);
                in.close();
                out.close();
            } catch (IOException e) {
                TestLogger.error(e);
            }
        } catch (FileNotFoundException e) {
            TestLogger.error(e);
        }
    }
