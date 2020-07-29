    public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
        File archive = new File(EncoderPlugin.getDefault().getStateLocation().toFile(), "ffmpeg-0.5.zip");
        String message = "Downloading FFMpeg, contacting downloads.sourceforge.net";
        LOGGER.info(message);
        monitor.beginTask(message, 1);
        HttpClient client = new HttpClient();
        HttpMethod method = new GetMethod(DOWNLOAD_URI);
        int statusCode = HttpStatus.SC_SERVICE_UNAVAILABLE;
        try {
            statusCode = client.executeMethod(method);
        } catch (HttpException exception) {
            monitor.setCanceled(true);
            throw new InvocationTargetException(exception);
        } catch (IOException exception) {
            monitor.setCanceled(true);
            throw new InvocationTargetException(exception);
        }
        if (statusCode != HttpStatus.SC_OK) {
            LOGGER.error(MessageFormat.format("Can't download ffmpeg.zip from zourceforge, " + "status = [{0}]", statusCode));
            monitor.setCanceled(true);
            return;
        }
        monitor.worked(1);
        Header header = method.getResponseHeader("Content-Length");
        long contentLength = 3173544;
        if (header != null) {
            try {
                contentLength = Long.parseLong(header.getValue());
            } catch (NumberFormatException nfe) {
            }
        }
        monitor.beginTask("Downloading FFMpeg, " + DOWNLOAD_URI, (int) contentLength);
        OutputStream archiveOutput = null;
        try {
            archiveOutput = new FileOutputStream(archive);
        } catch (FileNotFoundException exception) {
            monitor.setCanceled(true);
            throw new InvocationTargetException(exception, "Can't write temporary download file");
        }
        InputStream responseInput = null;
        try {
            responseInput = method.getResponseBodyAsStream();
        } catch (IOException exception) {
            monitor.setCanceled(true);
            throw new InvocationTargetException(exception);
        }
        byte[] buffer = new byte[1024 * 4];
        int count = -1;
        try {
            while ((count = responseInput.read(buffer)) != -1) {
                if (monitor.isCanceled()) {
                    return;
                }
                archiveOutput.write(buffer, 0, count);
                monitor.worked(count);
            }
            archiveOutput.close();
            responseInput.close();
        } catch (IOException exception) {
            monitor.setCanceled(true);
            throw new InvocationTargetException(exception);
        }
        monitor.beginTask("Downloading FFMpeg, extracting executable", 1);
        try {
            ZipFile zipFile = new ZipFile(archive);
            ZipEntry entry = zipFile.getEntry("ffmpeg-0.5/ffmpeg.exe");
            InputStream input = zipFile.getInputStream(entry);
            OutputStream output = new FileOutputStream(target);
            IOUtils.copy(input, output);
            input.close();
            output.close();
        } catch (IOException exception) {
            monitor.setCanceled(true);
            throw new InvocationTargetException(exception, "Can't unzip ffmpeg.exe");
        }
        monitor.worked(1);
        monitor.done();
    }
