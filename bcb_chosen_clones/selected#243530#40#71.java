    protected void download(URL url, File destination, long beginRange, long endRange, long totalFileSize, boolean appendToFile) throws DownloadException {
        System.out.println(" DOWNLOAD REQUEST RECEIVED " + url.toString() + " \n\tbeginRange : " + beginRange + " - EndRange " + endRange + " \n\t to -> " + destination.getAbsolutePath());
        try {
            if (destination.exists() && !appendToFile) {
                destination.delete();
            }
            if (!destination.exists()) destination.createNewFile();
            GetMethod get = new GetMethod(url.toString());
            HttpClient httpClient = new HttpClient();
            Header rangeHeader = new Header();
            rangeHeader.setName("Range");
            rangeHeader.setValue("bytes=" + beginRange + "-" + endRange);
            get.setRequestHeader(rangeHeader);
            httpClient.executeMethod(get);
            int statusCode = get.getStatusCode();
            if (statusCode >= 400 && statusCode < 500) throw new DownloadException("The file does not exist in this location : message from server ->  " + statusCode + " " + get.getStatusText());
            InputStream input = get.getResponseBodyAsStream();
            OutputStream output = new FileOutputStream(destination, appendToFile);
            try {
                int length = IOUtils.copy(input, output);
                System.out.println(" Length : " + length);
            } finally {
                input.close();
                output.flush();
                output.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Unable to figure out the length of the file from the URL : " + e.getMessage());
            throw new DownloadException("Unable to figure out the length of the file from the URL : " + e.getMessage());
        }
    }
