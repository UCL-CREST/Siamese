    public static String uncompress(String readPath, boolean mkdir) throws Exception {
        ZipArchiveInputStream arcInputStream = new ZipArchiveInputStream(new FileInputStream(readPath));
        BufferedInputStream bis = new BufferedInputStream(arcInputStream);
        File baseDir = new File(readPath).getParentFile();
        String basePath = baseDir.getPath() + "/";
        if (mkdir) {
            String[] schema = readPath.split("/");
            String baseName = schema[schema.length - 1].replaceAll(".zip", "");
            FileUtils.forceMkdir(new File(basePath + baseName));
            basePath = basePath + baseName + "/";
        }
        ArchiveEntry entry;
        while ((entry = arcInputStream.getNextEntry()) != null) {
            if (entry.isDirectory()) {
                FileUtils.forceMkdir(new File(basePath + entry.getName()));
            } else {
                String writePath = basePath + entry.getName();
                String dirName = FilenameUtils.getPath(writePath);
                FileUtils.forceMkdir(new File(dirName));
                BufferedOutputStream bos = new BufferedOutputStream(FileUtils.openOutputStream(new File(writePath)));
                int i = 0;
                while ((i = bis.read()) != -1) {
                    bos.write(i);
                }
                IOUtils.closeQuietly(bos);
            }
        }
        IOUtils.closeQuietly(bis);
        return basePath;
    }
