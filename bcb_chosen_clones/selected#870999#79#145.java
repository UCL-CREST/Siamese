    public void check(CheckListener listener) {
        ProcessBuilder builder = new ProcessBuilder(getFFMpeg(), "-version");
        Process process = null;
        try {
            process = builder.start();
            process.getOutputStream().close();
            process.getInputStream().close();
            process.getErrorStream().close();
        } catch (IOException ioe) {
            listener.failed(Category.ENCODER, "Error running ffmpeg", ioe);
            return;
        }
        try {
            process.waitFor();
        } catch (InterruptedException ie) {
            listener.failed(Category.ENCODER, "Error waiting for ffmpeg termination", ie);
            return;
        }
        listener.checked(Category.ENCODER, "Success running ffmpeg");
        builder = new ProcessBuilder(getFFMpeg(), "-formats");
        try {
            process = builder.start();
            Thread.sleep(100);
            process.getOutputStream().close();
            process.getErrorStream().close();
        } catch (IOException ioe) {
            listener.failed(Category.ENCODER, "Error running ffmpeg", ioe);
            return;
        } catch (InterruptedException e) {
            listener.failed(Category.ENCODER, "Error running ffmpeg", e);
            return;
        }
        InputStream standard = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(standard));
        StringBuilder ffmpegFormats = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                ffmpegFormats.append(line).append("\n");
            }
        } catch (IOException ioe) {
            listener.failed(Category.ENCODER, "Error reading ffmpeg formats list", ioe);
            return;
        }
        try {
            process.waitFor();
        } catch (InterruptedException ie) {
            listener.failed(Category.ENCODER, "Error waiting for ffmpeg termination", ie);
            return;
        }
        configuration = new FFMpegConfiguration();
        configuration.guessConfiguration(ffmpegFormats.toString());
        if (configuration.getFlvcodec() == null) {
            listener.failed(Category.ENCODER, "FFMpeg formats list misses FLV", null);
            return;
        }
        if (configuration.getMp3codec() == null) {
            listener.failed(Category.ENCODER, "FFMpeg formats list misses MP3", null);
            return;
        }
        listener.checked(Category.ENCODER, "Success hinting ffmpeg audio formats");
        if (configuration.getMpegcodec() == null) {
            listener.failed(Category.ENCODER, "FFMpeg formats list misses MPEG", null);
            return;
        }
        listener.checked(Category.ENCODER, "Success hinting ffmpeg video formats");
    }
