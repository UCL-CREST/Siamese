    public static Image load(final InputStream input, String format, Point dimension) throws CoreException {
        MultiStatus status = new MultiStatus(GraphVizActivator.ID, 0, "Errors occurred while running Graphviz", null);
        File dotInput = null, dotOutput = null;
        ByteArrayOutputStream dotContents = new ByteArrayOutputStream();
        try {
            dotInput = File.createTempFile(TMP_FILE_PREFIX, DOT_EXTENSION);
            dotOutput = File.createTempFile(TMP_FILE_PREFIX, "." + format);
            dotOutput.delete();
            FileOutputStream tmpDotOutputStream = null;
            try {
                IOUtils.copy(input, dotContents);
                tmpDotOutputStream = new FileOutputStream(dotInput);
                IOUtils.copy(new ByteArrayInputStream(dotContents.toByteArray()), tmpDotOutputStream);
            } finally {
                IOUtils.closeQuietly(tmpDotOutputStream);
            }
            IStatus result = runDot(format, dimension, dotInput, dotOutput);
            status.add(result);
            status.add(logInput(dotContents));
            if (dotOutput.isFile()) {
                if (!result.isOK() && Platform.inDebugMode()) LogUtils.log(status);
                ImageLoader loader = new ImageLoader();
                ImageData[] imageData = loader.load(dotOutput.getAbsolutePath());
                return new Image(Display.getDefault(), imageData[0]);
            }
        } catch (SWTException e) {
            status.add(new Status(IStatus.ERROR, GraphVizActivator.ID, "", e));
        } catch (IOException e) {
            status.add(new Status(IStatus.ERROR, GraphVizActivator.ID, "", e));
        } finally {
            dotInput.delete();
            dotOutput.delete();
            IOUtils.closeQuietly(input);
        }
        throw new CoreException(status);
    }
