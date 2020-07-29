    private void downloadDirectory() throws SocketException, IOException {
        FTPClient client = new FTPClient();
        client.connect(source.getHost());
        client.login(username, password);
        FTPFile[] files = client.listFiles(source.getPath());
        for (FTPFile file : files) {
            if (!file.isDirectory()) {
                long file_size = file.getSize() / 1024;
                Calendar cal = file.getTimestamp();
                URL source_file = new File(source + file.getName()).toURI().toURL();
                DownloadQueue.add(new Download(projectName, parser.getParserID(), source_file, file_size, cal, target + file.getName()));
            }
        }
    }
