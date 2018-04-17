    public void closeContents() throws IOException {
        ZipEntry entry = new ZipEntry(mkRelative(currentPath) + "/__contents__.xml");
        out.putNextEntry(entry);
        out.write(contents.toString().getBytes("UTF-8"));
        out.closeEntry();
        dataWritten = true;
    }
