    public static String replaceStrToLink(String msg) {
        if (msg != null) {
            String regex = "(https?|ftp|gopher|telnet|whois|news)\\:([\\w|\\:\\!\\#\\$\\%\\=\\&\\-\\^\\`\\\\|\\@\\~\\[\\{\\]\\}\\;\\+\\*\\,\\.\\?\\/]+)";
            Pattern p = Pattern.compile(regex);
            boolean check = true;
            while (check) {
                check = false;
                Matcher m = p.matcher(msg);
                while (m.find()) {
                    if (m.group(0).contains("@")) {
                        String matchString = m.group(0);
                        matchString = matchString.replaceAll("@", "%40");
                        String pre = msg.substring(0, m.start(0));
                        String post = msg.substring(m.end(0), msg.length());
                        msg = pre + matchString + post;
                        check = true;
                    }
                }
            }
            String newMsg = msg.replaceAll("(https?|ftp|gopher|telnet|whois|news)\\:([\\w|\\:\\!\\#\\$\\%\\=\\&\\-\\^\\`\\\\|\\@\\~\\[\\{\\]\\}\\;\\+\\*\\,\\.\\?\\/]+)", "<a href=\"$1\\:$2\" target=\"_blank\">$1\\:$2</a>");
            return newMsg.replaceAll("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+", "<a href='mailto:$0'>$0</a>");
        } else {
            return "";
        }
    }
