    public static void copyFile(String hostname, String url, String username, String password, File targetFile) throws Exception {
        org.apache.commons.httpclient.HttpClient client = WebDavUtility.initClient("files-cert.rxhub.net", username, password);
        HttpMethod method = new GetMethod(url);
        client.executeMethod(method);
        BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(targetFile));
        IOUtils.copyLarge(method.getResponseBodyAsStream(), output);
    }
