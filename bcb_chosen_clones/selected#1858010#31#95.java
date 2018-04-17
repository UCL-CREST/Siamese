    private void readTlsData() throws Exception {
        vTlsModels = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> tlsGroups;
        Map<String, Object> tlsGroup = null;
        List<Map<String, Object>> ranges = null;
        Map<String, Object> range = null;
        tlsGroups = new ArrayList<Map<String, Object>>();
        while (readLine() != null) {
            String[] tokens = getTokens(line.replace('\'', ' '));
            if (tokens.length == 0) continue;
            if (tokens[0].equals("TLS")) {
                tlsGroup = new Hashtable<String, Object>();
                ranges = new ArrayList<Map<String, Object>>();
                tlsGroup.put("ranges", ranges);
                tlsGroups.add(tlsGroup);
                tlsGroup.put("id", Integer.valueOf(++tlsGroupID));
            } else if (tokens[0].equals("RANGE")) {
                range = new Hashtable<String, Object>();
                char chain1 = tokens[1].charAt(0);
                char chain2 = tokens[3].charAt(0);
                int res1 = Parser.parseInt(tokens[2]);
                int res2 = Parser.parseInt(tokens[4]);
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
            } else if (tokens[0].equals("ORIGIN")) {
                Point3f origin = new Point3f();
                tlsGroup.put("origin", origin);
                origin.set(parseFloat(tokens[1]), parseFloat(tokens[2]), parseFloat(tokens[3]));
                if (Float.isNaN(origin.x) || Float.isNaN(origin.y) || Float.isNaN(origin.z)) {
                    origin.set(Float.NaN, Float.NaN, Float.NaN);
                    tlsAddError("invalid origin: " + line);
                }
            } else if (tokens[0].equals("T") || tokens[0].equals("L") || tokens[0].equals("S")) {
                char tensorType = tokens[0].charAt(0);
                String[] nn = (tensorType == 'S' ? Snn : TLnn);
                float[][] tensor = new float[3][3];
                tlsGroup.put("t" + tensorType, tensor);
                for (int i = 1; i < tokens.length; i++) {
                    int ti = nn[i].charAt(0) - '1';
                    int tj = nn[i].charAt(1) - '1';
                    tensor[ti][tj] = parseFloat(tokens[++i]);
                    if (ti < tj) tensor[tj][ti] = tensor[ti][tj];
                }
                if (tensorType == 'S') tensor[0][0] = -tensor[0][0];
                for (int i = 0; i < 3; i++) for (int j = 0; j < 3; j++) if (Float.isNaN(tensor[i][j])) {
                    tlsAddError("invalid tensor: " + Escape.escapeArray(tensor));
                }
            }
        }
        Logger.info(tlsGroupID + " TLS groups read");
        Hashtable<String, Object> groups = new Hashtable<String, Object>();
        groups.put("groupCount", Integer.valueOf(tlsGroupID));
        groups.put("groups", tlsGroups);
        vTlsModels.add(groups);
        htParams.put("vTlsModels", vTlsModels);
    }
