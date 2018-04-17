        public void runInternal() {
            connection = null;
            boolean itemsLoadedPartially = false;
            boolean loadNext = false;
            HashSet<String> visited = new HashSet<String>();
            do {
                try {
                    setProgressMessage(url.toString(), -1);
                    visited.add(url.toString());
                    long startTimeStamp = System.currentTimeMillis();
                    delayedProgress = coolReader.getEngine().showProgressDelayed(0, progressMessage, PROGRESS_DELAY_MILLIS);
                    URLConnection conn = url.openConnection();
                    if (conn instanceof HttpsURLConnection) {
                        onError("HTTPs is not supported yet");
                        return;
                    }
                    if (!(conn instanceof HttpURLConnection)) {
                        onError("Only HTTP supported");
                        return;
                    }
                    connection = (HttpURLConnection) conn;
                    connection.setRequestProperty("User-Agent", "CoolReader/3(Android)");
                    if (referer != null) connection.setRequestProperty("Referer", referer);
                    connection.setInstanceFollowRedirects(true);
                    connection.setAllowUserInteraction(false);
                    connection.setConnectTimeout(CONNECT_TIMEOUT);
                    connection.setReadTimeout(READ_TIMEOUT);
                    connection.setDoInput(true);
                    String fileName = null;
                    String disp = connection.getHeaderField("Content-Disposition");
                    if (disp != null) {
                        int p = disp.indexOf("filename=");
                        if (p > 0) {
                            fileName = disp.substring(p + 9);
                        }
                    }
                    int response = -1;
                    response = connection.getResponseCode();
                    L.d("Response: " + response);
                    if (response != 200) {
                        onError("Error " + response);
                        return;
                    }
                    String contentType = connection.getContentType();
                    String contentEncoding = connection.getContentEncoding();
                    int contentLen = connection.getContentLength();
                    L.d("Entity content length: " + contentLen);
                    L.d("Entity content type: " + contentType);
                    L.d("Entity content encoding: " + contentEncoding);
                    setProgressMessage(url.toString(), contentLen);
                    InputStream is = connection.getInputStream();
                    delayedProgress.cancel();
                    is = new ProgressInputStream(is, startTimeStamp, progressMessage, contentLen, 80);
                    final int MAX_CONTENT_LEN_TO_BUFFER = 256 * 1024;
                    boolean isZip = contentType != null && contentType.equals("application/zip");
                    if (expectedType != null) contentType = expectedType; else if (contentLen > 0 && contentLen < MAX_CONTENT_LEN_TO_BUFFER) {
                        byte[] buf = new byte[contentLen];
                        if (is.read(buf) != contentLen) {
                            onError("Wrong content length");
                            return;
                        }
                        is.close();
                        is = null;
                        is = new ByteArrayInputStream(buf);
                        if (findSubstring(buf, "<?xml version=") >= 0 && findSubstring(buf, "<feed") >= 0) contentType = "application/atom+xml";
                    }
                    if (contentType.startsWith("application/atom+xml")) {
                        L.d("Parsing feed");
                        parseFeed(is);
                        itemsLoadedPartially = true;
                        if (handler.docInfo.nextLink != null && handler.docInfo.nextLink.type.startsWith("application/atom+xml;profile=opds-catalog")) {
                            if (handler.entries.size() < MAX_OPDS_ITEMS) {
                                url = new URL(handler.docInfo.nextLink.href);
                                loadNext = !visited.contains(url.toString());
                                L.d("continue with next part: " + url);
                            } else {
                                L.d("max item count reached: " + handler.entries.size());
                                loadNext = false;
                            }
                        } else {
                            loadNext = false;
                        }
                    } else {
                        if (fileName == null) fileName = defaultFileName;
                        L.d("Downloading book: " + contentEncoding);
                        downloadBook(contentType, url.toString(), is, contentLen, fileName, isZip);
                        if (progressShown) coolReader.getEngine().hideProgress();
                        loadNext = false;
                        itemsLoadedPartially = false;
                    }
                } catch (Exception e) {
                    L.e("Exception while trying to open URI " + url.toString(), e);
                    if (progressShown) coolReader.getEngine().hideProgress();
                    onError("Error occured while reading OPDS catalog");
                    break;
                } finally {
                    if (connection != null) try {
                        connection.disconnect();
                    } catch (Exception e) {
                    }
                }
            } while (loadNext);
            if (progressShown) coolReader.getEngine().hideProgress();
            if (itemsLoadedPartially) BackgroundThread.guiExecutor.execute(new Runnable() {

                @Override
                public void run() {
                    L.d("Parsing is finished successfully. " + handler.entries.size() + " entries found");
                    callback.onFinish(handler.docInfo, handler.entries);
                }
            });
        }
