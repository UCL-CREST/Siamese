    public jnamed(String conffile) throws IOException {
        FileInputStream fs;
        Vector ports = new Vector();
        Vector addresses = new Vector();
        try {
            fs = new FileInputStream(conffile);
        } catch (Exception e) {
            System.out.println("Cannot open " + conffile);
            return;
        }
        caches = new Hashtable();
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
            if (keyword.charAt(0) == '#') continue;
            if (keyword.equals("primary")) addPrimaryZone(st.nextToken(), st.nextToken()); else if (keyword.equals("secondary")) addSecondaryZone(st.nextToken(), st.nextToken()); else if (keyword.equals("cache")) {
                Cache cache = new Cache(st.nextToken());
                caches.put(new Short(DClass.IN), cache);
            } else if (keyword.equals("key")) addTSIG(st.nextToken(), st.nextToken()); else if (keyword.equals("port")) ports.addElement(Short.valueOf(st.nextToken())); else if (keyword.equals("address")) {
                String addr = st.nextToken();
                addresses.addElement(InetAddress.getByName(addr));
            } else {
                System.out.println("ignoring invalid keyword: " + keyword);
            }
        }
        if (ports.size() == 0) ports.addElement(new Short((short) 53));
        if (addresses.size() == 0) addresses.addElement(null);
        Enumeration eaddr = addresses.elements();
        while (eaddr.hasMoreElements()) {
            InetAddress addr = (InetAddress) eaddr.nextElement();
            Enumeration eport = ports.elements();
            while (eport.hasMoreElements()) {
                short port = ((Short) eport.nextElement()).shortValue();
                String addrString;
                addUDP(addr, port);
                addTCP(addr, port);
                if (addr == null) addrString = "0.0.0.0"; else addrString = addr.getHostAddress();
                System.out.println("jnamed: listening on " + addrString + "#" + port);
            }
        }
        System.out.println("jnamed: running");
    }
