    public static URL addToArchive(Pod pod, ZipOutputStream podArchiveOutputStream, String filename, InputStream source) throws IOException {
        ZipEntry entry = new ZipEntry(filename);
        podArchiveOutputStream.putNextEntry(entry);
        IOUtils.copy(source, podArchiveOutputStream);
        podArchiveOutputStream.closeEntry();
        return PodArchiveResolver.withinPodArchive(pod, filename);
    }
