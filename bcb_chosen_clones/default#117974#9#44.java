    public static void main(String[] args) {
        StringBuffer htmlPage;
        htmlPage = new StringBuffer();
        double min = 99999.99;
        double max = 0;
        double value = 0;
        try {
            URL url = new URL("http://search.ebay.com/" + args[0]);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String line;
            while ((line = in.readLine()) != null) {
                htmlPage.append(line);
                htmlPage.append("\n");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Pattern p = Pattern.compile("\\$([\\d\\.]+)", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(htmlPage);
        while (m.find()) {
            if (m.start(0) < m.end(0)) {
                value = Double.parseDouble(m.group(1));
                if (value < min) {
                    min = value;
                }
                if (value > max) {
                    max = value;
                }
            }
        }
        if (min == 99999.99) {
            min = 0;
        }
        System.out.println(args[0] + "," + min + "," + max);
        System.exit(0);
    }
