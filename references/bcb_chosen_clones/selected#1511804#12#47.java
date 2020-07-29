    public static void getCityAndProvince() {
        BufferedReader bufferedReader = null;
        StringBuilder sb = new StringBuilder();
        try {
            bufferedReader = new BufferedReader(new FileReader("E:\\Study\\Search\\code\\trunk\\owl\\nkjs.test.t.owl"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String s = null;
        try {
            while ((s = bufferedReader.readLine()) != null) {
                sb.append(s);
                sb.append("\n");
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        try {
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String rs = sb.toString();
        Pattern p = Pattern.compile("rdf:ID=\"C(.)+\"", Pattern.CASE_INSENSITIVE);
        Matcher matcher = p.matcher(rs);
        while (matcher.find()) {
            s = rs.substring(matcher.start() + 8, matcher.end() - 1);
            System.out.println(s);
        }
        p = Pattern.compile("rdf:ID=\"P(.)+\"", Pattern.CASE_INSENSITIVE);
        matcher = p.matcher(rs);
        while (matcher.find()) {
            s = rs.substring(matcher.start() + 8, matcher.end() - 1);
            System.out.println(s);
        }
    }
