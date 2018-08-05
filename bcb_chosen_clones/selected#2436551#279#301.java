    public String convertToValidNewItemName(String refName) {
        StringBuffer sb = new StringBuffer(refName);
        Pattern p = Pattern.compile("^[^a-z_A-Z]$");
        if (p.matcher(refName).matches()) {
            sb.setCharAt(0, '_');
        }
        p = Pattern.compile("[^a-z_A-Z0-9]");
        Matcher m = p.matcher(sb);
        while (m.find()) {
            sb.setCharAt(m.start(), '_');
        }
        p = Pattern.compile(".*_(\\d+)$");
        while (getItemByName(sb.toString()) != null) {
            m = p.matcher(sb.toString());
            if (m.matches()) {
                int i = Integer.valueOf(m.group(1));
                sb.replace(m.start(1), m.end(1), String.valueOf(i + 1));
            } else {
                sb.append("_1");
            }
        }
        return sb.toString();
    }
