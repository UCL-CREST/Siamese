    private void readFromFile1() throws DException {
        URL url1 = null;
        if (url == null) {
            url = getClass().getResource("/com/daffodilwoods/daffodildb/utils/parser/parser.schema");
            try {
                url = new URL(url.getProtocol() + ":" + url.getPath().substring(0, url.getPath().indexOf("/parser.schema")));
            } catch (MalformedURLException ex2) {
                ex2.printStackTrace();
                throw new DException("DSE0", new Object[] { ex2 });
            }
            try {
                url1 = new URL(url.getProtocol() + ":" + url.getPath() + "/parser.schema");
            } catch (MalformedURLException ex) {
                throw new DException("DSE0", new Object[] { ex });
            }
            if (url1 == null) {
                throw new DException("DSE0", new Object[] { "Parser.schema file is missing in classpath." });
            }
        } else {
            try {
                url1 = new URL(url.getProtocol() + ":" + url.getPath() + "/parser.schema");
            } catch (MalformedURLException ex) {
                throw new DException("DSE0", new Object[] { ex });
            }
        }
        ArrayList arr1 = null;
        StringBuffer rule = null;
        try {
            LineNumberReader raf = new LineNumberReader(new BufferedReader(new InputStreamReader(url1.openStream())));
            arr1 = new ArrayList();
            rule = new StringBuffer("");
            while (true) {
                String str1 = raf.readLine();
                if (str1 == null) {
                    break;
                }
                String str = str1.trim();
                if (str.length() == 0) {
                    if (rule.length() > 0) {
                        arr1.add(rule.toString());
                    }
                    rule = new StringBuffer("");
                } else {
                    rule.append(" ").append(str);
                }
            }
            raf.close();
        } catch (IOException ex1) {
            ex1.printStackTrace();
            throw new DException("DSE0", new Object[] { ex1 });
        }
        if (rule.length() > 0) arr1.add(rule.toString());
        for (int i = 0; i < arr1.size(); i++) {
            String str = (String) arr1.get(i);
            int index = str.indexOf("::=");
            if (index == -1) {
                P.pln("Error " + str);
                throw new DException("DSE0", new Object[] { "Rule is missing from parser.schema" });
            }
            String key = str.substring(0, index).trim();
            String value = str.substring(index + 3).trim();
            Object o = fileEntries.put(key, value);
            if (o != null) {
                new Exception("Duplicate Defination for Rule [" + key + "] Value [" + value + "] Is Replaced By  [" + o + "]").printStackTrace();
            }
        }
    }
