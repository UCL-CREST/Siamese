    public static Set getSensiFromFile(String filename, Set sensi) throws Exception {
        Set ret = sensi;
        File file = new File(filename);
        if (file.exists()) {
            BufferedReader in = new BufferedReader(new FileReader(filename));
            String inputline;
            while ((inputline = in.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(inputline);
                if (st.countTokens() > 0) {
                    while (st.hasMoreTokens()) {
                        String next = st.nextToken();
                        if (next.startsWith("(sensu")) {
                            String sensu = "";
                            while (sensu.indexOf(")") == -1) sensu = sensu + st.nextToken() + " ";
                            sensu = sensu.substring(0, sensu.length() - 2);
                            ret.add(sensu);
                        }
                    }
                }
            }
            in.close();
        } else System.out.println("Couldn't access file " + filename);
        return ret;
    }
