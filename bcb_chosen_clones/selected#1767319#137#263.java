    public TranscodeInputStream startTranscodeProcess(String localPath, String videoCodec, int videoBitrate, String audioCodec, int audioBitrate, String scale, boolean deint, String mux) throws Throwable {
        readProperties();
        String fileExt = (String) fileExtMap.get(mux);
        if (fileExt == null) {
            throw new IOException("Invalid mux specified");
        }
        if (streams.size() >= maxStreams) {
            throw new IOException("Maximum number of streams reached.");
        }
        ServerSocket socket = findAvailablePort();
        if (socket == null) {
            throw new IOException("Unable to allocate port for streaming " + "between specified ports " + startPort + "-" + stopPort);
        }
        int port = socket.getLocalPort();
        StringBuffer cmdBuff = new StringBuffer();
        cmdBuff.append(" \"");
        cmdBuff.append(vlcCmd);
        cmdBuff.append("\" ");
        cmdBuff.append(vlcOpts);
        replaceFirst(cmdBuff, OPT_TOKEN_LOCAL_PATH, localPath);
        replaceFirst(cmdBuff, OPT_TOKEN_VIDEO_CODEC, videoCodec);
        replaceFirst(cmdBuff, OPT_TOKEN_VIDEO_BITRATE, String.valueOf(videoBitrate));
        replaceFirst(cmdBuff, OPT_TOKEN_AUDIO_CODEC, audioCodec);
        replaceFirst(cmdBuff, OPT_TOKEN_AUDIO_BITRATE, String.valueOf(audioBitrate));
        replaceFirst(cmdBuff, OPT_TOKEN_SERVERPORT, localhostAddr.getHostAddress() + ":" + port);
        replaceFirst(cmdBuff, OPT_TOKEN_SCALE, scale);
        replaceFirst(cmdBuff, OPT_TOKEN_MUX, mux);
        if (deint) {
            replaceFirst(cmdBuff, OPT_TOKEN_DEINT, ",deinterlace");
        } else {
            replaceFirst(cmdBuff, OPT_TOKEN_DEINT, "");
        }
        if (debugLog) Acme.Serve.Serve.extLog("OS: " + System.getProperty("os.name"));
        if (System.getProperty("os.name").toLowerCase().startsWith("linux")) {
            if (debugLog) Acme.Serve.Serve.extLog("Converting Command for Linux: " + cmdBuff);
            String UQ = "(?<=[^\\\\])\\\"";
            String US = "(?<=[^\\\\])\\s";
            Pattern p = Pattern.compile(UQ + ".*?" + UQ);
            Matcher m = p.matcher(cmdBuff.toString());
            StringBuffer sb = new StringBuffer();
            while (m.find()) {
                String s = cmdBuff.toString().substring(m.start() + 1, m.end() - 1);
                Acme.Serve.Serve.extLog("s1: " + s);
                s = s.replaceAll(US, "\\\\ ");
                Acme.Serve.Serve.extLog("s2: " + s);
                s = s.replace("\\", "\\\\");
                Acme.Serve.Serve.extLog("s3: " + s);
                m.appendReplacement(sb, s);
            }
            m.appendTail(sb);
            cmdBuff = sb;
            replaceFirst(cmdBuff, "--dummy-quiet", "");
        }
        TranscodeInputStream is = null;
        synchronized (streams) {
            socket.close();
            if (debugLog) Acme.Serve.Serve.extLog("Executing command: " + cmdBuff.substring(1));
            final Process proc = Runtime.getRuntime().exec(cmdBuff.toString().substring(1), null, vlcWkDir);
            if (proc != null) {
                Thread stdout_reader = new Thread() {

                    public void run() {
                        BufferedReader proc_out = new BufferedReader(new java.io.InputStreamReader(proc.getInputStream()));
                        String line;
                        try {
                            while (null != (line = proc_out.readLine())) if (debugLog) Acme.Serve.Serve.extLog("VLC(out): " + line);
                        } catch (IOException e) {
                        }
                        try {
                            proc_out.close();
                        } catch (IOException e) {
                        }
                    }
                };
                stdout_reader.start();
                Thread stderr_reader = new Thread() {

                    public void run() {
                        BufferedReader proc_err = new BufferedReader(new java.io.InputStreamReader(proc.getErrorStream()));
                        String line;
                        try {
                            while (null != (line = proc_err.readLine())) if (debugLog) Acme.Serve.Serve.extLog("VLC(err): " + line);
                        } catch (IOException e) {
                        }
                        try {
                            proc_err.close();
                        } catch (IOException e) {
                        }
                    }
                };
                stderr_reader.start();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                int retryCount = 0;
                URL url = new URL("http://" + localhostAddr.getHostAddress() + ":" + String.valueOf(socket.getLocalPort()));
                while (retryCount < maxRetries) {
                    try {
                        URLConnection conn = url.openConnection();
                        if (debugLog) Acme.Serve.Serve.extLog("Connected to " + conn.toString());
                        is = new TranscodeInputStream(conn.getInputStream(), proc, port, fileExt, serverBuffer);
                        streams.add(is);
                        if (debugLog) Acme.Serve.Serve.extLog("Returning InputStream: " + is);
                        return is;
                    } catch (IOException ex) {
                        Acme.Serve.Serve.extLog("Unable to connect, retrying. " + ex);
                        retryCount++;
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                throw new IOException("Too many retries");
            } catch (Throwable e) {
                Acme.Serve.Serve.extLog("Failed to set up InputStream: " + e);
                Acme.Serve.Serve.extLog("Shutting down vlc transcoder process");
                if (proc != null) proc.destroy();
                throw e;
            }
        }
    }
