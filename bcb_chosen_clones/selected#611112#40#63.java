    public String readRemoteFile() throws IOException {
        String response = "";
        boolean eof = false;
        URL url = new URL(StaticData.remoteFile);
        InputStream is = url.openStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String s;
        s = br.readLine();
        response = s;
        while (!eof) {
            try {
                s = br.readLine();
                if (s == null) {
                    eof = true;
                    br.close();
                } else response += s;
            } catch (EOFException eo) {
                eof = true;
            } catch (IOException e) {
                System.out.println("IO Error : " + e.getMessage());
            }
        }
        return response;
    }
