    private String jarDiagnostics(Throwable throwable, File[] files) throws IOException {
        File home = new File(System.getProperty("user.home"));
        File jar_file = File.createTempFile("nuages-diagnostic.", ".jar", home);
        FileOutputStream fos = new FileOutputStream(jar_file);
        JarOutputStream jos = new JarOutputStream(fos);
        ZipEntry ze = new ZipEntry("Exception");
        jos.putNextEntry(ze);
        PrintStream ps = new PrintStream(jos);
        throwable.printStackTrace(ps);
        for (File file : files) if ((file != null) && file.exists()) {
            ze = new ZipEntry(file.getName());
            jos.putNextEntry(ze);
            byte[] buffer = new byte[65536];
            FileInputStream fis = new FileInputStream(file);
            int len;
            while ((len = fis.read(buffer)) > 0) jos.write(buffer, 0, len);
            fis.close();
        }
        jos.close();
        return jar_file.getPath();
    }
