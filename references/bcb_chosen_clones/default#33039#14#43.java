    public void load(String file) throws IOException {
        String id;
        int i;
        StringTokenizer t;
        String srcText = "";
        String s;
        String nonTrans = "";
        BufferedReader in = new BufferedReader(new FileReader(file));
        while ((s = in.readLine()) != null) {
            t = new StringTokenizer(s, "\t");
            if (t.countTokens() < 2) {
                nonTrans += s;
                continue;
            }
            id = t.nextToken();
            nonTrans += id + "\t";
            srcText = t.nextToken();
            if (m_outFile != null) {
                m_outFile.write(nonTrans);
                nonTrans = "";
            }
            processEntry(srcText, file);
            while (t.hasMoreTokens()) {
                nonTrans += "\t" + t.nextToken();
            }
        }
        if ((m_outFile != null) && (nonTrans.compareTo("") != 0)) {
            m_outFile.write(nonTrans);
        }
    }
