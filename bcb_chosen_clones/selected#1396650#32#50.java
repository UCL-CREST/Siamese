    public void xtest1() throws Exception {
        Pattern pattern1 = Pattern.compile(TextFinder.PATTERN_1);
        Pattern pattern2 = Pattern.compile(TextFinder.PATTERN_2);
        File[] files = this.getFiles();
        for (File file : files) {
            String text = this.readFile(file);
            System.out.println(text);
            Matcher matcher1 = pattern1.matcher(text);
            int index = 0;
            while (matcher1.find()) {
                String group = matcher1.group().trim();
                System.out.println((index++) + " - " + group);
                Matcher matcher2 = pattern2.matcher(group);
                while (matcher2.find()) {
                    System.out.println(matcher2.start() + " - " + matcher2.end());
                }
            }
        }
    }
