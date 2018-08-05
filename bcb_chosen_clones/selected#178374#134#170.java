    public void findNext(String toFind) {
        if (toFind == null || toFind.length() == 0) {
            return;
        }
        try {
            int initial_caret = jTextArea_.getCaretPosition();
            int caret = initial_caret;
            String doc = jTextArea_.getDocument().getText(0, jTextArea_.getDocument().getLength());
            Pattern pattern = Pattern.compile(toFind, Pattern.DOTALL);
            Matcher matcher = pattern.matcher(doc);
            if (matcher.find()) {
                boolean done = false;
                while (!done) {
                    matcher = pattern.matcher(doc);
                    while (matcher.find()) {
                        int start = matcher.start();
                        int end = matcher.end();
                        if (start < caret) {
                            continue;
                        }
                        caret = end;
                        jTextArea_.setCaretPosition(start);
                        jTextArea_.select(start, end);
                        done = true;
                        break;
                    }
                    if (wrapFind) {
                        caret = 0;
                    } else {
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
