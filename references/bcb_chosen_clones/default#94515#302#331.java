    StringBuffer parseConfig(String nagConfig) {
        StringBuffer retval = new StringBuffer();
        try {
            FileReader config = new FileReader(nagConfig);
            BufferedReader br = new BufferedReader(config);
            String line;
            while ((line = br.readLine()) != null) {
                if ((line.startsWith("#")) || (line.trim().equals(""))) {
                    continue;
                }
                StringTokenizer tokens = new StringTokenizer(line, "=");
                while (tokens.hasMoreTokens()) {
                    String tkn = tokens.nextToken().trim();
                    if (tkn.equals("cfg_file")) {
                        retval.append(parseConfig(tokens.nextToken().trim()));
                    }
                    if (tkn.equals("cfg_dir")) {
                        String[] cfgs = getConfigs(tokens.nextToken().trim());
                        for (int i = 0; i < cfgs.length; i++) {
                            retval.append(parseConfig(cfgs[i]));
                        }
                    }
                }
                retval.append(line + "\n");
            }
        } catch (Exception ex) {
            System.out.println("Problem Parsing Config File: " + ex);
        }
        return (retval);
    }
