    public HashMap parseFile(File newfile) throws IOException {
        String s;
        String[] tokens;
        int nvalues = 0;
        double num1, num2, num3;
        boolean baddata = false;
        URL url = newfile.toURL();
        InputStream is = url.openStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        HashMap data = new HashMap();
        while ((s = br.readLine()) != null) {
            tokens = s.split("\\s+");
            nvalues = tokens.length;
            if (nvalues == 2) {
                data.put(new String(tokens[0]), new Double(Double.parseDouble(tokens[1])));
            } else {
                System.out.println("Sorry, trouble reading reference file.");
            }
        }
        return data;
    }
