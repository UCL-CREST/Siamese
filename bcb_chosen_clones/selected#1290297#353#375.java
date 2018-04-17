    public static String[] dealEnclosingTags(String source, String tagName, String features, boolean includeTag) {
        if (source == null || tagName == null) return null;
        List<String> ret = new ArrayList<String>();
        int[] stack1 = new int[100];
        String[] stack2 = new String[100];
        int top = 0;
        Pattern p = Pattern.compile("<[/]?" + tagName.toLowerCase() + "[^>]*>", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(source);
        while (m.find()) {
            String g = m.group();
            if (g.startsWith("</")) {
                if (top == 0) continue;
                int s = stack1[--top];
                String startTag = stack2[top];
                int e = includeTag ? m.end() : m.start();
                if (features == null || startTag.indexOf(features) != -1) ret.add(source.substring(s, e));
            } else {
                stack1[top] = includeTag ? m.start() : m.end();
                stack2[top++] = g;
            }
        }
        return ret.toArray(new String[0]);
    }
