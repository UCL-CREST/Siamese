    private static void copyFile(File f) {
        try {
            String baseName = baseDir.getCanonicalPath();
            String fullPath = f.getCanonicalPath();
            String nameSufix = fullPath.substring(baseName.length() + 1);
            File destFile = new File(FileDestDir, nameSufix);
            destFile.getParentFile().mkdirs();
            destFile.createNewFile();
            FileChannel fromChannel = new FileInputStream(f).getChannel();
            FileChannel toChannel = new FileOutputStream(destFile).getChannel();
            fromChannel.transferTo(0, fromChannel.size(), toChannel);
            fromChannel.close();
            toChannel.close();
            destFile.setLastModified(f.lastModified());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
