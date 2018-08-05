    private ArrayList<TextObject> parseText(String text) {
        String pattern = new String("\\n{1}[A-Z[\\s]]+\\n{1}");
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(text);
        ArrayList<TextObject> list = new ArrayList<TextObject>();
        ArrayList<String> feature_type = new ArrayList<String>();
        ArrayList<TextBlock> feature_position = new ArrayList<TextBlock>();
        TextBlock feature_length = new TextBlock();
        String feature = new String();
        int start = 0;
        int prev_end = 0;
        if (m.find()) {
            if (m.group().trim().length() > 0) {
                feature = m.group();
                start = m.end();
            }
            while (m.find()) {
                if (m.group().trim().length() > 0) {
                    prev_end = m.start() - 1;
                    feature_length = new TextBlock(start, prev_end);
                    feature_position.add(feature_length);
                    feature_type.add(feature);
                    feature = m.group();
                    start = m.end();
                }
            }
            feature_length = new TextBlock(start, text.length());
            feature_position.add(feature_length);
            feature_type.add(feature);
            for (int i = 0; i <= feature_type.size() - 1; i++) {
                String feature_text = text.substring(feature_position.get(i).getStart(), feature_position.get(i).getEnd());
                String feature_tp = feature_type.get(i).replaceAll(LINE_TERMINATOR, "");
                list.add(new TextObject(feature_tp, feature_text));
            }
        } else list.add(new TextObject("TEXT", text));
        return list;
    }
