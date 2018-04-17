    public String execute(String param) {
        String str;
        try {
            Runtime r = Runtime.getRuntime();
            Process p = r.exec("/tmp/instant-soap/e-science-tool/test " + param);
            InputStream is = p.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            str = br.readLine();
        } catch (Exception e) {
            str = e.toString();
        }
        return str;
    }
