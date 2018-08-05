    private int startCompressOutputToZip(GameDB currentGame, String romName) {
        if (log.isDebugEnabled()) {
            log.debug("startCompressOutputToZip(GameDB currentGame, String romName)");
            log.debug("CurrentLocation: " + currentGame.getCurrentlocation());
            log.debug("SourceFile: " + currentGame.getSourceFile());
        }
        int returnCode = -1;
        String inputFile = currentGame.getCurrentlocation();
        String outputFile = this.settings.getOutputFolder() + File.separatorChar + romName.substring(0, romName.length() - 4) + ".zip";
        if (log.isDebugEnabled()) {
            log.debug("InputFile: " + inputFile);
            log.debug("OutputFile: " + outputFile);
        }
        message("rename.zip.start", romName, outputFile);
        ZipOutputStream zous = null;
        try {
            ZipEntry romEntry = new ZipEntry(romName);
            zous = new ZipOutputStream(new FileOutputStream(outputFile, false));
            zous.putNextEntry(romEntry);
            RandomAccessFile raf = new RandomAccessFile(inputFile, "r");
            long fileSize = raf.length();
            long currentPosition = 0;
            int bufferLength = 2048;
            int len;
            byte[] buffer = new byte[bufferLength];
            while ((len = raf.read(buffer)) != -1) {
                setProgress(currentPosition += len, 0, fileSize);
                zous.write(buffer, 0, len);
            }
            raf.close();
            returnCode = 0;
        } catch (IOException ex) {
            log.error(null, ex);
            returnCode = -1;
        } finally {
            try {
                zous.close();
            } catch (Exception ex) {
            }
        }
        return returnCode;
    }
