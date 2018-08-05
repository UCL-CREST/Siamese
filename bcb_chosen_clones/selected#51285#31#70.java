    public int process(ProcessorContext context) throws InterruptedException, ProcessorException {
        logger.info("JAISaveTask:process");
        final RenderedOp im = (RenderedOp) context.get("RenderedOp");
        final String path = "s3://s3.amazonaws.com/rssfetch/" + (new Guid());
        final PNGEncodeParam.RGB encPar = new PNGEncodeParam.RGB();
        encPar.setTransparentRGB(new int[] { 0, 0, 0 });
        File tmpFile = null;
        try {
            tmpFile = File.createTempFile("thmb", ".png");
            OutputStream out = new FileOutputStream(tmpFile);
            final ParameterBlock pb = (new ParameterBlock()).addSource(im).add(out).add("png").add(encPar);
            JAI.create("encode", pb, null);
            out.flush();
            out.close();
            FileInputStream in = new FileInputStream(tmpFile);
            final XFile xfile = new XFile(path);
            final XFileOutputStream xout = new XFileOutputStream(xfile);
            final com.luzan.common.nfs.s3.XFileExtensionAccessor xfa = ((com.luzan.common.nfs.s3.XFileExtensionAccessor) xfile.getExtensionAccessor());
            if (xfa != null) {
                xfa.setMimeType("image/png");
                xfa.setContentLength(tmpFile.length());
            }
            IOUtils.copy(in, xout);
            xout.flush();
            xout.close();
            in.close();
            context.put("outputPath", path);
        } catch (IOException e) {
            logger.error(e);
            throw new ProcessorException(e);
        } catch (Throwable e) {
            logger.error(e);
            throw new ProcessorException(e);
        } finally {
            if (tmpFile != null && tmpFile.exists()) {
                tmpFile.delete();
            }
        }
        return TaskState.STATE_MO_START + TaskState.STATE_ENCODE;
    }
