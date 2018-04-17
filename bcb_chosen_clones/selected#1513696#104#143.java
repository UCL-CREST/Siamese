    protected synchronized InputStream openURLConnection(StreamDataSetDescriptor dsd, Datum start, Datum end, StringBuffer additionalFormData) throws DasException {
        String[] tokens = dsd.getDataSetID().split("\\?|\\&");
        String dataSetID = tokens[1];
        try {
            String formData = createFormDataString(dataSetID, start, end, additionalFormData);
            if (dsd.isRestrictedAccess()) {
                key = server.getKey("");
                if (key != null) {
                    formData += "&key=" + URLEncoder.encode(key.toString(), "UTF-8");
                }
            }
            if (redirect) {
                formData += "&redirect=1";
            }
            URL serverURL = this.server.getURL(formData);
            this.lastRequestURL = String.valueOf(serverURL);
            DasLogger.getLogger(DasLogger.DATA_TRANSFER_LOG).info("opening " + serverURL.toString());
            URLConnection urlConnection = serverURL.openConnection();
            urlConnection.connect();
            String contentType = urlConnection.getContentType();
            if (!contentType.equalsIgnoreCase("application/octet-stream")) {
                BufferedReader bin = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line = bin.readLine();
                String message = "";
                while (line != null) {
                    message = message.concat(line);
                    line = bin.readLine();
                }
                throw new DasIOException(message);
            }
            InputStream in = urlConnection.getInputStream();
            if (isLegacyStream()) {
                return processLegacyStream(in);
            } else {
                throw new UnsupportedOperationException();
            }
        } catch (IOException e) {
            throw new DasIOException(e);
        }
    }
