    private String applyMacro(XMLRegexPattern in, HashMap<String, XMLRegexPattern> macros) {
        String input = in.regex;
        int start = 0;
        int j = 0;
        int k = 0;
        int l = 0;
        String temp = "";
        XMLRegexGroup group = null;
        XMLRegexGroup newGroup = null;
        ArrayList<Integer> captGroups = getGroups(input);
        if (in.groups.getGroup().size() > 0) {
            for (int i : captGroups) {
                j++;
                group = null;
                for (Group g : in.groups.getGroup()) {
                    if (g.getId() == j) {
                        group = (XMLRegexGroup) in.groups.getGroup().get(l);
                        break;
                    }
                }
                if (group == null) {
                    continue;
                }
                newGroup = new XMLRegexGroup(0, group.getClassName());
                groups.getGroup().add(newGroup);
                k = groups.getGroup().size() - 1;
                temp += input.substring(start, i) + "\\p{group:" + k + "}";
                start = i;
                l++;
            }
        }
        temp += input.substring(start, input.length());
        if (in.getClassName() != null) {
            newGroup = new XMLRegexGroup(0, in.getClassName());
            groups.getGroup().add(newGroup);
            k = groups.getGroup().size() - 1;
            temp = "\\p{group:" + k + "}(" + temp + ")";
        }
        Pattern p = Pattern.compile("\\\\p\\{pattern:([^}]+)\\}");
        Matcher m = p.matcher(temp);
        String out = "";
        start = 0;
        while (m.find()) {
            XMLRegexPattern macro = macros.get(m.group(1));
            out += temp.substring(start, m.start()) + applyMacro(macro, macros);
            start = m.end();
        }
        out += temp.substring(start, temp.length());
        return out;
    }
