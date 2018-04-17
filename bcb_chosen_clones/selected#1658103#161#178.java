    private void applyGroups() {
        Pattern p = Pattern.compile("\\\\p\\{group:(?:([0-9]+)|null)\\}");
        Matcher m = p.matcher(regex);
        String temp = "";
        int start = 0;
        while (m.find()) {
            String g1 = m.group(1);
            if (g1 != null && !g1.equals("")) {
                int id = getGroupId(regex.substring(0, m.start())) + 1;
                int group = Integer.valueOf(g1);
                groups.getGroup().get(group).setId(id);
            }
            temp += regex.substring(start, m.start());
            start = m.end();
        }
        temp += regex.substring(start, regex.length());
        regex = temp;
    }
