    private int tagFind(StringBuilder sb, HashMap hmap) {
        Pattern pattag = Pattern.compile(":\\w{3}:");
        Matcher m = pattag.matcher(sb);
        int startpos = 0;
        int endpos = 0;
        String tagname = null;
        String tagfield = null;
        String sbcontent = null;
        if (m.find()) {
            startpos = m.start();
            endpos = m.end();
            tagname = m.group();
        }
        while (m.find()) {
            startpos = m.start();
            sbcontent = sb.substring(endpos, startpos);
            setFieldValue(hmap, tagname, sbcontent);
            endpos = m.end();
            tagname = m.group();
        }
        if (endpos == 0) {
            return 0;
        } else if ((startpos = sb.indexOf("}", endpos)) != -1) {
            setFieldValue(hmap, tagname, sb.substring(endpos, startpos));
            return -1;
        } else if ((startpos = sb.indexOf(":", endpos)) != -1) {
            setFieldValue(hmap, tagname, sb.substring(endpos, startpos));
            return -1;
        }
        return endpos;
    }
