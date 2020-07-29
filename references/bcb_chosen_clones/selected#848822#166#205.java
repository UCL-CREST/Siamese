    public void getWebByUrl(String strUrl, String charset, String fileIndex) {
        try {
            System.out.println("Getting web by url: " + strUrl);
            addReport("Getting web by url: " + strUrl + "\n");
            URL url = new URL(strUrl);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            InputStream is = null;
            is = url.openStream();
            String filePath = fPath + "/web" + fileIndex + ".htm";
            PrintWriter pw = null;
            FileOutputStream fos = new FileOutputStream(filePath);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            pw = new PrintWriter(writer);
            BufferedReader bReader = new BufferedReader(new InputStreamReader(is));
            StringBuffer sb = new StringBuffer();
            String rLine = null;
            String tmp_rLine = null;
            while ((rLine = bReader.readLine()) != null) {
                tmp_rLine = rLine;
                int str_len = tmp_rLine.length();
                if (str_len > 0) {
                    sb.append("\n" + tmp_rLine);
                    pw.println(tmp_rLine);
                    pw.flush();
                    if (deepUrls.get(strUrl) < webDepth) getUrlByString(tmp_rLine, strUrl);
                }
                tmp_rLine = null;
            }
            is.close();
            pw.close();
            System.out.println("Get web successfully! " + strUrl);
            addReport("Get web successfully! " + strUrl + "\n");
            addWebSuccessed();
        } catch (Exception e) {
            System.out.println("Get web failed!       " + strUrl);
            addReport("Get web failed!       " + strUrl + "\n");
            addWebFailed();
        }
    }
