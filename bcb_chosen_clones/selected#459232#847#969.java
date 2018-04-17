    private boolean remarkTls() throws Exception {
        int nGroups = 0;
        int iGroup = 0;
        String components = null;
        List<Map<String, Object>> tlsGroups = null;
        Map<String, Object> tlsGroup = null;
        List<Map<String, Object>> ranges = null;
        Map<String, Object> range = null;
        String remark = line.substring(0, 11);
        while (readLine() != null && line.startsWith(remark)) {
            try {
                String[] tokens = getTokens(line.substring(10).replace(':', ' '));
                if (tokens.length < 2) continue;
                Logger.info(line);
                if (tokens[1].equalsIgnoreCase("GROUP")) {
                    tlsGroup = new Hashtable<String, Object>();
                    ranges = new ArrayList<Map<String, Object>>();
                    tlsGroup.put("ranges", ranges);
                    tlsGroups.add(tlsGroup);
                    tlsGroupID = parseInt(tokens[tokens.length - 1]);
                    tlsGroup.put("id", Integer.valueOf(tlsGroupID));
                } else if (tokens[0].equalsIgnoreCase("NUMBER")) {
                    if (tokens[2].equalsIgnoreCase("COMPONENTS")) {
                    } else {
                        nGroups = parseInt(tokens[tokens.length - 1]);
                        if (nGroups < 1) break;
                        if (vTlsModels == null) vTlsModels = new ArrayList<Map<String, Object>>();
                        tlsGroups = new ArrayList<Map<String, Object>>();
                        appendLoadNote(line.substring(11).trim());
                    }
                } else if (tokens[0].equalsIgnoreCase("COMPONENTS")) {
                    components = line;
                } else if (tokens[0].equalsIgnoreCase("RESIDUE")) {
                    range = new Hashtable<String, Object>();
                    char chain1, chain2;
                    int res1, res2;
                    if (tokens.length == 6) {
                        chain1 = tokens[2].charAt(0);
                        chain2 = tokens[4].charAt(0);
                        res1 = parseInt(tokens[3]);
                        res2 = parseInt(tokens[5]);
                    } else {
                        int toC = components.indexOf(" C ");
                        int fromC = components.indexOf(" C ", toC + 4);
                        chain1 = line.charAt(fromC);
                        chain2 = line.charAt(toC);
                        res1 = parseInt(line.substring(fromC + 1, toC));
                        res2 = parseInt(line.substring(toC + 1));
                    }
                    if (chain1 == chain2) {
                        range.put("chains", "" + chain1 + chain2);
                        if (res1 <= res2) {
                            range.put("residues", new int[] { res1, res2 });
                            ranges.add(range);
                        } else {
                            tlsAddError(" TLS group residues are not in order (range ignored)");
                        }
                    } else {
                        tlsAddError(" TLS group chains are different (range ignored)");
                    }
                } else if (tokens[0].equalsIgnoreCase("SELECTION")) {
                    char chain = '\0';
                    for (int i = 1; i < tokens.length; i++) {
                        if (tokens[i].toUpperCase().indexOf("CHAIN") >= 0) {
                            chain = tokens[++i].charAt(0);
                            continue;
                        }
                        int resno = parseInt(tokens[i]);
                        if (resno == Integer.MIN_VALUE) continue;
                        range = new Hashtable<String, Object>();
                        range.put("residues", new int[] { resno, parseInt(tokens[++i]) });
                        if (chain != '\0') range.put("chains", "" + chain + chain);
                        ranges.add(range);
                    }
                } else if (tokens[0].equalsIgnoreCase("ORIGIN")) {
                    Point3f origin = new Point3f();
                    tlsGroup.put("origin", origin);
                    if (tokens.length == 8) {
                        origin.set(parseFloat(tokens[5]), parseFloat(tokens[6]), parseFloat(tokens[7]));
                    } else {
                        int n = line.length();
                        origin.set(parseFloat(line.substring(n - 27, n - 18)), parseFloat(line.substring(n - 18, n - 9)), parseFloat(line.substring(n - 9, n)));
                    }
                    if (Float.isNaN(origin.x) || Float.isNaN(origin.y) || Float.isNaN(origin.z)) {
                        origin.set(Float.NaN, Float.NaN, Float.NaN);
                        tlsAddError("invalid origin: " + line);
                    }
                } else if (tokens[1].equalsIgnoreCase("TENSOR")) {
                    char tensorType = tokens[0].charAt(0);
                    String s = (readLine().substring(10) + readLine().substring(10) + readLine().substring(10)).replace(tensorType, ' ').replace(':', ' ');
                    tokens = getTokens(s);
                    float[][] tensor = new float[3][3];
                    tlsGroup.put("t" + tensorType, tensor);
                    for (int i = 0; i < tokens.length; i++) {
                        int ti = tokens[i].charAt(0) - '1';
                        int tj = tokens[i].charAt(1) - '1';
                        tensor[ti][tj] = parseFloat(tokens[++i]);
                        if (ti < tj) tensor[tj][ti] = tensor[ti][tj];
                    }
                    for (int i = 0; i < 3; i++) for (int j = 0; j < 3; j++) if (Float.isNaN(tensor[i][j])) {
                        tlsAddError("invalid tensor: " + Escape.escapeArray(tensor));
                    }
                    if (tensorType == 'S' && ++iGroup == nGroups) {
                        Logger.info(nGroups + " TLS groups read");
                        readLine();
                        break;
                    }
                }
            } catch (Exception e) {
                Logger.error(line + "\nError in TLS parser: ");
                e.printStackTrace();
                tlsGroups = null;
                break;
            }
        }
        if (tlsGroups != null) {
            Hashtable<String, Object> groups = new Hashtable<String, Object>();
            groups.put("groupCount", Integer.valueOf(nGroups));
            groups.put("groups", tlsGroups);
            vTlsModels.add(groups);
        }
        return (nGroups < 1);
    }
