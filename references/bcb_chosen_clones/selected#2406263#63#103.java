    private void downloadFiles() throws SocketException, IOException {
        HashSet<String> files_set = new HashSet<String>();
        boolean hasWildcarts = false;
        FTPClient client = new FTPClient();
        for (String file : downloadFiles) {
            files_set.add(file);
            if (file.contains(WILDCARD_WORD) || file.contains(WILDCARD_DIGIT)) hasWildcarts = true;
        }
        client.connect(source.getHost());
        client.login(username, password);
        FTPFile[] files = client.listFiles(source.getPath());
        if (!hasWildcarts) {
            for (FTPFile file : files) {
                String filename = file.getName();
                if (files_set.contains(filename)) {
                    long file_size = file.getSize() / 1024;
                    Calendar cal = file.getTimestamp();
                    URL source_file = new File(source + file.getName()).toURI().toURL();
                    DownloadQueue.add(new Download(projectName, parser.getParserID(), source_file, file_size, cal, target + file.getName()));
                }
            }
        } else {
            for (FTPFile file : files) {
                String filename = file.getName();
                boolean match = false;
                for (String db_filename : downloadFiles) {
                    db_filename = db_filename.replaceAll("\\" + WILDCARD_WORD, WILDCARD_WORD_PATTERN);
                    db_filename = db_filename.replaceAll("\\" + WILDCARD_DIGIT, WILDCARD_DIGIT_PATTERN);
                    Pattern p = Pattern.compile(db_filename);
                    Matcher m = p.matcher(filename);
                    match = m.matches();
                }
                if (match) {
                    long file_size = file.getSize() / 1024;
                    Calendar cal = file.getTimestamp();
                    URL source_file = new File(source + file.getName()).toURI().toURL();
                    DownloadQueue.add(new Download(projectName, parser.getParserID(), source_file, file_size, cal, target + file.getName()));
                }
            }
        }
    }
