    public static byte[] getbytes(String host, int port, String cmd) {
        String result = "GetHtmlFromServer no answer";
        String tmp = "";
        result = "";
        try {
            tmp = "http://" + host + ":" + port + "/" + cmd;
            URL url = new URL(tmp);
            if (1 == 2) {
                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                String str;
                while ((str = in.readLine()) != null) {
                    result += str;
                }
                in.close();
                return result.getBytes();
            } else {
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setConnectTimeout(2 * 1000);
                c.setRequestMethod("GET");
                c.connect();
                int amt = c.getContentLength();
                InputStream in = c.getInputStream();
                MojasiWriter writer = new MojasiWriter();
                byte[] buff = new byte[256];
                while (writer.size() < amt) {
                    int got = in.read(buff);
                    if (got < 0) break;
                    writer.pushBytes(buff, got);
                }
                in.close();
                c.disconnect();
                return writer.getBytes();
            }
        } catch (MalformedURLException e) {
            System.err.println(tmp + " " + e);
        } catch (IOException e) {
            ;
        }
        return null;
    }
