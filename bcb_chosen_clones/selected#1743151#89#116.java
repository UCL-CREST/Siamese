    public static boolean exec_applet(String fname, VarContainer vc, ActionContainer ac, ThingTypeContainer ttc, Output OUT, InputStream IN, boolean AT, Statement state, String[] arggies) {
        if (!urlpath.endsWith("/")) {
            urlpath = urlpath + '/';
        }
        if (!urlpath.startsWith("http://")) {
            urlpath = "http://" + urlpath;
        }
        String url = urlpath;
        if (fname.startsWith("dusty_")) {
            url = url + "libraries/" + fname;
        } else {
            url = url + "users/" + fname;
        }
        StringBuffer src = new StringBuffer(2400);
        try {
            String s;
            BufferedReader br = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
            while ((s = br.readLine()) != null) {
                src.append(s).append('\n');
            }
            br.close();
        } catch (Exception e) {
            OUT.println(new DSOut(DSOut.ERR_OUT, -1, "Dustyscript failed at reading the file'" + fname + "'\n\t...for 'use' statement"), vc, AT);
            return false;
        }
        fork(src, vc, ac, ttc, OUT, IN, AT, state, arggies);
        return true;
    }
