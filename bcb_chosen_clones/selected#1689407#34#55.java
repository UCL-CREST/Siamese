    public static void main(String[] args) throws Exception {
        FileOutputStream output = new FileOutputStream("/tmp/output_iostat.txt");
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("/tmp/rwall-desktop_iostat.txt")));
        String line = null;
        String buff = "";
        String start = "avg-cpu:";
        String end = "avg-cpu:";
        Pattern startPattern = Pattern.compile(start + ".*" + end);
        System.out.println("PATTERN : " + startPattern.pattern());
        Matcher startMatcher = null;
        while ((line = br.readLine()) != null) {
            buff = buff + line;
            startMatcher = startPattern.matcher(buff);
            while (startMatcher.find()) {
                String tmp = buff.substring(startMatcher.start(), startMatcher.end());
                Pattern tmpPattern = Pattern.compile(end);
                Matcher tmpMatcher = tmpMatcher = tmpPattern.matcher(tmp);
                ;
                return;
            }
        }
    }
