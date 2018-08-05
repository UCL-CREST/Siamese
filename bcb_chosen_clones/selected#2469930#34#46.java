    @Override
    public String getSelectedFile() throws FileSystemBrowserException {
        try {
            Process process = new ProcessBuilder(System.getenv("comspec"), "/c", grabSelectedFile, "|", "find", "/V", "\"\"").start();
            InputStream in = process.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line = reader.readLine();
            reader.close();
            return line;
        } catch (IOException e) {
            throw new FileSystemBrowserException("Error while getting selected file", e);
        }
    }
