    public void create_list() {
        try {
            String data = URLEncoder.encode("PHPSESSID", "UTF-8") + "=" + URLEncoder.encode(this.get_session(), "UTF-8");
            URL url = new URL(URL_LOLA + FILE_CREATE_LIST);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            line = rd.readLine();
            wr.close();
            rd.close();
            System.out.println("Gene list saved in LOLA");
        } catch (Exception e) {
            System.out.println("error in createList()");
            e.printStackTrace();
        }
    }
