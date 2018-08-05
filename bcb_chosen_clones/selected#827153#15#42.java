    public void run() {
        try {
            String s = (new StringBuilder()).append("fName=").append(URLEncoder.encode("???", "UTF-8")).append("&lName=").append(URLEncoder.encode("???", "UTF-8")).toString();
            URL url = new URL("http://snoop.minecraft.net/");
            HttpURLConnection httpurlconnection = (HttpURLConnection) url.openConnection();
            httpurlconnection.setRequestMethod("POST");
            httpurlconnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            httpurlconnection.setRequestProperty("Content-Length", (new StringBuilder()).append("").append(Integer.toString(s.getBytes().length)).toString());
            httpurlconnection.setRequestProperty("Content-Language", "en-US");
            httpurlconnection.setUseCaches(false);
            httpurlconnection.setDoInput(true);
            httpurlconnection.setDoOutput(true);
            DataOutputStream dataoutputstream = new DataOutputStream(httpurlconnection.getOutputStream());
            dataoutputstream.writeBytes(s);
            dataoutputstream.flush();
            dataoutputstream.close();
            java.io.InputStream inputstream = httpurlconnection.getInputStream();
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(inputstream));
            StringBuffer stringbuffer = new StringBuffer();
            String s1;
            while ((s1 = bufferedreader.readLine()) != null) {
                stringbuffer.append(s1);
                stringbuffer.append('\r');
            }
            bufferedreader.close();
        } catch (Exception exception) {
        }
    }
