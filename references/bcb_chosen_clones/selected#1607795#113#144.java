    public void extractImage(String input, String output, DjatokaDecodeParam params, IWriter w) throws DjatokaException {
        File in = null;
        String dest = output;
        if (input.equals(STDIN)) {
            try {
                in = File.createTempFile("tmp", ".jp2");
                input = in.getAbsolutePath();
                in.deleteOnExit();
                IOUtils.copyFile(new File(STDIN), in);
            } catch (IOException e) {
                logger.error("Unable to process image from " + STDIN + ": " + e.getMessage());
                throw new DjatokaException(e);
            }
        }
        BufferedImage bi = extractImpl.process(input, params);
        if (bi != null) {
            if (params.getScalingFactor() != 1.0 || params.getScalingDimensions() != null) bi = applyScaling(bi, params);
            if (params.getTransform() != null) bi = params.getTransform().run(bi);
            try {
                BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(new File(dest)));
                w.write(bi, os);
                os.close();
            } catch (FileNotFoundException e) {
                logger.error("Requested file was not found: " + dest);
                throw new DjatokaException(e);
            } catch (IOException e) {
                logger.error("Error attempting to close: " + dest);
                throw new DjatokaException(e);
            }
        }
        if (in != null) in.delete();
    }
