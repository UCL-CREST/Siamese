    public int getUrl() {
        try {
            final URL url = new URL(this.url);
            conn = url.openConnection();
            if (cookies != null) {
                conn.setRequestProperty("Cookie", cookies);
            }
            InputStreamReader inputstream = new InputStreamReader(conn.getInputStream(), charset);
            charset = inputstream.getEncoding();
            BufferedReader input = new BufferedReader(inputstream);
            String line;
            while ((line = input.readLine()) != null) {
                content += line + "\n";
            }
            return 0;
        } catch (MalformedURLException e) {
            return 1;
        } catch (IOException e2) {
            return 2;
        }
    }
