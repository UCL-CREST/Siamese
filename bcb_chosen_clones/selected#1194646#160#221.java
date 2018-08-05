    public void compressImage(InputStream input, OutputStream output, DjatokaEncodeParam params) throws DjatokaException {
        if (params == null) params = new DjatokaEncodeParam();
        File inputFile = null;
        try {
            inputFile = File.createTempFile("tmp", ".tif");
            IOUtils.copyStream(input, new FileOutputStream(inputFile));
            if (params.getLevels() == 0) {
                ImageRecord dim = ImageRecordUtils.getImageDimensions(inputFile.getAbsolutePath());
                params.setLevels(ImageProcessingUtils.getLevelCount(dim.getWidth(), dim.getHeight()));
                dim = null;
            }
        } catch (IOException e1) {
            logger.error("Unexpected file format; expecting uncompressed TIFF", e1);
            throw new DjatokaException("Unexpected file format; expecting uncompressed TIFF");
        }
        String out = STDOUT;
        File winOut = null;
        if (isWindows) {
            try {
                winOut = File.createTempFile("pipe_", ".jp2");
            } catch (IOException e) {
                logger.error(e, e);
                throw new DjatokaException(e);
            }
            out = winOut.getAbsolutePath();
        }
        String command = getKduCompressCommand(inputFile.getAbsolutePath(), out, params);
        logger.debug("compressCommand: " + command);
        Runtime rt = Runtime.getRuntime();
        try {
            final Process process = rt.exec(command, envParams, new File(env));
            if (out.equals(STDOUT)) {
                IOUtils.copyStream(process.getInputStream(), output);
            } else if (isWindows) {
                FileInputStream fis = new FileInputStream(out);
                IOUtils.copyStream(fis, output);
                fis.close();
            }
            process.waitFor();
            if (process != null) {
                String errorCheck = null;
                try {
                    errorCheck = new String(IOUtils.getByteArray(process.getErrorStream()));
                } catch (Exception e1) {
                    logger.error(e1, e1);
                }
                process.getInputStream().close();
                process.getOutputStream().close();
                process.getErrorStream().close();
                process.destroy();
                if (errorCheck != null) throw new DjatokaException(errorCheck);
            }
        } catch (IOException e) {
            logger.error(e, e);
            throw new DjatokaException(e);
        } catch (InterruptedException e) {
            logger.error(e, e);
            throw new DjatokaException(e);
        }
        if (inputFile != null) inputFile.delete();
        if (winOut != null) winOut.delete();
    }
