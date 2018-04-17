    public static boolean createZipFile(String targetArchivePath, File baseDir, List<File> files) throws ObclipseException {
        boolean success = false;
        byte[] buf = new byte[2048];
        File archive = new File(targetArchivePath);
        if (archive.getParentFile() != null && !archive.getParentFile().exists()) {
            archive.getParentFile().mkdirs();
        }
        Msg.verbose("Creating zip file ''{0}''...", archive);
        if (!baseDir.exists()) {
            Msg.error("The given base directory ''{0}'' does not exist!", baseDir.getAbsolutePath());
            return false;
        }
        try {
            ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(targetArchivePath));
            for (File file : files) {
                if (!file.exists()) {
                    Msg.error("The given file ''{0}'' does not exist! File skipped!", file.getAbsolutePath());
                    continue;
                }
                FileInputStream in = new FileInputStream(file);
                String filePath = file.getAbsolutePath();
                filePath = filePath.replaceAll("\\\\", "/");
                String baseDirPath = baseDir.getAbsolutePath();
                baseDirPath = baseDirPath.replaceAll("\\\\", "/") + "/";
                String archiveFilePath = filePath.replaceAll(baseDirPath, "");
                outZip.putNextEntry(new ZipEntry(archiveFilePath));
                int len;
                while ((len = in.read(buf)) > 0) {
                    outZip.write(buf, 0, len);
                }
                outZip.closeEntry();
                in.close();
            }
            outZip.close();
            success = true;
            Msg.verbose("Zip file SUCCESSFULLY created.");
        } catch (IOException e) {
            Msg.ioException(archive, e);
        }
        return success;
    }
