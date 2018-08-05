    public jnamed(String conffile) throws IOException, ZoneTransferException {
        FileInputStream fs;
        List ports = new ArrayList();
        List addresses = new ArrayList();
        try {
            fs = new FileInputStream(conffile);
        } catch (Exception e) {
            System.out.println("Cannot open " + conffile);
            return;
        }
        try {
            caches = new HashMap();
            znames = new HashMap();
            TSIGs = new HashMap();
            InputStreamReader isr = new InputStreamReader(fs);
            BufferedReader br = new BufferedReader(isr);
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
                    caches.put(new Integer(DClass.IN), cache);
                } else if (keyword.equals("key")) addTSIG(st.nextToken(), st.nextToken()); else if (keyword.equals("port")) ports.add(Integer.valueOf(st.nextToken())); else if (keyword.equals("address")) {
                    String addr = st.nextToken();
                    addresses.add(InetAddress.getByName(addr));
                } else {
                    System.out.println("unknown keyword: " + keyword);
                }
            }
            if (ports.size() == 0) ports.add(new Integer(53));
            if (addresses.size() == 0) addresses.add(null);
            Iterator iaddr = addresses.iterator();
            while (iaddr.hasNext()) {
                InetAddress addr = (InetAddress) iaddr.next();
                Iterator iport = ports.iterator();
                while (iport.hasNext()) {
                    int port = ((Integer) iport.next()).intValue();
                    String addrString;
                    addUDP(addr, port);
                    addTCP(addr, port);
                    if (addr == null) addrString = "0.0.0.0"; else addrString = addr.getHostAddress();
                    System.out.println("jnamed: listening on " + addrString + "#" + port);
                }
            }
            System.out.println("jnamed: running");
        } finally {
            fs.close();
        }
    }
