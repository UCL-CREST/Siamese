    private String replaceLabel(final String inStr) {
        final StringBuffer sb = new StringBuffer();
        int start = 0;
        final Pattern pattern = Pattern.compile("[$][{][^}]+[}]");
        final Matcher matcher = pattern.matcher(inStr);
        while (matcher.find()) {
            sb.append(inStr.substring(start, matcher.start()));
            if ("${SP}".equals(matcher.group())) {
                sb.append("<xsl:text> </xsl:text>");
            } else if ("${CR}".equals(matcher.group())) {
                sb.append("<xsl:text>\n</xsl:text>");
            } else if ("${TAB}".equals(matcher.group())) {
                sb.append("<xsl:text>\t</xsl:text>");
            } else if (matcher.group().startsWith("${cType(")) {
                final String balise = "<xsl:value-of select=\"java:fr.ninauve.jnoob.ReflectUtils.getCType(XXXX)\"/>";
                sb.append(balise.replace("XXXX", matcher.group().substring("${cType(".length(), matcher.group().length() - 2)));
            } else if (matcher.group().startsWith("${condensedType(")) {
                final String balise = "<xsl:value-of select=\"java:fr.ninauve.jnoob.ReflectUtils.getJniParam(XXXX)\"/>";
                sb.append(balise.replace("XXXX", matcher.group().substring("${condensedType(".length(), matcher.group().length() - 2)));
            } else if (matcher.group().startsWith("${jniCallMethod(")) {
                final String balise = "<xsl:value-of select=\"java:fr.ninauve.jnoob.ReflectUtils.getJniCallMethod(XXXX)\"/>";
                sb.append(balise.replace("XXXX", matcher.group().substring("${jniCallMethod(".length(), matcher.group().length() - 2)));
            } else if (matcher.group().startsWith("${lookupType(")) {
                final String balise = "<xsl:value-of select=\"java:fr.ninauve.jnoob.ReflectUtils.getLookupType(XXXX)\"/>";
                sb.append(balise.replace("XXXX", matcher.group().substring("${lookupType(".length(), matcher.group().length() - 2)));
            } else {
                final String balise = "<xsl:value-of select=\"XXXX\"/>";
                sb.append(balise.replace("XXXX", matcher.group().substring(2, matcher.group().length() - 1)));
            }
            start = matcher.end();
        }
        if (start <= inStr.length() - 1) sb.append(inStr.substring(start));
        return sb.toString();
    }
