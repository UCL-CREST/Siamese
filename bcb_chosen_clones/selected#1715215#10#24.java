    public static void request() {
        try {
            URL url = new URL("http://www.nseindia.com/marketinfo/companyinfo/companysearch.jsp?cons=ghcl&section=7");
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                System.out.println(line);
            }
            rd.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
