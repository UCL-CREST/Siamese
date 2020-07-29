    public void findAction() {
        if (index >= 0) {
            if (searchChanged == true) {
                index = area.getCaretPosition();
                textToSearchFor = textField.getText();
                textToSearchIn = area.getText();
                if (matchCase == false) {
                    if (holeWords == true) {
                        p = Pattern.compile("\\b" + textToSearchFor + "\\b", Pattern.CASE_INSENSITIVE);
                    } else {
                        p = Pattern.compile(textToSearchFor, Pattern.CASE_INSENSITIVE);
                    }
                } else {
                    if (holeWords == true) {
                        p = Pattern.compile("\\b" + textToSearchFor + "\\b");
                    } else {
                        p = Pattern.compile(textToSearchFor);
                    }
                }
                m = p.matcher(textToSearchIn);
                searchChanged = false;
            }
            int start, end;
            while (m.find() == true) {
                start = m.start();
                if (start < index) {
                    continue;
                }
                end = m.end();
                area.setCaretPosition(start);
                area.moveCaretPosition(end);
                formerSelectionDeleted = true;
                return;
            }
            continueAnswer = JOptionPane.showConfirmDialog(this, SearchDialogResources.getString("StartFromBeginMsg"), title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (continueAnswer == JOptionPane.YES_OPTION) {
                index = 0;
                searchChanged = true;
                area.setCaretPosition(index);
                findAction();
            }
        }
    }
