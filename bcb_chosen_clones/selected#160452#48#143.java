    public TaskListPanel() {
        setLayout(new BorderLayout(0, 0));
        JScrollPane scrollPane = new JScrollPane();
        table = new JTable();
        table.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseReleased(MouseEvent e) {
            }
        });
        table.setModel(SMTSingleton.getSingletonInstance().getCurrentSearch().getTasks());
        scrollPane.setViewportView(table);
        add(scrollPane, BorderLayout.CENTER);
        JPopupMenu popupMenu = new JPopupMenu();
        addPopup(table, popupMenu);
        JMenuItem mntmPrintTaskAssignment = new JMenuItem("Print Task Assignment Form");
        mntmPrintTaskAssignment.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                String taskid = ((TaskList) table.getModel()).getTaskAt(row).getIdentifier();
                ((TaskList) table.getModel()).getTaskAt(row).writeToPdf(taskid.replace(' ', '_') + "_ICS-204a-OS" + ".pdf");
            }
        });
        JMenuItem mntmEditTask = new JMenuItem("Edit Task");
        mntmEditTask.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                int row = table.getSelectedRow();
                TaskDialog d = new TaskDialog(((TaskList) table.getModel()).getTaskAt(row));
                d.setVisible(true);
            }
        });
        popupMenu.add(mntmEditTask);
        popupMenu.add(mntmPrintTaskAssignment);
        popupMenu.addSeparator();
        JMenuItem mntmPrintTaskAssignments = new JMenuItem("Print All Task Assignment Forms");
        mntmPrintTaskAssignments.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Document document = new Document();
                if ("A4".equals(SMTSingleton.getSingletonInstance().getProperties().getProperties().get(SMTProperties.KEY_PAPERSIZE))) {
                    document.setPageSize(PageSize.A4);
                } else {
                    document.setPageSize(PageSize.LETTER);
                }
                try {
                    String filename = "TaskList_ICS-204_with_Appendixes.pdf";
                    PdfWriter.getInstance(document, new FileOutputStream(filename));
                    document.open();
                    TaskList tasks = ((TaskList) table.getModel());
                    tasks.writeToPdf(document);
                    for (int i = 0; i < tasks.getRowCount(); i++) {
                        tasks.getTaskAt(i).writeToPdf(document);
                    }
                    document.close();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (DocumentException ex) {
                    ex.printStackTrace();
                }
            }
        });
        popupMenu.add(mntmPrintTaskAssignments);
        JMenuItem mntmPrintTaskAssignments2 = new JMenuItem("Print All Task Assignment Forms");
        mntmPrintTaskAssignments2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Document document = new Document();
                if ("A4".equals(SMTSingleton.getSingletonInstance().getProperties().getProperties().get(SMTProperties.KEY_PAPERSIZE))) {
                    document.setPageSize(PageSize.A4);
                } else {
                    document.setPageSize(PageSize.LETTER);
                }
                try {
                    String filename = "TaskList_ICS-204_with_Appendixes.pdf";
                    PdfWriter.getInstance(document, new FileOutputStream(filename));
                    document.open();
                    TaskList tasks = ((TaskList) table.getModel());
                    tasks.writeToPdf(document);
                    for (int i = 0; i < tasks.getRowCount(); i++) {
                        tasks.getTaskAt(i).writeToPdf(document);
                    }
                    document.close();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (DocumentException ex) {
                    ex.printStackTrace();
                }
            }
        });
        JMenuBar menuBar = new JMenuBar();
        add(menuBar, BorderLayout.NORTH);
        menuBar.add(getTaskCreationMenu());
        menuBar.add(mntmPrintTaskAssignments2);
    }
