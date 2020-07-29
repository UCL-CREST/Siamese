    public String fetchSpecificText(String regularExpress, String contents) {
        String resultStr = "";
        try {
            Pattern p = Pattern.compile(regularExpress);
            Matcher matcher = p.matcher(contents);
            String startTag = regularExpress.substring(0, regularExpress.indexOf("["));
            String endTag = regularExpress.substring(regularExpress.indexOf("]"), regularExpress.length());
            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();
                String match = contents.substring(start, end);
                resultStr = match.replaceAll(startTag, "").replaceAll(endTag, "");
            }
        } catch (Exception ex) {
            this.logAffectuException(ex, "fetchSpecificText出错:" + contents);
        }
        return resultStr;
    }
