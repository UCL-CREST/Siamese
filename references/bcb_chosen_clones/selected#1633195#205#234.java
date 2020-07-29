    private static void zip(ZipOutputStream aOutputStream, final File[] aFiles, final String sArchive, final URI aRootURI, final String sFilter) throws FileError {
        boolean closeStream = false;
        if (aOutputStream == null) try {
            aOutputStream = new ZipOutputStream(new FileOutputStream(sArchive));
            closeStream = true;
        } catch (final FileNotFoundException e) {
            throw new FileError("Can't create ODF file!", e);
        }
        try {
            try {
                for (final File curFile : aFiles) {
                    aOutputStream.putNextEntry(new ZipEntry(URLDecoder.decode(aRootURI.relativize(curFile.toURI()).toASCIIString(), "UTF-8")));
                    if (curFile.isDirectory()) {
                        aOutputStream.closeEntry();
                        FileUtils.zip(aOutputStream, FileUtils.getFiles(curFile, sFilter), sArchive, aRootURI, sFilter);
                        continue;
                    }
                    final FileInputStream inputStream = new FileInputStream(curFile);
                    for (int i; (i = inputStream.read(FileUtils.BUFFER)) != -1; ) aOutputStream.write(FileUtils.BUFFER, 0, i);
                    inputStream.close();
                    aOutputStream.closeEntry();
                }
            } finally {
                if (closeStream && aOutputStream != null) aOutputStream.close();
            }
        } catch (final IOException e) {
            throw new FileError("Can't zip file to archive!", e);
        }
        if (closeStream) DocumentController.getStaticLogger().fine(aFiles.length + " files and folders zipped as " + sArchive);
    }
