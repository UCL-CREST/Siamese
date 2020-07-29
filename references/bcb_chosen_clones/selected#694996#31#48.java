    public static String upload(File tmpFile, URL url) throws IOException {
        StringBuffer reply = new StringBuffer();
        URLConnection uc = url.openConnection();
        ClientHttpRequest request = new ClientHttpRequest(uc);
        String file = "file";
        String filename = tmpFile.getName();
        InputStream fileinput = new FileInputStream(tmpFile);
        request.setParameter(file, filename, fileinput);
        InputStream serverInput = request.post();
        BufferedReader in = new BufferedReader(new InputStreamReader(serverInput));
        String line = in.readLine();
        while (line != null) {
            reply.append(line + "\n");
            line = in.readLine();
        }
        in.close();
        return reply.toString();
    }
