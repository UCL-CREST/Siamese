    public FoundExpressions find() {
        this.expressions = null;
        Pattern p = Pattern.compile(regExpr);
        Iterator it = this.searchableComponents.iterator();
        while (it.hasNext()) {
            ISearchable searchable = (ISearchable) it.next();
            if (searchable.getTextComponent() == null) {
                continue;
            }
            String text = searchable.getText();
            Matcher m = p.matcher(text);
            while (m.find()) {
                String group = m.group();
                int start = m.start();
                int end = m.end();
                if (this.expressions == null) {
                    this.expressions = new FoundExpressions();
                }
                Selection selection = new Selection(searchable);
                selection.setStart(start);
                selection.setEnd(end);
                this.expressions.add(selection);
                this.logger.finer("Add selection with start '" + start + "' and end '" + end + "' for seachable '" + searchable.toString() + "'.");
            }
        }
        return this.expressions;
    }
