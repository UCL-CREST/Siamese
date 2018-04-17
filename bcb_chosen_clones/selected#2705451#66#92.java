    public static String urlDecode(String s) throws UnsupportedEncodingException {
        Pattern pattern = Pattern.compile("(%[a-fA-F0-9]{2})+|\\+");
        Matcher m = pattern.matcher(s);
        int start = 0;
        StringBuffer sb = new StringBuffer();
        while (m.find(start)) {
            if (start < m.start()) {
                sb.append(s.substring(start, m.start()));
            }
            if ("+".equals(m.group())) {
                sb.append(' ');
            } else {
                String hex = m.group();
                byte[] bytes = new byte[hex.length() / 3];
                for (int i = 0; i < bytes.length; i++) {
                    int b = Integer.parseInt(hex.substring(i * 3 + 1, i * 3 + 3), 16);
                    bytes[i] = (byte) b;
                }
                sb.append(new String(bytes, "UTF8"));
            }
            start = m.end();
        }
        if (start < s.length()) {
            sb.append(s.substring(start));
        }
        return sb.toString();
    }
