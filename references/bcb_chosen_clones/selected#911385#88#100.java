    private static String getData(String myurl) throws Exception {
        System.out.println("getdata");
        URL url = new URL(myurl);
        uc = (HttpURLConnection) url.openConnection();
        br = new BufferedReader(new InputStreamReader(uc.getInputStream()));
        String temp = "", k = "";
        while ((temp = br.readLine()) != null) {
            System.out.println(temp);
            k += temp;
        }
        br.close();
        return k;
    }
