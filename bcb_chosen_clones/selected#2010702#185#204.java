    public static Hashtable getNamingHashtable() {
        Hashtable namingHash = new Hashtable();
        URL url = AceTree.class.getResource("/org/rhwlab/snight/namesHash.txt");
        InputStream istream = null;
        try {
            istream = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(istream));
            String s;
            while (br.ready()) {
                s = br.readLine();
                if (s.length() == 0) continue;
                String[] sa = s.split(",");
                namingHash.put(sa[0], sa[1]);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return namingHash;
    }
