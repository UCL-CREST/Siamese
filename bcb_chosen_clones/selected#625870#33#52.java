    public void appendListenerInfo(String a_methodName, Element a_node, String a_src) {
        String l_patternAddListener = "\\." + a_methodName + "My[a-zA-Z0-9-_]+Listener";
        Pattern l_pattern = Pattern.compile(l_patternAddListener);
        Matcher l_matcher = l_pattern.matcher(a_src);
        Element l_node;
        while (l_matcher.find()) {
            l_node = GB_XmlTools.addElement(a_node, a_methodName, null);
            System.out.println("Find OK ");
            int l_start = l_matcher.start();
            int l_end = l_matcher.end();
            System.out.println(l_start + " - " + l_end);
            System.out.println(a_src.substring(l_start, l_end));
            int l_line = GB_SourceTools.lineNumberFromIndex(a_src, l_start);
            System.out.println("line: " + l_line);
            String l_details = GB_SourceTools.lineFromIndex(a_src, l_start);
            System.out.println("line: " + l_details);
            l_node.setAttribute("line", "" + l_line);
            GB_XmlTools.addElement(l_node, "details", l_details);
        }
    }
