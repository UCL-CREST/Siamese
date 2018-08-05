    public void run() {
        LogPrinter.log(Level.FINEST, "Started Download at : {0, date, long}", new Date());
        if (!PipeConnected) {
            throw new IllegalStateException("You should connect the pipe before with getInputStream()");
        }
        InputStream ins = null;
        if (IsAlreadyDownloaded) {
            LogPrinter.log(Level.FINEST, "The file already Exists open and foward the byte");
            try {
                ContentLength = (int) TheAskedFile.length();
                ContentType = URLConnection.getFileNameMap().getContentTypeFor(TheAskedFile.getName());
                ins = new FileInputStream(TheAskedFile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int read = ins.read(buffer);
                while (read >= 0) {
                    Pipe.write(buffer, 0, read);
                    read = ins.read(buffer);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (ins != null) {
                    try {
                        ins.close();
                    } catch (IOException e) {
                    }
                }
            }
        } else {
            LogPrinter.log(Level.FINEST, "the file does not exist locally so we try to download the thing");
            File theDir = TheAskedFile.getParentFile();
            if (!theDir.exists()) {
                theDir.mkdirs();
            }
            for (URL url : ListFastest) {
                FileOutputStream fout = null;
                boolean OnError = false;
                long timestart = System.currentTimeMillis();
                long bytecount = 0;
                try {
                    URL newUrl = new URL(url.toString() + RequestedFile);
                    LogPrinter.log(Level.FINEST, "the download URL = {0}", newUrl);
                    URLConnection conn = newUrl.openConnection();
                    ContentType = conn.getContentType();
                    ContentLength = conn.getContentLength();
                    ins = conn.getInputStream();
                    fout = new FileOutputStream(TheAskedFile);
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int read = ins.read(buffer);
                    while (read >= 0) {
                        fout.write(buffer, 0, read);
                        Pipe.write(buffer, 0, read);
                        read = ins.read(buffer);
                        bytecount += read;
                    }
                    Pipe.flush();
                } catch (IOException e) {
                    OnError = true;
                } finally {
                    if (ins != null) {
                        try {
                            ins.close();
                        } catch (IOException e) {
                        }
                    }
                    if (fout != null) {
                        try {
                            fout.close();
                        } catch (IOException e) {
                        }
                    }
                }
                long timeend = System.currentTimeMillis();
                if (OnError) {
                    continue;
                } else {
                    long timetook = timeend - timestart;
                    BigDecimal speed = new BigDecimal(bytecount).multiply(new BigDecimal(1000)).divide(new BigDecimal(timetook), MathContext.DECIMAL32);
                    for (ReportCalculatedStatistique report : Listener) {
                        report.reportUrlStat(url, speed, timetook);
                    }
                    break;
                }
            }
        }
        LogPrinter.log(Level.FINEST, "download finished at {0,date,long}", new Date());
        if (Pipe != null) {
            try {
                Pipe.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
