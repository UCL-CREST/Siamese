    private String transformForEach(String data, Map context) {
        Pattern pattern = Pattern.compile("\\#foreach\\(([^\\)]*)\\)(.*)\\#endfor", Pattern.MULTILINE | Pattern.DOTALL);
        Matcher m = pattern.matcher(data);
        while (m.find()) {
            StringBuffer allRows = new StringBuffer();
            String variableAssignment = m.group(1);
            String content = m.group(2);
            String[] vars = variableAssignment.split("\\:");
            String key = vars[0].trim();
            String contextKey = vars[1].trim();
            Collection c;
            try {
                contextKey = Pattern.compile("\\$\\{(.*)\\}").matcher(contextKey).replaceAll("$1");
                Object val = ognl.Ognl.getValue(contextKey, context);
                if (val.getClass().isArray()) {
                    c = Arrays.asList((Object[]) val);
                } else {
                    c = (Collection) val;
                }
            } catch (Exception e) {
                c = Collections.EMPTY_LIST;
            }
            key = key.replace("$", "\\$");
            Pattern p = Pattern.compile(key);
            for (Object row : c) {
                allRows.append(p.matcher(content).replaceAll(row.toString()));
            }
            data = data.substring(0, m.start()) + allRows.toString() + data.substring(m.end(), data.length());
            m = pattern.matcher(data);
        }
        return data;
    }
