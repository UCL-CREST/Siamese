    protected void setOuterIP() {
        try {
            URL url = new URL("http://elm-ve.sf.net/ipCheck/ipCheck.cgi");
            InputStreamReader isr = new InputStreamReader(url.openStream());
            BufferedReader br = new BufferedReader(isr);
            String ip = br.readLine();
            ip = ip.trim();
            bridgeOutIPTF.setText(ip);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
