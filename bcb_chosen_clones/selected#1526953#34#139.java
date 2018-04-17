    public String displayLabel(boolean bTimexTag) {
        String sRez;
        if (bTimexTag) {
            sRez = "<TIMEX2";
        } else {
            sRez = "";
        }
        if (htblVariables.containsKey("function")) {
            String sFunctionPattern = "^(DOW|M|Y|D|H)=([^,]*)(,.*$|$)";
            Pattern pattern = Pattern.compile(sFunctionPattern);
            Matcher m = pattern.matcher(htblVariables.get("function").toString());
            String[] arrsMatch = new String[3];
            arrsMatch[0] = "";
            arrsMatch[1] = "";
            arrsMatch[2] = "";
            while (m.find()) {
                if (m.start(1) < m.end(1)) {
                    arrsMatch[0] = m.group(1);
                } else {
                    break;
                }
                if (m.start(2) < m.end(2)) {
                    arrsMatch[1] = m.group(2);
                } else {
                    break;
                }
                if (m.start(3) < m.end(3)) {
                    arrsMatch[2] = m.group(3);
                } else {
                }
                if (arrsMatch[0].compareTo("DOW") == 0) {
                    this.set("dayOfWeek", arrsMatch[1]);
                } else if (arrsMatch[0].compareTo("M") == 0) {
                    this.set("month", arrsMatch[1]);
                } else if (arrsMatch[0].compareTo("Y") == 0) {
                    this.set("year", arrsMatch[1]);
                } else if (arrsMatch[0].compareTo("D") == 0) {
                    this.set("dayOfMonth", arrsMatch[1]);
                } else {
                    this.set("hour", arrsMatch[1]);
                }
                String sPattern3 = "^,([^,]*$)";
                Pattern pattern3 = Pattern.compile(sPattern3);
                Matcher m3 = pattern3.matcher(arrsMatch[2]);
                if (m3.matches()) {
                    this.set("function", arrsMatch[0]);
                } else {
                    this.set("function", "");
                }
            }
        }
        String sPatternKey = "dayOfMonth|week|month|hour|minute|second|miliSec";
        Pattern patternKey = Pattern.compile(sPatternKey);
        Vector<String> v = new Vector<String>(htblVariables.keySet());
        Collections.sort(v);
        for (Enumeration<String> e = v.elements(); e.hasMoreElements(); ) {
            String sKey = e.nextElement();
            Matcher mKey = patternKey.matcher(sKey);
            if (mKey.matches()) {
                Pattern pattern2 = Pattern.compile("^(\\d)$");
                String sTemp = htblVariables.get(sKey);
                Matcher m2 = pattern2.matcher(sTemp);
                if (m2.matches()) {
                    sTemp = "0" + sTemp;
                    htblVariables.put(sKey, sTemp);
                }
            }
            if ((htblVariables.get(sKey).length() > 0) && (sKey.compareTo("text") != 0) && (sKey.compareTo("toFind") != 0)) {
                if ((sKey.compareTo("val") != 0)) {
                    sRez += " " + sKey + "=\"" + htblVariables.get(sKey) + "\"";
                } else {
                    String sNewVal = htblVariables.get(sKey).replaceAll("-9", "X");
                    sRez += " " + sKey + "=\"" + sNewVal + "\"";
                }
            }
        }
        if (htblVariables.containsKey("timeZone")) {
            Pattern patternTimeZone = Pattern.compile("^((\\+|-)(\\d\\d))(:(\\d\\d))?$");
            String sTemp = htblVariables.get("timeZone");
            Matcher m2 = patternTimeZone.matcher(sTemp);
            if (m2.matches()) {
                String sHourWithSign = m2.group(1);
                String sMin = m2.group(5);
                if (toFind_ResultList == null) {
                    toFind_ResultList = new ArrayList<String>();
                }
                toFind_ResultList.add("H");
                toFind_ResultList.add(sHourWithSign);
                if (sMin != null) {
                    toFind_ResultList.add("MIN");
                    toFind_ResultList.add(sMin);
                }
            }
        }
        if (toFind_ResultList != null) {
            sRez += " toFind=\"";
            for (int i = 0; i < toFind_ResultList.size(); i++) {
                sRez += toFind_ResultList.get(i) + " ";
            }
            sRez += "\"";
        }
        if (bTimexTag) {
            sRez += ">";
        }
        return sRez;
    }
