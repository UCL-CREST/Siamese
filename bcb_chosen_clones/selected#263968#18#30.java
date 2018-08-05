    @Override
    public void parse() throws DocumentException, IOException {
        URL url = new URL(getDataUrl());
        URLConnection con = url.openConnection();
        BufferedReader bStream = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String s = bStream.readLine();
        bStream.readLine();
        while ((s = bStream.readLine()) != null) {
            String[] tokens = s.split("\\|");
            ResultUnit unit = new ResultUnit(tokens[3], Float.valueOf(tokens[4]), Integer.valueOf(tokens[2]));
            set.add(unit);
        }
    }
