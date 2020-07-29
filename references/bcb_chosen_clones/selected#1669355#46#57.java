    private static void processFile(String file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        int read = 0;
        byte[] buf = new byte[2048];
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        while ((read = in.read(buf)) > 0) bout.write(buf, 0, read);
        in.close();
        String converted = bout.toString().replaceAll("@project.name@", projectNameS).replaceAll("@base.package@", basePackageS).replaceAll("@base.dir@", baseDir).replaceAll("@webapp.dir@", webAppDir).replaceAll("path=\"target/classes\"", "path=\"src/main/webapp/WEB-INF/classes\"");
        FileOutputStream out = new FileOutputStream(file);
        out.write(converted.getBytes());
        out.close();
    }
