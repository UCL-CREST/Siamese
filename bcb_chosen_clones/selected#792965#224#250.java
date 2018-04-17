    public static File copyFile(File fileToCopy, File copiedFile) {
        BufferedInputStream in = null;
        BufferedOutputStream outWriter = null;
        if (!copiedFile.exists()) {
            try {
                copiedFile.createNewFile();
            } catch (IOException e1) {
                ExceptionHandlingService.INSTANCE.handleException(e1);
                return null;
            }
        }
        try {
            in = new BufferedInputStream(new FileInputStream(fileToCopy), 4096);
            outWriter = new BufferedOutputStream(new FileOutputStream(copiedFile), 4096);
            int c;
            while ((c = in.read()) != -1) outWriter.write(c);
            in.close();
            outWriter.close();
        } catch (FileNotFoundException e) {
            ExceptionHandlingService.INSTANCE.handleException(e);
            return null;
        } catch (IOException e) {
            ExceptionHandlingService.INSTANCE.handleException(e);
            return null;
        }
        return copiedFile;
    }
