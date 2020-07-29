    public Object downloadFile(File destinationFile, URL[] urls, DownloadListener listener, Object checksum, long length, PRIORITY priority) throws DownloadException {
        URL url = urls[0];
        if (!url.getProtocol().equalsIgnoreCase("http")) {
            throw new DownloadException(" Only HTTP is supported in this version ");
        }
        if (!destinationFile.exists()) {
            try {
                destinationFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                throw new DownloadException("Unable to download from URL : " + url.toString());
            }
        }
        HeadMethod head = new HeadMethod(url.toString());
        HttpClient httpClient = new HttpClient();
        try {
            httpClient.executeMethod(head);
            Header[] headers = head.getResponseHeaders();
            for (Header header : headers) {
                System.out.println(header);
            }
            Header header = head.getResponseHeader("Content-Length");
            Object contentLength = header.getValue();
            Long fileLength = Long.parseLong(contentLength.toString());
            System.out.println(length + " : " + fileLength);
            GetMethod get = new GetMethod(url.toString());
            httpClient.executeMethod(get);
            InputStream ins = get.getResponseBodyAsStream();
            FileOutputStream fos = new FileOutputStream(destinationFile);
            IOUtils.copy(ins, fos);
            System.out.println(" DOWNLOADED FILE");
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
