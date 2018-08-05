    public String getHtmlSource(String url) {
        StringBuffer codeBuffer = null;
        BufferedReader in = null;
        URLConnection uc = null;
        try {
            uc = new URL(url).openConnection();
            uc.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows XP; DigExt)");
            in = new BufferedReader(new InputStreamReader(uc.getInputStream(), "utf-8"));
            codeBuffer = new StringBuffer();
            String tempCode = "";
            while ((tempCode = in.readLine()) != null) {
                codeBuffer.append(tempCode).append("\n");
            }
            in.close();
            tempCode = null;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != in) in = null;
            if (null != uc) uc = null;
        }
        return codeBuffer.toString();
    }
