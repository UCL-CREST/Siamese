    private void colorizeEdit(StyledText edit) {
        String findResult = edit.getText();
        final Color COLOR_BLUE = dialog.getDisplay().getSystemColor(SWT.COLOR_BLUE);
        int start = 0;
        int index = findResult.indexOf("\n");
        while (index != -1) {
            String subString = findResult.substring(start, index);
            if (subString.length() != 0 && subString.startsWith("(") && subString.endsWith(")")) {
                StyleRange style = new StyleRange();
                style.start = start;
                style.length = subString.length();
                style.underline = true;
                ((StyledText) editResult).setStyleRange(style);
            } else {
                StyleRange style = new StyleRange();
                style.start = start;
                style.length = subString.length();
                style.foreground = COLOR_BLUE;
                ((StyledText) editResult).setStyleRange(style);
            }
            start = index + 1;
            index = findResult.indexOf("\n", index + 1);
        }
        String subString = findResult.substring(start).replace("\n", "");
        if (subString.length() != 0 && subString.startsWith("(") && subString.endsWith(")")) {
            StyleRange style = new StyleRange();
            style.start = start;
            style.length = subString.length();
            style.underline = true;
            ((StyledText) editResult).setStyleRange(style);
        } else {
            StyleRange style = new StyleRange();
            style.start = start;
            style.length = subString.length();
            style.foreground = COLOR_BLUE;
            ((StyledText) editResult).setStyleRange(style);
        }
        if (!WorkspaceSaveContainer.findCurrent.equals("")) {
            final Color COLOR_RED = dialog.getDisplay().getSystemColor(SWT.COLOR_RED);
            if (WorkspaceSaveContainer.regularExpression) {
                Pattern pattern = null;
                try {
                    if (WorkspaceSaveContainer.caseSensitive) pattern = Pattern.compile(WorkspaceSaveContainer.findCurrent); else pattern = Pattern.compile(WorkspaceSaveContainer.findCurrent, Pattern.CASE_INSENSITIVE);
                } catch (PatternSyntaxException e) {
                    return;
                }
                String[] findResultList = findResult.split("\n");
                int findResultListCount = findResultList.length;
                int findResultListIndex;
                int offset = 0;
                for (findResultListIndex = 0; findResultListIndex < findResultListCount; findResultListIndex++) {
                    Matcher matcher = pattern.matcher(findResultList[findResultListIndex]);
                    while (matcher.find()) {
                        int startIndex = matcher.start();
                        int endIndex = matcher.end();
                        StyleRange style = new StyleRange();
                        style.start = startIndex + offset;
                        style.length = endIndex - startIndex;
                        style.fontStyle = SWT.BOLD;
                        style.foreground = COLOR_RED;
                        ((StyledText) editResult).setStyleRange(style);
                    }
                    offset += findResultList[findResultListIndex].length() + 1;
                }
            } else {
                String findCurrentLowerCase = WorkspaceSaveContainer.findCurrent.toLowerCase();
                String findResultLowerCase = findResult.toLowerCase();
                if (WorkspaceSaveContainer.caseSensitive) index = findResult.indexOf(WorkspaceSaveContainer.findCurrent); else index = findResultLowerCase.indexOf(findCurrentLowerCase);
                while (index != -1) {
                    StyleRange style = new StyleRange();
                    style.start = index;
                    style.length = WorkspaceSaveContainer.findCurrent.length();
                    style.fontStyle = SWT.BOLD;
                    style.foreground = COLOR_RED;
                    ((StyledText) editResult).setStyleRange(style);
                    if (WorkspaceSaveContainer.caseSensitive) index = findResult.indexOf(WorkspaceSaveContainer.findCurrent, index + 1); else index = findResultLowerCase.indexOf(findCurrentLowerCase, index + 1);
                }
            }
        }
    }
