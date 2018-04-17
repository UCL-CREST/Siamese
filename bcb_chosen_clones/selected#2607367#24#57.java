    public void doCompress(File[] files, File out, List<String> excludedKeys) {
        Map<String, File> map = new HashMap<String, File>();
        String parent = FilenameUtils.getBaseName(out.getName());
        for (File f : files) {
            CompressionUtil.list(f, parent, map, excludedKeys);
        }
        if (!map.isEmpty()) {
            FileOutputStream fos = null;
            ArchiveOutputStream aos = null;
            InputStream is = null;
            try {
                fos = new FileOutputStream(out);
                aos = getArchiveOutputStream(fos);
                for (Map.Entry<String, File> entry : map.entrySet()) {
                    File file = entry.getValue();
                    ArchiveEntry ae = getArchiveEntry(file, entry.getKey());
                    aos.putArchiveEntry(ae);
                    if (file.isFile()) {
                        IOUtils.copy(is = new FileInputStream(file), aos);
                        IOUtils.closeQuietly(is);
                        is = null;
                    }
                    aos.closeArchiveEntry();
                }
                aos.finish();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                IOUtils.closeQuietly(is);
                IOUtils.closeQuietly(aos);
                IOUtils.closeQuietly(fos);
            }
        }
    }
