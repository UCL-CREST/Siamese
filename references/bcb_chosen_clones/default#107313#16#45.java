    public jnamed(String conffile) throws IOException {
        FileInputStream fs;
        try {
            fs = new FileInputStream(conffile);
        } catch (Exception e) {
            System.out.println("Cannot open " + conffile);
            return;
        }
        cache = null;
        znames = new Hashtable();
        TSIGs = new Hashtable();
        BufferedReader br = new BufferedReader(new InputStreamReader(fs));
        String line = null;
        while ((line = br.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(line);
            if (!st.hasMoreTokens()) continue;
            String keyword = st.nextToken();
            if (!st.hasMoreTokens()) {
                System.out.println("Invalid line: " + line);
                continue;
            }
            if (keyword.equals("primary")) addZone(st.nextToken(), Zone.PRIMARY); else if (keyword.equals("cache")) cache = new Zone(st.nextToken(), Zone.CACHE, null); else if (keyword.equals("key")) addTSIG(st.nextToken(), st.nextToken());
        }
        if (cache == null) {
            System.out.println("no cache specified");
            System.exit(-1);
        }
        addUDP((short) 12345);
        addTCP((short) 12345);
    }
