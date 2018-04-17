    private void updatePointFromModel(RGBA rgba, int ind) throws ParseException {
        String text = jTextArea1.getText();
        String pts = numeric;
        if (this.colSpacePanel1.getColorSpace().getDimension() == 2) pts = numeric + "\\s*,\\s*" + numeric;
        Pattern pattern = Pattern.compile(String.format("(point\\s*\\(\\s*)%s(\\s*\\)\\s*\\{)", pts));
        Matcher m = pattern.matcher(text);
        int ind0 = 0;
        while (m.find()) {
            if (ind0++ != ind) {
                continue;
            }
            if (colSpacePanel1.getColorSpace().getDimension() == 1) {
                text = String.format("%s%s%s", text.substring(0, m.start()), m.group(0).replaceFirst(pattern.pattern(), String.format(Locale.ENGLISH, "$1%.3f$3", rgba.point.get(0).evaluate())), text.substring(m.end()));
                jTextArea1.setText(text);
                return;
            } else if (colSpacePanel1.getColorSpace().getDimension() == 2) {
                text = String.format("%s%s%s", text.substring(0, m.start()), m.group(0).replaceFirst(pattern.pattern(), String.format(Locale.ENGLISH, "$1%.3f, %.3f$4", rgba.point.get(0).evaluate(), rgba.point.get(1).evaluate())), text.substring(m.end()));
                jTextArea1.setText(text);
                return;
            }
            break;
        }
    }
