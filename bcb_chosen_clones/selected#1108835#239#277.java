    private static void benchMatcherReplace(int n) {
        Pattern p = Pattern.compile("a");
        Matcher m;
        StopwatchNano sn = new StopwatchNano();
        sn.start();
        for (int i = 0; i < n; i++) {
            m = p.matcher(testText);
            StringBuffer out = new StringBuffer(m.replaceAll("b"));
        }
        sn.stop();
        System.out.println("Replacing all a&#x2019;s with b&#x2019;s took " + sn.getStoppedTimeString());
        sn.reset();
        m = p.matcher(testText);
        sn.start();
        for (int i = 0; i < n; i++) {
            m = p.matcher(testText);
            StringBuffer out = new StringBuffer(m.replaceAll("bbbbbbbbbb"));
        }
        sn.stop();
        System.out.println("Replacing all a&#x2019;s with 10 b&#x2019;s took " + sn.getStoppedTimeString());
        sn.reset();
        m = p.matcher(testText);
        sn.start();
        for (int i = 0; i < n; i++) {
            m = p.matcher(testText);
            StringBuffer out = new StringBuffer();
            int last = 0;
            while (m.find()) {
                out.append(testText.substring(last, m.start()));
                out.append("bbbbbbbbbb");
                last = m.end();
            }
            out.append(testText.substring(last, testText.length()));
            m.replaceAll("bbbbbbbbbb");
        }
        sn.stop();
        System.out.println("Replacing all a&#x2019;s with 10 b&#x2019;s took " + sn.getStoppedTimeString());
        sn.reset();
    }
