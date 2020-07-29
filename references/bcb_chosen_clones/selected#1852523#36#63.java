    public IOObject[] apply() throws OperatorException {
        TextObject text = getInput(TextObject.class);
        IOContainer input = getInput();
        Pattern startPattern = Pattern.compile(getParameterAsString(PARAMETER_START_REGEX));
        Pattern endPattern = Pattern.compile(getParameterAsString(PARAMETER_END_REGEX));
        Matcher startMatcher = startPattern.matcher(text.getText());
        Matcher endMatcher = endPattern.matcher(text.getText());
        int start = 0;
        while (startMatcher.find(start)) {
            if (endMatcher.find(startMatcher.end())) {
                TextObject segment = new TextObject(text.getText().substring(startMatcher.start(), endMatcher.end()));
                Iterator<Operator> childIterator = super.getOperators();
                IOContainer childInput = input.append(segment);
                while (childIterator.hasNext()) {
                    try {
                        childInput = childIterator.next().apply(childInput);
                    } catch (ConcurrentModificationException e) {
                        if (isDebugMode()) e.printStackTrace();
                        throw new UserError(this, 923);
                    }
                }
                start = endMatcher.end();
            } else {
                break;
            }
        }
        return input.getIOObjects();
    }
