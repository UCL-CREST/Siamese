    String readArticleFromFile(String urlStr) {
        String docbase = getDocumentBase().toString();
        int pos = docbase.lastIndexOf('/');
        if (pos > -1) {
            docbase = docbase.substring(0, pos + 1);
        } else {
            docbase = "";
        }
        docbase = docbase + urlStr;
        String prog = "";
        try {
            URL url = new URL(docbase);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            if (in != null) {
                while (true) {
                    try {
                        String mark = in.readLine();
                        if (mark == null) break;
                        prog = prog + mark + "\n";
                    } catch (Exception e) {
                    }
                }
                in.close();
            }
        } catch (Exception e) {
        }
        return prog;
    }
