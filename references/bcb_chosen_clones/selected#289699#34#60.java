    public static void generate(final InputStream input, String format, Point dimension, IPath outputLocation) throws CoreException {
        MultiStatus status = new MultiStatus(GraphVizActivator.ID, 0, "Errors occurred while running Graphviz", null);
        File dotInput = null, dotOutput = outputLocation.toFile();
        ByteArrayOutputStream dotContents = new ByteArrayOutputStream();
        try {
            dotInput = File.createTempFile(TMP_FILE_PREFIX, DOT_EXTENSION);
            FileOutputStream tmpDotOutputStream = null;
            try {
                IOUtils.copy(input, dotContents);
                tmpDotOutputStream = new FileOutputStream(dotInput);
                IOUtils.copy(new ByteArrayInputStream(dotContents.toByteArray()), tmpDotOutputStream);
            } finally {
                IOUtils.closeQuietly(tmpDotOutputStream);
            }
            IStatus result = runDot(format, dimension, dotInput, dotOutput);
            if (dotOutput.isFile()) {
                if (!result.isOK() && Platform.inDebugMode()) LogUtils.log(status);
                return;
            }
        } catch (IOException e) {
            status.add(new Status(IStatus.ERROR, GraphVizActivator.ID, "", e));
        } finally {
            dotInput.delete();
            IOUtils.closeQuietly(input);
        }
        throw new CoreException(status);
    }
