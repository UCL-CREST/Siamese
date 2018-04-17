    public static void createTar(File directoryToPack, File targetTarFile) throws IOException {
        if (directoryToPack == null) {
            throw new NullPointerException("The parameter 'directoryToPack' must not be null");
        }
        if (targetTarFile == null) {
            throw new NullPointerException("The parameter 'targetTarFile' must not be null");
        }
        if (!directoryToPack.exists() || !directoryToPack.isDirectory()) {
            throw new IllegalArgumentException("The target file '" + directoryToPack + "' does not exist or is not a directory.");
        }
        if (targetTarFile.exists()) {
            log.warn("The target file '" + targetTarFile + "' already exists. Will overwrite");
        }
        log.debug("Creating tar from all files in directory '" + directoryToPack + "'");
        byte buffer[] = new byte[BUFFER_SIZE];
        FileOutputStream targetOutput = new FileOutputStream(targetTarFile);
        TarOutputStream targetOutputTar = new TarOutputStream(targetOutput);
        try {
            List<File> fileList = collectFiles(directoryToPack);
            for (Iterator<File> iter = fileList.iterator(); iter.hasNext(); ) {
                File file = iter.next();
                if (file == null || !file.exists() || file.isDirectory()) {
                    log.info("The file '" + file + "' is ignored - is a directory or non-existent");
                    continue;
                }
                if (file.equals(targetTarFile)) {
                    log.debug("Skipping file: '" + file + "' - is the tar file itself");
                    continue;
                }
                log.debug("Adding to archive: file='" + file + "', archive='" + targetTarFile + "'");
                String filePathInTar = getFilePathInTar(file, directoryToPack);
                log.debug("File path in tar: '" + filePathInTar + "' (file=" + file + ")");
                TarEntry tarAdd = new TarEntry(file);
                tarAdd.setModTime(file.lastModified());
                tarAdd.setName(filePathInTar);
                targetOutputTar.putNextEntry(tarAdd);
                if (file.isFile()) {
                    FileInputStream in = new FileInputStream(file);
                    try {
                        while (true) {
                            int nRead = in.read(buffer, 0, buffer.length);
                            if (nRead <= 0) break;
                            targetOutputTar.write(buffer, 0, nRead);
                        }
                    } finally {
                        StreamUtil.tryCloseStream(in);
                    }
                }
                targetOutputTar.closeEntry();
            }
        } finally {
            StreamUtil.tryCloseStream(targetOutputTar);
            StreamUtil.tryCloseStream(targetOutput);
        }
        log.info("Tar Archive created successfully '" + targetTarFile + "'");
    }
