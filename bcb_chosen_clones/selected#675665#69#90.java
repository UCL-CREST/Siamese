    protected void parse() {
        List fieldList = new ArrayList();
        int lastEnd = 0;
        Pattern pattern = Pattern.compile("\\$\\{(.+?)\\}");
        Matcher matcher = pattern.matcher(template);
        while (matcher.find()) {
            if (matcher.start() > lastEnd) {
                String text = template.substring(lastEnd, matcher.start());
                fieldList.add(new TextField(text));
            }
            fieldList.add(new DollarField(matcher.group(1)));
            lastEnd = matcher.end();
        }
        if (lastEnd < template.length()) {
            String text = template.substring(lastEnd);
            fieldList.add(new TextField(text));
        }
        fields = new TemplateField[fieldList.size()];
        for (int i = 0; i < fields.length; i++) {
            fields[i] = (TemplateField) fieldList.get(i);
        }
    }
