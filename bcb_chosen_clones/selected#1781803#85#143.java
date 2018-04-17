    private void process(String zipFileName, String directory, String db, boolean quiet) throws SQLException {
        ArrayList<String> list = FileLister.getDatabaseFiles(directory, db, true);
        if (list.size() == 0) {
            if (!quiet) {
                printNoDatabaseFilesFound(directory, db);
            }
            return;
        }
        zipFileName = FileUtils.normalize(zipFileName);
        if (FileUtils.exists(zipFileName)) {
            FileUtils.delete(zipFileName);
        }
        OutputStream fileOut = null;
        try {
            fileOut = FileUtils.openFileOutputStream(zipFileName, false);
            ZipOutputStream zipOut = new ZipOutputStream(fileOut);
            String base = "";
            for (String fileName : list) {
                if (fileName.endsWith(Constants.SUFFIX_PAGE_FILE)) {
                    base = FileUtils.getParent(fileName);
                    break;
                } else if (fileName.endsWith(Constants.SUFFIX_DATA_FILE)) {
                    base = FileUtils.getParent(fileName);
                    break;
                }
            }
            for (String fileName : list) {
                String f = FileUtils.getAbsolutePath(fileName);
                if (!f.startsWith(base)) {
                    Message.throwInternalError(f + " does not start with " + base);
                }
                if (FileUtils.isDirectory(fileName)) {
                    continue;
                }
                f = f.substring(base.length());
                f = BackupCommand.correctFileName(f);
                ZipEntry entry = new ZipEntry(f);
                zipOut.putNextEntry(entry);
                InputStream in = null;
                try {
                    in = FileUtils.openFileInputStream(fileName);
                    IOUtils.copyAndCloseInput(in, zipOut);
                } catch (FileNotFoundException e) {
                } finally {
                    IOUtils.closeSilently(in);
                }
                zipOut.closeEntry();
                if (!quiet) {
                    out.println("Processed: " + fileName);
                }
            }
            zipOut.closeEntry();
            zipOut.close();
        } catch (IOException e) {
            throw Message.convertIOException(e, zipFileName);
        } finally {
            IOUtils.closeSilently(fileOut);
        }
    }
