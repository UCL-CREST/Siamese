    @Test
    public void testgetSimpleName() throws Exception {
        String qname = "java.util.Currency";
        String regex = String.format("(%s)([a-zA-Z_0-9]*)", Pattern.quote("."), Pattern.quote("."));
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(qname);
        System.out.println(String.format("groupCount:%d", matcher.groupCount()));
        while (matcher.find()) {
            System.out.println(String.format("%s start:%d, end:%d", matcher.group(), matcher.start(), matcher.end()));
            for (int i = 0; i <= matcher.groupCount(); i++) {
                System.out.println(String.format("group(%d):%s", i, matcher.group(i)));
            }
            String result = matcher.group(2);
            System.out.println(String.format("result:%s", result));
        }
    }
