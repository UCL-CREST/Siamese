    public void itemStateChanged(ItemEvent evt) {
        if (evt != null && evt.getStateChange() != ItemEvent.SELECTED) {
            return;
        }
        FilePane newPane = null;
        try {
            String newPaneClass = _paneTypes.get(_paneTypeChooser.getSelectedItem()).toString();
            newPane = (FilePane) Class.forName(newPaneClass).getConstructor(new Class[] { _ocmd.getClass() }).newInstance(new Object[] { _ocmd });
        } catch (NullPointerException npe) {
            _ocmd.logMessage(OCmd.LogType.LOG_PANE, "WARNING: selected pane not found");
            _ocmd.logThrowable(npe);
            return;
        } catch (Exception e) {
            _ocmd.logMessage(OCmd.LogType.LOG_PANE, "WARNING: could not load selected pane");
            _ocmd.logThrowable(e);
            return;
        }
        if (_scrollPane != null) {
            remove(_scrollPane);
            _ocmd.removeOCmdListener(_pane);
        }
        _ocmd.addOCmdListener(newPane);
        FilePane oldPane = _pane;
        _pane = newPane;
        _scrollPane = new JScrollPane(_pane, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JComponent columnHeader = _pane.getColumnHeader();
        if (columnHeader != null) {
            _scrollPane.setColumnHeaderView(columnHeader);
        }
        JComponent rowHeader = _pane.getRowHeader();
        if (rowHeader != null) {
            _scrollPane.setRowHeaderView(rowHeader);
        }
        add(_scrollPane, BorderLayout.CENTER);
        _ocmd.fireOCmdEvent(this, OCmdEvent.EventType.ACTIVE_FILEPANE, oldPane, _pane);
    }
