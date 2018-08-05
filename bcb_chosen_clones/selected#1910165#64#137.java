    public void onMessage(Message message) {
        LOG.debug("onMessage");
        DownloadMessage downloadMessage;
        try {
            downloadMessage = new DownloadMessage(message);
        } catch (JMSException e) {
            LOG.error("JMS error: " + e.getMessage(), e);
            return;
        }
        String caName = downloadMessage.getCaName();
        boolean update = downloadMessage.isUpdate();
        LOG.debug("issuer: " + caName);
        CertificateAuthorityEntity certificateAuthority = this.certificateAuthorityDAO.findCertificateAuthority(caName);
        if (null == certificateAuthority) {
            LOG.error("unknown certificate authority: " + caName);
            return;
        }
        if (!update && Status.PROCESSING != certificateAuthority.getStatus()) {
            LOG.debug("CA status not marked for processing");
            return;
        }
        String crlUrl = certificateAuthority.getCrlUrl();
        if (null == crlUrl) {
            LOG.warn("No CRL url for CA " + certificateAuthority.getName());
            certificateAuthority.setStatus(Status.NONE);
            return;
        }
        NetworkConfig networkConfig = this.configurationDAO.getNetworkConfig();
        HttpClient httpClient = new HttpClient();
        if (null != networkConfig) {
            httpClient.getHostConfiguration().setProxy(networkConfig.getProxyHost(), networkConfig.getProxyPort());
        }
        HttpClientParams httpClientParams = httpClient.getParams();
        httpClientParams.setParameter("http.socket.timeout", new Integer(1000 * 20));
        LOG.debug("downloading CRL from: " + crlUrl);
        GetMethod getMethod = new GetMethod(crlUrl);
        getMethod.addRequestHeader("User-Agent", "jTrust CRL Client");
        int statusCode;
        try {
            statusCode = httpClient.executeMethod(getMethod);
        } catch (Exception e) {
            downloadFailed(caName, crlUrl);
            throw new RuntimeException();
        }
        if (HttpURLConnection.HTTP_OK != statusCode) {
            LOG.debug("HTTP status code: " + statusCode);
            downloadFailed(caName, crlUrl);
            throw new RuntimeException();
        }
        String crlFilePath;
        File crlFile = null;
        try {
            crlFile = File.createTempFile("crl-", ".der");
            InputStream crlInputStream = getMethod.getResponseBodyAsStream();
            OutputStream crlOutputStream = new FileOutputStream(crlFile);
            IOUtils.copy(crlInputStream, crlOutputStream);
            IOUtils.closeQuietly(crlInputStream);
            IOUtils.closeQuietly(crlOutputStream);
            crlFilePath = crlFile.getAbsolutePath();
            LOG.debug("temp CRL file: " + crlFilePath);
        } catch (IOException e) {
            downloadFailed(caName, crlUrl);
            if (null != crlFile) {
                crlFile.delete();
            }
            throw new RuntimeException(e);
        }
        try {
            this.notificationService.notifyHarvester(caName, crlFilePath, update);
        } catch (JMSException e) {
            crlFile.delete();
            throw new RuntimeException(e);
        }
    }
