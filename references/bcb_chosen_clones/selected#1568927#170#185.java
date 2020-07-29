    public synchronized void connectURL(String url) throws IllegalArgumentException, IOException, MalformedURLException {
        URL myurl = new URL(url);
        InputStream in = myurl.openStream();
        BufferedReader page = new BufferedReader(new InputStreamReader(in));
        String ior = null;
        ArrayList nodesAL = new ArrayList();
        while ((ior = page.readLine()) != null) {
            if (ior.trim().equals("")) continue;
            nodesAL.add(ior);
        }
        in.close();
        Object[] nodesOA = nodesAL.toArray();
        Node[] nodes = new Node[nodesOA.length];
        for (int i = 0; i < nodesOA.length; i++) nodes[i] = TcbnetOrb.getInstance().getNode((String) nodesOA[i]);
        this.connect(nodes);
    }
