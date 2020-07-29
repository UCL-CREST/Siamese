    public String login() {
        System.out.println("Logging in to LOLA");
        try {
            String data = URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(get_email(), "UTF-8");
            data += "&" + URLEncoder.encode("pw", "UTF-8") + "=" + URLEncoder.encode(get_pw(), "UTF-8");
            URL url = new URL(URL_LOLA + FILE_LOGIN);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line, sessid;
            line = rd.readLine();
            sessid = get_sessid(line);
            this.set_session(sessid);
            wr.close();
            rd.close();
            return sessid;
        } catch (Exception e) {
            System.out.println("Login Error");
            return "";
        }
    }
