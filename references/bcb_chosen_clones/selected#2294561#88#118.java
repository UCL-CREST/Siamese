    private Map getBlackHoleData() throws Exception {
        File dataFile = new File(Kit.getDataDir() + BLACK_HOLE);
        if (dataFile.exists() && daysOld(dataFile) < 1) {
            return getStoredData(dataFile);
        }
        InputStream stream = null;
        try {
            String bh_url = "http://www.critique.org/users/critters/blackholes/sightdata.html";
            URL url = new URL(bh_url);
            stream = url.openStream();
        } catch (IOException e) {
            return getStoredData(dataFile);
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        StringBuffer data = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            data.append(line);
        }
        br.close();
        Pattern p = Pattern.compile(regexp);
        Matcher m = p.matcher(data);
        Map map = new THashMap();
        while (m.find()) {
            map.put(m.group(1).trim(), new ReplyTimeDatum(Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)), 0, Integer.parseInt(m.group(2))));
        }
        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(dataFile));
        oos.writeObject(map);
        oos.close();
        return map;
    }
