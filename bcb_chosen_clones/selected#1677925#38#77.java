    public ResourcePanel() {
        setLayout(new BorderLayout(0, 0));
        JScrollPane scrollPane = new JScrollPane();
        add(scrollPane, BorderLayout.CENTER);
        table = new JTable();
        table.setModel(new ResourceList());
        scrollPane.setViewportView(table);
        SMTSingleton.getSingletonInstance().getCurrentSearch().getTasks().registerTaskListListener(this);
        JMenuBar menuBar = new JMenuBar();
        add(menuBar, BorderLayout.NORTH);
        JMenu mnNewMenu = new JMenu("Resources");
        menuBar.add(mnNewMenu);
        mnNewMenu.setMnemonic(KeyEvent.VK_R);
        JMenuItem mntmPrintTaskAssignments = new JMenuItem("Print Operational Planning Worksheet (ICS 215)");
        mntmPrintTaskAssignments.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Document document = new Document();
                if ("A4".equals(SMTSingleton.getSingletonInstance().getProperties().getProperties().get(SMTProperties.KEY_PAPERSIZE))) {
                    document.setPageSize(PageSize.A4);
                } else {
                    document.setPageSize(PageSize.LETTER);
                }
                try {
                    String filename = "OperationalPlanningWorksheet_ICS-215.pdf";
                    PdfWriter.getInstance(document, new FileOutputStream(filename));
                    document.open();
                    ResourceList tasks = ((ResourceList) table.getModel());
                    tasks.writeToPdf(document);
                    log.debug("Printing: " + filename);
                    document.close();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (DocumentException ex) {
                    ex.printStackTrace();
                }
            }
        });
        mnNewMenu.add(mntmPrintTaskAssignments);
    }
