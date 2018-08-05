        public void valueChanged(ListSelectionEvent evt) {
            if (evt.getValueIsAdjusting()) return;
            final ErrorSource.Error error = (ErrorSource.Error) errorList.getSelectedValue();
            final Buffer buffer;
            if (error.getBuffer() != null) buffer = error.getBuffer(); else {
                buffer = jEdit.openFile(view, null, error.getFilePath(), false, false, false);
            }
            view.toFront();
            view.requestFocus();
            Runnable r = new Runnable() {

                public void run() {
                    view.setBuffer(buffer);
                    int start = error.getStartOffset();
                    int end = error.getEndOffset();
                    int lineNo = error.getLineNumber();
                    Element line = buffer.getDefaultRootElement().getElement(lineNo);
                    if (line != null) {
                        start += line.getStartOffset();
                        if (end == 0) end = line.getEndOffset() - 1; else end += line.getStartOffset();
                    }
                    view.getTextArea().select(start, end);
                }
            };
            try {
                Class.forName("org.gjt.sp.jedit.io.VFSManager").getMethod("runInAWTThread", new Class[] { Runnable.class }).invoke(null, new Object[] { r });
            } catch (Exception e) {
                r.run();
            }
        }
