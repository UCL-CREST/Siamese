    private void zipFiles() throws ZipException, IOException {
        ZipOutputStream zipOuputStream = new ZipOutputStream(new FileOutputStream(zipFile));
        for (File sourceDir : sourceDirs) {
            File[] sourceFiles = sourceDir.listFiles(new SourceFileFilter());
            if (sourceFiles != null) {
                for (File sourceFile : sourceFiles) {
                    ZipEntry zipEntry = new ZipEntry(sourceFile.getName());
                    zipOuputStream.putNextEntry(zipEntry);
                    if (logger.isInfoEnabled()) {
                        logger.info("Adding zip entry " + sourceFile.getAbsolutePath() + " to " + zipFile.getAbsolutePath());
                    }
                    if (sourceFile.length() > BYTE_ARRAY_BUFF_SIZE) {
                        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(sourceFile), BYTE_ARRAY_BUFF_SIZE);
                        byte[] buf = new byte[BYTE_ARRAY_BUFF_SIZE];
                        int b;
                        while ((b = bis.read(buf, 0, BYTE_ARRAY_BUFF_SIZE)) != -1) {
                            zipOuputStream.write(buf, 0, b);
                        }
                        bis.close();
                    } else {
                        byte[] fileContents = FileUtil.getFileContentsAsByteArray(sourceFile);
                        zipOuputStream.write(fileContents);
                    }
                    zipOuputStream.closeEntry();
                }
            }
        }
        zipOuputStream.close();
    }
