    private List parseUrlGetUids(String url) throws FetchError {
        List hids = new ArrayList();
        try {
            InputStream is = (new URL(url)).openStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            StringBuffer buffer = new StringBuffer();
            String inputLine = "";
            Pattern pattern = Pattern.compile("\\<input\\s+type=hidden\\s+name=hid\\s+value=(\\d+)\\s?\\>", Pattern.CASE_INSENSITIVE);
            while ((inputLine = in.readLine()) != null) {
                Matcher matcher = pattern.matcher(inputLine);
                if (matcher.find()) {
                    String id = matcher.group(1);
                    if (!hids.contains(id)) {
                        hids.add(id);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
            throw new FetchError(e);
        }
        return hids;
    }
