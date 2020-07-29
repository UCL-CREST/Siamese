    public boolean optimize(int coreId) {
        try {
            URL url = new URL(solrUrl + "/core" + coreId + "/update");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-type", "text/xml");
            conn.setRequestProperty("charset", "utf-8");
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            System.out.println("******************optimizing");
            wr.write("<optimize/>");
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
            }
            wr.close();
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
