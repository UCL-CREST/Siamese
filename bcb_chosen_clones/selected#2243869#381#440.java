        public void actionPerformed(ActionEvent evt) {
            if (!isAnnotationEditorReady()) {
                return;
            }
            annotationEditor.setPinnedMode(true);
            annotationEditor.setEditingEnabled(false);
            String patternText = searchTextField.getText();
            Pattern pattern;
            try {
                String prefixPattern = searchWholeWordsChk.isSelected() ? "\\b" : "";
                prefixPattern += searchRegExpChk.isSelected() ? "" : "\\Q";
                String suffixPattern = searchRegExpChk.isSelected() ? "" : "\\E";
                suffixPattern += searchWholeWordsChk.isSelected() ? "\\b" : "";
                patternText = prefixPattern + patternText + suffixPattern;
                pattern = searchCaseSensChk.isSelected() ? Pattern.compile(patternText) : Pattern.compile(patternText, Pattern.CASE_INSENSITIVE);
            } catch (PatternSyntaxException e) {
                annotationEditorWindow.setVisible(false);
                JOptionPane.showMessageDialog(annotationEditorWindow, "Invalid regular expression.\n\n" + e.toString().replaceFirst("^.+PatternSyntaxException: ", ""), "GATE", JOptionPane.INFORMATION_MESSAGE);
                annotationEditorWindow.setVisible(true);
                return;
            }
            matcher = pattern.matcher(content);
            boolean found = false;
            int start = -1;
            int end = -1;
            nextMatchStartsFrom = 0;
            while (matcher.find(nextMatchStartsFrom) && !found) {
                start = (matcher.groupCount() > 0) ? matcher.start(1) : matcher.start();
                end = (matcher.groupCount() > 0) ? matcher.end(1) : matcher.end();
                found = false;
                if (searchHighlightsChk.isSelected()) {
                    javax.swing.text.Highlighter.Highlight[] highlights = getOwner().getTextComponent().getHighlighter().getHighlights();
                    for (javax.swing.text.Highlighter.Highlight h : highlights) {
                        if (h.getStartOffset() <= start && h.getEndOffset() >= end) {
                            found = true;
                            break;
                        }
                    }
                } else {
                    found = true;
                }
                nextMatchStartsFrom = end;
            }
            if (found) {
                findNextAction.setEnabled(true);
                annotateMatchAction.setEnabled(true);
                annotateAllMatchesAction.setEnabled(false);
                matchedIndexes = new LinkedList<Vector<Integer>>();
                Vector<Integer> v = new Vector<Integer>(2);
                v.add(start);
                v.add(end);
                matchedIndexes.add(v);
                getOwner().getTextComponent().select(start, end);
                annotationEditor.placeDialog(start, end);
            } else {
                findNextAction.setEnabled(false);
                annotateMatchAction.setEnabled(false);
            }
            findPreviousAction.setEnabled(false);
        }
