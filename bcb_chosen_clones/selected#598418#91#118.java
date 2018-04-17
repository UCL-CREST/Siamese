    public void postData(String protocol, String host, String form, String data) throws Exception {
        if ((protocol == null) || (protocol.equals(""))) {
            protocol = DEFAULT_PROTOCOL;
        }
        if ((host == null) || (host.equals(""))) {
            host = DEFAULT_HOST;
        }
        if (form == null) {
            form = DEFAULT_FORM;
        }
        if (data == null) {
            throw new IllegalArgumentException("Invalid data");
        }
        URL url = new URL(protocol, host, form);
        URLConnection con = url.openConnection();
        con.setDoOutput(true);
        con.setDoInput(true);
        con.setUseCaches(false);
        con.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
        con.setRequestProperty("Content-length", String.valueOf(data.length()));
        PrintStream out = new PrintStream(con.getOutputStream(), true);
        out.print(data);
        out.close();
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        while (in.readLine() != null) {
        }
        in.close();
    }
