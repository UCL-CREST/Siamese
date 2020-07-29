    public void seeURLConnection() throws Exception {
        URL url = new URL("http://wantmeet.iptime.org");
        URLConnection uc = url.openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
        String s = null;
        StringBuffer sb = new StringBuffer();
        while ((s = br.readLine()) != null) {
            sb.append(s);
        }
        br.close();
        log.debug("sb=[" + sb.toString() + "]");
    }
