    public static void copyFile(String source, String destination, boolean overwrite) {
        File sourceFile = new File(source);
        try {
            File destinationFile = new File(destination);
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile));
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(destinationFile));
            int temp = 0;
            while ((temp = bis.read()) != -1) bos.write(temp);
            bis.close();
            bos.close();
        } catch (Exception e) {
        }
        return;
    }
