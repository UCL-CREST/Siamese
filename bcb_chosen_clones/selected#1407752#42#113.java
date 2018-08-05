    @SuppressWarnings("unchecked")
    private void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        printAccess(request, false);
        try {
            response.setContentType("application/json");
            String position = request.getParameter("pos");
            if (position == null || position.length() == 0) {
                reportJsonError(request, response, "Missing position parameter");
            } else {
                PositionParser pp;
                pp = new PositionParser(position);
                String url = "http://simbad.u-strasbg.fr/simbad/sim-script?submit=submit+script&script=";
                url += URLEncoder.encode("format object \"%IDLIST[%-30*]|-%COO(A)|%COO(D)|%OTYPELIST(S)\"\n" + pp.getPosition() + " radius=1m", "ISO-8859-1");
                System.out.println(url);
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
                            String id = fields[0].split("\\s{2,}")[0].trim();
                            JSONArray darray = new JSONArray();
                            darray.add(id.trim());
                            darray.add(ra + " " + dec);
                            darray.add(type.trim());
                            dataarray.add(darray);
                            datasize++;
                            if (datasize >= 15) {
                                darray = new JSONArray();
                                darray.add("truncated to 15");
                                darray.add("");
                                darray.add("");
                                dataarray.add(darray);
                                datasize++;
                            }
                        }
                    } else if (boeuf.startsWith("::data")) {
                        data_found = true;
                    }
                }
                in.close();
                retour.put("aaData", dataarray);
                retour.put("iTotalRecords", datasize);
                retour.put("iTotalDisplayRecords", datasize);
                JsonUtils.teePrint(response.getOutputStream(), retour.toJSONString());
            }
        } catch (QueryException e) {
            reportJsonError(request, response, e);
        }
    }
