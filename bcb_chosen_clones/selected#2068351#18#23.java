    public static void saveFile(final ZipOutStreamTaskAdapter out, String name, String content) throws IOException, TaskTimeoutException {
        out.putNextEntry(new ZipEntry(name));
        out.write(content.getBytes("UTF-8"));
        out.closeEntry();
        out.nextFile();
    }
