    public void testPattern() {
        String input = FileUtils.readPackageResource("web/sopo/template/Index.html");
        input = input.replaceAll("\\n", "");
        Pattern pattern = Pattern.compile("<head>.*</head>");
        Matcher matcher = pattern.matcher(input);
        int i = 0;
        while (matcher.find()) {
            i++;
            int start = matcher.start();
            int end = matcher.end();
            String match = input.substring(start, end);
        }
        assertTrue(1 == i);
    }
