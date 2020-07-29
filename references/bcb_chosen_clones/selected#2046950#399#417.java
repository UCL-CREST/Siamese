    public String loadURLString(java.net.URL url) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuffer buf = new StringBuffer();
            String s = "";
            while (br.ready() && s != null) {
                s = br.readLine();
                if (s != null) {
                    buf.append(s);
                    buf.append("\n");
                }
            }
            return buf.toString();
        } catch (IOException ex) {
            return "";
        } catch (NullPointerException npe) {
            return "";
        }
    }
