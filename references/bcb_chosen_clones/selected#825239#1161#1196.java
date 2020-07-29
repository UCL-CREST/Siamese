    public static void copyDirectory(File sourceDirectory, File targetDirectory) throws IOException {
        File[] sourceFiles = sourceDirectory.listFiles(FILE_FILTER);
        File[] sourceDirectories = sourceDirectory.listFiles(DIRECTORY_FILTER);
        targetDirectory.mkdirs();
        if (sourceFiles != null && sourceFiles.length > 0) {
            for (int i = 0; i < sourceFiles.length; i++) {
                File sourceFile = sourceFiles[i];
                FileInputStream fis = new FileInputStream(sourceFile);
                FileOutputStream fos = new FileOutputStream(targetDirectory + File.separator + sourceFile.getName());
                FileChannel fcin = fis.getChannel();
                FileChannel fcout = fos.getChannel();
                ByteBuffer buf = ByteBuffer.allocateDirect(8192);
                long size = fcin.size();
                long n = 0;
                while (n < size) {
                    buf.clear();
                    if (fcin.read(buf) < 0) {
                        break;
                    }
                    buf.flip();
                    n += fcout.write(buf);
                }
                fcin.close();
                fcout.close();
                fis.close();
                fos.close();
            }
        }
        if (sourceDirectories != null && sourceDirectories.length > 0) {
            for (int i = 0; i < sourceDirectories.length; i++) {
                File directory = sourceDirectories[i];
                File newTargetDirectory = new File(targetDirectory, directory.getName());
                copyDirectory(directory, newTargetDirectory);
            }
        }
    }
