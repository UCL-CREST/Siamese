    @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {
        PositionParser pp;
        Database.init("XIDResult");
        pp = new PositionParser("01:33:50.904+30:39:35.79");
        String url = "http://simbad.u-strasbg.fr/simbad/sim-script?submit=submit+script&script=";
        String script = "format object \"%IDLIST[%-30*]|-%COO(A)|%COO(D)|%OTYPELIST(S)\"\n";
        String tmp = "";
        script += pp.getPosition() + " radius=1m";
        url += URLEncoder.encode(script, "ISO-8859-1");
        URL simurl = new URL(url);
        BufferedReader in = new BufferedReader(new InputStreamReader(simurl.openStream()));
        String boeuf;
        boolean data_found = false;
        JSONObject retour = new JSONObject();
        JSONArray dataarray = new JSONArray();
        JSONArray colarray = new JSONArray();
        JSONObject jsloc = new JSONObject();
        jsloc.put("sTitle", "ID");
        colarray.add(jsloc);
        jsloc = new JSONObject();
        jsloc.put("sTitle", "Position");
        colarray.add(jsloc);
        jsloc = new JSONObject();
        jsloc.put("sTitle", "Type");
        colarray.add(jsloc);
        retour.put("aoColumns", colarray);
        int datasize = 0;
        while ((boeuf = in.readLine()) != null) {
            if (data_found) {
                String[] fields = boeuf.trim().split("\\|", -1);
                int pos = fields.length - 1;
                if (pos >= 3) {
                    String type = fields[pos];
                    pos--;
                    String dec = fields[pos];
                    pos--;
                    String ra = fields[pos];
                    String id = "";
                    for (int i = 0; i < pos; i++) {
                        id += fields[i];
                        if (i < (pos - 1)) {
                            id += "|";
                        }
                    }
                    if (id.length() <= 30) {
                        JSONArray darray = new JSONArray();
                        darray.add(id.trim());
                        darray.add(ra + " " + dec);
                        darray.add(type.trim());
                        dataarray.add(darray);
                        datasize++;
                    }
                }
            } else if (boeuf.startsWith("::data")) {
                data_found = true;
            }
        }
        retour.put("aaData", dataarray);
        retour.put("iTotalRecords", datasize);
        retour.put("iTotalDisplayRecords", datasize);
        System.out.println(retour.toJSONString());
        in.close();
    }
