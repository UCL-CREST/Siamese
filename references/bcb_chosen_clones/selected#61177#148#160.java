    private void copyFile(File file, String contextPath) throws IOException {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File f : files) {
                copyFile(f, contextPath == null ? "" : contextPath + file.getName() + File.separator);
            }
        } else if (file.isFile()) {
            InputStream is = new BufferedInputStream(new FileInputStream(file));
            File dest = new File(classesDirectory, contextPath + file.getName());
            OutputStream os = new BufferedOutputStream(new FileOutputStream(dest));
            copyStream(is, os);
        }
    }
