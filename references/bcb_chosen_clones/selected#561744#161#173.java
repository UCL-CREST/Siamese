    private void updateColorFromModel(int lastActive, float[] color) {
        String text = jTextArea1.getText();
        Pattern pattern = Pattern.compile(String.format("(RGB\\s+)%s(\\s+)%s(\\s+)%s([\\s/\\}])", numeric, numeric, numeric));
        Matcher m = pattern.matcher(text);
        int count = 0;
        while (m.find()) {
            if (count++ == lastActive) {
                text = text.substring(0, m.start()) + m.group(0).replaceFirst(pattern.pattern(), String.format(Locale.ENGLISH, "$1%.3f$3%.3f$5%.3f$7", color[0], color[1], color[2])).replaceAll("0.000", "0").replaceAll("(\\d\\.\\d)00", "$1") + text.substring(m.end());
                jTextArea1.setText(text);
                return;
            }
        }
    }
