    @Override
    public String getMessage(Locale locale, String key, Object[] args) {
        String msg = super.getMessage(locale, key, args);
        if (Locale.KOREA.equals(locale) || Locale.KOREAN.equals(locale)) {
            StringBuffer sb = new StringBuffer();
            Pattern p = Pattern.compile("\\" + OPEN + "." + OR + "." + "\\" + CLOSE);
            Matcher m = p.matcher(msg);
            int prv = 0;
            while (m.find()) {
                sb.append(msg.substring(prv, m.start()));
                sb.append(getPostWord(msg.charAt(m.start() - 1), m.group()));
                prv = m.end();
            }
            sb.append(msg.substring(prv));
            msg = sb.toString();
        }
        return msg;
    }
