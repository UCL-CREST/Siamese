    public void process(int branch, int level) {
        Pattern p1 = Pattern.compile("<DIV class=\"treeline\">([^<]*)");
        Pattern p = Pattern.compile("<a href=\"javascript:checkBranch\\(([0-9]+), 'true'\\)\">([^<]*)</a>");
        Matcher m, m1;
        URL url;
        HttpURLConnection conn;
        try {
            url = new URL("http://cri-srv-ade.insa-toulouse.fr:8080/ade/standard/gui/tree.jsp?branchId=" + branch + "&expand=false&forceLoad=false&reload=false&scroll=0");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestProperty("Cookie", sessionId);
            BufferedReader i = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            boolean doAdd = false;
            while ((line = i.readLine()) != null) {
                m1 = p1.matcher(line);
                m = p.matcher(line);
                if (m1.find()) {
                    if (m1.group(1).equals(createIdent(level))) {
                        doAdd = true;
                    } else {
                        doAdd = false;
                    }
                }
                if (m.find()) {
                    if (doAdd) {
                        trainee.add(new Node(Integer.parseInt(m.group(1)), m.group(2)));
                        System.out.println(m.group(1) + " - " + m.group(2));
                    }
                }
            }
            url = new URL("http://cri-srv-ade.insa-toulouse.fr:8080/ade/standard/gui/tree.jsp?branchId=" + branch + "&expand=false&forceLoad=false&reload=false&scroll=0");
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
