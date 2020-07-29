    private void find() {
        this.expressions = null;
        Pattern p = Pattern.compile(regExpr);
        Matcher m = p.matcher(text);
        while (m.find()) {
            if (this.stopped) {
                System.out.println("Finder was stopped.");
                this.expressions = null;
                return;
            }
            int start = m.start();
            int end = m.end();
            if (expressions == null) {
                expressions = new FoundExpressions();
            }
            Selection selection = new Selection();
            selection.setStart(start);
            selection.setEnd(end);
            expressions.add(selection);
        }
    }
