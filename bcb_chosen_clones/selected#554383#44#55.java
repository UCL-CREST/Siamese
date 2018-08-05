    public void addFolder(final ZipOutStreamTaskAdapter out, final FolderEntity folder, final String zipPath) throws IOException, TaskTimeoutException {
        if (zipPath.length() != 0) {
            out.putNextEntry(new ZipEntry(zipPath));
            out.closeEntry();
        }
        if (!out.isSkip(zipPath + "_folder.xml")) {
            out.putNextEntry(new ZipEntry(zipPath + "_folder.xml"));
            out.write(getFolderSystemFile(folder).getBytes("UTF-8"));
            out.closeEntry();
            out.nextFile();
        }
    }
