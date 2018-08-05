    public void actionPerformed(ActionEvent e) {
        String source = "";
        if (e.getSource() instanceof JMenuItem) source = ((JMenuItem) e.getSource()).getText();
        if (e.getSource() instanceof JButton) source = ((JButton) e.getSource()).getActionCommand();
        if (source.equals("Show Console")) ConsoleFrame.showFrame(); else if (source.equals("Save")) {
            try {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("ATD Save File");
                chooser.setCurrentDirectory(new File("."));
                chooser.setFileFilter(new AtdFilter());
                int returnVal = chooser.showSaveDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    f = chooser.getSelectedFile();
                    if (f.getName().toLowerCase().endsWith(".atd") == false) {
                        File renameF = new File(f.getAbsolutePath() + ".atd");
                        f = renameF;
                    }
                } else return;
                if (f.exists()) {
                    int response = JOptionPane.showConfirmDialog(null, "Overwrite existing file?", "Confirm Overwrite", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.CANCEL_OPTION) return;
                }
                FileOutputStream fileOut = new FileOutputStream(f);
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                try {
                    if (_classID == 1) {
                        out.writeInt(_classID);
                        out.writeObject((Hashtable) ((ReportViewer) _saveO)._map);
                        out.writeObject(((ReportViewer) _saveO).time_l.getText());
                        out.writeObject(((ReportViewer) _saveO).r_c.getText());
                        out.writeObject((ReportTable) ((ProfilePane) ((ReportViewer) _saveO).pp)._table);
                        out.writeObject((PlotterPanel) ((ReportViewer) _saveO).plotterPanel);
                        out.writeObject((PlotterPanel) ((ReportViewer) _saveO).spp.g_p);
                        out.writeObject("Version 4.0");
                    } else if (_classID == 2) {
                        out.writeInt(_classID);
                        out.writeObject(((StringProfiler) _saveO).qp_1.getStatus());
                        out.writeObject(((StringProfiler) _saveO).qp_2.getStatus());
                        out.writeObject(((StringProfiler) _saveO).qp_3.getStatus());
                        out.writeObject(((StringProfiler) _saveO).q1_c.getText());
                        out.writeObject(((StringProfiler) _saveO).q2_c.getText());
                        out.writeObject(((StringProfiler) _saveO).q3_c.getText());
                        out.writeObject((Hashtable) ((StringProfiler) _saveO)._h_info);
                        out.writeObject(((StringProfiler) _saveO).r_t.getText());
                        out.writeObject(((StringProfiler) _saveO).r_c.getText());
                        out.writeObject((ReportTable) ((StringProfiler) _saveO).qtable);
                        out.writeObject("Version 4.0");
                    } else {
                        out.writeInt(_classID);
                        out.writeObject((Hashtable) ((AnalysisListener) _saveO).map);
                        out.writeObject((JPanel) ((AnalysisListener) _saveO).top);
                        out.writeObject((ReportTable) ((AnalysisListener) _saveO)._table_1);
                        out.writeObject((ReportTable) ((AnalysisListener) _saveO)._table_2);
                        out.writeObject((ReportTable) ((AnalysisListener) _saveO)._table_3);
                        out.writeObject("Version 4.0");
                    }
                } catch (NotSerializableException ser_exp) {
                    ConsoleFrame.addText("\n Serialization ERROR:" + ser_exp.getMessage());
                    JOptionPane.showMessageDialog(null, ser_exp.getMessage(), "Error Message", JOptionPane.ERROR_MESSAGE);
                }
                out.close();
                fileOut.close();
            } catch (FileNotFoundException file_exp) {
                JOptionPane.showMessageDialog(null, file_exp.getMessage(), "Error Message", JOptionPane.ERROR_MESSAGE);
            } catch (IOException exp) {
                JOptionPane.showMessageDialog(null, exp.getMessage(), "Error Message", JOptionPane.ERROR_MESSAGE);
                exp.printStackTrace();
            }
        } else {
            try {
                JFileChooser chooser = new JFileChooser();
                chooser.setDialogTitle("ATD Open File");
                chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                chooser.setCurrentDirectory(new File("."));
                chooser.setFileFilter(new AtdFilter());
                int returnVal = chooser.showOpenDialog(this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    f = chooser.getSelectedFile();
                } else return;
                FileInputStream fileIn = new FileInputStream(f);
                ObjectInputStream in = new ObjectInputStream(fileIn);
                int i = in.readInt();
                if (i == 1) {
                    String r_c = null, r_t = null;
                    try {
                        _hashInfo = (Hashtable) in.readObject();
                        r_t = (String) in.readObject();
                        r_c = (String) in.readObject();
                        pr = (ReportTable) in.readObject();
                        sp = (PlotterPanel) in.readObject();
                        sp_dc = (PlotterPanel) in.readObject();
                    } catch (WriteAbortedException wrt_exp) {
                    }
                    in.close();
                    fileIn.close();
                    JFrame frame = new JFrame("Number report") {

                        public void paint(Graphics g) {
                            super.paint(g);
                            if (sp.getInit()) sp.drawBarChart();
                            if (sp_dc.getBInit()) {
                                minS.setValue(minS.getMinimum());
                                maxS.setValue(maxS.getMaximum());
                                sp_dc.showBubbleChart();
                            }
                        }
                    };
                    JPanel t_pane = new JPanel();
                    t_pane.setLayout(new GridLayout(3, 0));
                    t_pane.setPreferredSize(new Dimension(900, 900));
                    JPanel pane = new JPanel();
                    SpringLayout layout = new SpringLayout();
                    pane.setLayout(layout);
                    pane.setOpaque(true);
                    dsn = new JLabel("DSN: " + _hashInfo.get("Schema"));
                    table = new JLabel("Table: " + _hashInfo.get("Table"));
                    col = new JLabel("Column: " + _hashInfo.get("Column"));
                    type = new JLabel("Type: " + _hashInfo.get("Type"));
                    cond = new JLabel("Condition: " + _hashInfo.get("Condition"));
                    JLabel p_t = new JLabel(r_t);
                    String r_c_e = r_c.replaceAll("\n", "<br>");
                    r_c_e = "<html><I>" + r_c_e + "</I></html>";
                    JLabel p_c_l = new JLabel("Report Comment: ");
                    JLabel p_c = new JLabel(r_c_e);
                    p_c.setForeground(Color.BLUE);
                    p_c.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    pr.setPreferredSize(new Dimension(700, 180));
                    layout.putConstraint(SpringLayout.WEST, dsn, 5, SpringLayout.WEST, pane);
                    layout.putConstraint(SpringLayout.WEST, table, 10, SpringLayout.EAST, dsn);
                    layout.putConstraint(SpringLayout.WEST, col, 10, SpringLayout.EAST, table);
                    layout.putConstraint(SpringLayout.WEST, type, 10, SpringLayout.EAST, col);
                    layout.putConstraint(SpringLayout.WEST, cond, 10, SpringLayout.EAST, type);
                    layout.putConstraint(SpringLayout.WEST, p_t, 5, SpringLayout.WEST, pane);
                    layout.putConstraint(SpringLayout.NORTH, p_t, 30, SpringLayout.NORTH, pane);
                    layout.putConstraint(SpringLayout.WEST, p_c_l, 10, SpringLayout.EAST, p_t);
                    layout.putConstraint(SpringLayout.NORTH, p_c_l, 30, SpringLayout.NORTH, pane);
                    layout.putConstraint(SpringLayout.WEST, p_c, 10, SpringLayout.EAST, p_c_l);
                    layout.putConstraint(SpringLayout.NORTH, p_c, 30, SpringLayout.NORTH, pane);
                    layout.putConstraint(SpringLayout.NORTH, pr, 5, SpringLayout.SOUTH, p_c);
                    layout.putConstraint(SpringLayout.WEST, pr, 10, SpringLayout.WEST, pane);
                    pane.add(dsn);
                    pane.add(table);
                    pane.add(col);
                    pane.add(type);
                    pane.add(cond);
                    pane.add(p_t);
                    pane.add(p_c_l);
                    pane.add(p_c);
                    pane.add(pr);
                    t_pane.add(pane);
                    JPanel p_pane = new JPanel();
                    p_pane.setLayout(new BorderLayout());
                    EmptyBorder emptyBorder = new EmptyBorder(10, 10, 10, 10);
                    BevelBorder bevelBorder = new BevelBorder(BevelBorder.LOWERED);
                    CompoundBorder compoundBorder = new CompoundBorder(emptyBorder, bevelBorder);
                    p_pane.setBorder(new CompoundBorder(compoundBorder, emptyBorder));
                    if (sp.getInit()) {
                        JPanel b_p = new JPanel();
                        JButton z_in = new JButton("Zoom In");
                        JButton z_out = new JButton("Zoom Out");
                        JButton r_set = new JButton("Reset");
                        z_in.addMouseListener(this);
                        z_out.addMouseListener(this);
                        r_set.addMouseListener(this);
                        b_p.add(z_in);
                        b_p.add(z_out);
                        b_p.add(r_set);
                        p_pane.add(b_p, BorderLayout.PAGE_END);
                        JPanel l_p = new JPanel();
                        l_p.setLayout(new GridLayout(12, 2));
                        double[] i_i = sp.getBoundry();
                        String[] l = sp.getXLabel();
                        for (int j = 0; j < l.length; j++) {
                            JLabel n_l1 = new JLabel();
                            JLabel n_l2 = new JLabel();
                            n_l1.setText(Double.toString(i_i[j]));
                            n_l2.setText(l[j]);
                            l_p.add(n_l1);
                            l_p.add(n_l2);
                        }
                        JLabel n_11 = new JLabel(Double.toString(i_i[10]));
                        l_p.add(n_11);
                        p_pane.add(l_p, BorderLayout.LINE_START);
                        p_pane.add(sp, BorderLayout.CENTER);
                    } else {
                        JLabel nb_l = new JLabel("<html><b><i>No Bin Analysis to show</i></b></html>", JLabel.CENTER);
                        nb_l.setForeground(Color.RED);
                        p_pane.add(nb_l);
                    }
                    t_pane.add(p_pane);
                    JPanel b_pane = new JPanel();
                    b_pane.setLayout(new BorderLayout());
                    if (sp_dc.getBInit()) {
                        JPanel tp_p = new JPanel();
                        JLabel gr_l = new JLabel("Data Grouped in: " + sp_dc.getGC());
                        JButton z_in = new JButton("Bubble In");
                        JButton z_out = new JButton("Bubble Out");
                        JButton r_set = new JButton("Bubble Reset");
                        z_in.addMouseListener(this);
                        z_out.addMouseListener(this);
                        r_set.addMouseListener(this);
                        tp_p.add(gr_l);
                        tp_p.add(z_in);
                        tp_p.add(z_out);
                        tp_p.add(r_set);
                        b_pane.add(tp_p, BorderLayout.PAGE_START);
                        JPanel s_pane = new JPanel();
                        s_pane.setLayout(new GridLayout(0, 2));
                        s_pane.setPreferredSize(new Dimension(150, 200));
                        vc = sp_dc.vc;
                        int size = vc.size();
                        minS = new JSlider(JSlider.VERTICAL, ((Double) vc.elementAt(0)).intValue() - 1, ((Double) vc.elementAt(size - 1)).intValue() + 1, ((Double) vc.elementAt(0)).intValue() - 1);
                        maxS = new JSlider(JSlider.VERTICAL, ((Double) vc.elementAt(0)).intValue() - 1, ((Double) vc.elementAt(size - 1)).intValue() + 1, ((Double) vc.elementAt(size - 1)).intValue() + 1);
                        if (minS.getMaximum() - minS.getMinimum() < 10) {
                            Hashtable l_t = minS.createStandardLabels(1);
                            minS.setLabelTable(l_t);
                            maxS.setLabelTable(l_t);
                        } else {
                            Hashtable l_t = minS.createStandardLabels((minS.getMaximum() - minS.getMinimum()) / 10);
                            minS.setLabelTable(l_t);
                            maxS.setLabelTable(l_t);
                        }
                        minS.setPaintLabels(true);
                        maxS.setPaintLabels(true);
                        minS.addChangeListener(this);
                        maxS.addChangeListener(this);
                        s_pane.add(minS);
                        s_pane.add(maxS);
                        b_pane.add(s_pane, BorderLayout.LINE_START);
                        b_pane.add(sp_dc, BorderLayout.CENTER);
                    } else {
                        JLabel nb_l = new JLabel("<html><b><i>No Group Analysis to show</i></b></html>", JLabel.CENTER);
                        nb_l.setForeground(Color.RED);
                        b_pane.add(nb_l);
                    }
                    t_pane.add(b_pane);
                    JScrollPane rs_pane = new JScrollPane(t_pane);
                    rs_pane.setPreferredSize(new Dimension(800, 700));
                    AdjustmentListener listener = new MyAdjustmentListener();
                    rs_pane.getHorizontalScrollBar().addAdjustmentListener(listener);
                    rs_pane.getVerticalScrollBar().addAdjustmentListener(listener);
                    frame.setContentPane(rs_pane);
                    frame.setLocation(75, 5);
                    frame.pack();
                    frame.setVisible(true);
                    ToolTipManager.sharedInstance().registerComponent(sp);
                    sp.drawBarChart();
                    sp_dc.showBubbleChart();
                } else if (i == 2) {
                    String pattern1 = null, pattern2 = null, pattern3 = null;
                    String c1 = null, c2 = null, c3 = null;
                    String r_c = null, r_t = null;
                    try {
                        pattern1 = (String) in.readObject();
                        pattern2 = (String) in.readObject();
                        pattern3 = (String) in.readObject();
                        c1 = (String) in.readObject();
                        c2 = (String) in.readObject();
                        c3 = (String) in.readObject();
                        _hashInfo = (Hashtable) in.readObject();
                        r_t = (String) in.readObject();
                        r_c = (String) in.readObject();
                        q_table = (ReportTable) in.readObject();
                    } catch (WriteAbortedException wrt_s_exp) {
                    }
                    in.close();
                    fileIn.close();
                    JFrame frame = new JFrame("String report");
                    JPanel pane = new JPanel();
                    SpringLayout layout = new SpringLayout();
                    pane.setLayout(layout);
                    pane.setOpaque(true);
                    pane.setPreferredSize(new Dimension(900, 700));
                    dsn = new JLabel("DSN: " + _hashInfo.get("Schema"));
                    table = new JLabel("Table: " + _hashInfo.get("Table"));
                    col = new JLabel("Column: " + _hashInfo.get("Column"));
                    type = new JLabel("Type: " + _hashInfo.get("Type"));
                    cond = new JLabel("Condition: " + _hashInfo.get("Condition"));
                    JLabel p1 = new JLabel("Pattern 1: " + pattern1);
                    JLabel p2 = new JLabel("Pattern 2: " + pattern2);
                    JLabel p3 = new JLabel("Pattern 3: " + pattern3);
                    JLabel p_t = new JLabel(r_t);
                    String r_c_e = r_c.replaceAll("\n", "<br>");
                    if (r_c_e.compareTo("") == 0 || r_c_e == null) r_c_e = "NO Comment";
                    r_c_e = "<html><I>" + r_c_e + "</I></html>";
                    JLabel p_c_l = new JLabel("Report Comment: ");
                    JLabel p_c = new JLabel(r_c_e);
                    p_c.setForeground(Color.BLUE);
                    JLabel cc1 = new JLabel(c1);
                    JLabel cc2 = new JLabel(c2);
                    JLabel cc3 = new JLabel(c3);
                    cc1.setToolTipText("<html>Count of Pattern_1 <BR>N/P: NOT PROFILED</html>");
                    cc2.setToolTipText("<html>Count of Pattern_2 <BR>N/P: NOT PROFILED</html>");
                    cc3.setToolTipText("<html>Count of Pattern_3 <BR>N/P: NOT PROFILED</html>");
                    cc1.setForeground(Color.RED);
                    cc2.setForeground(Color.RED);
                    cc3.setForeground(Color.RED);
                    layout.putConstraint(SpringLayout.WEST, dsn, 5, SpringLayout.WEST, pane);
                    layout.putConstraint(SpringLayout.NORTH, dsn, 10, SpringLayout.NORTH, pane);
                    layout.putConstraint(SpringLayout.WEST, table, 25, SpringLayout.EAST, dsn);
                    layout.putConstraint(SpringLayout.NORTH, table, 10, SpringLayout.NORTH, pane);
                    layout.putConstraint(SpringLayout.WEST, col, 25, SpringLayout.EAST, table);
                    layout.putConstraint(SpringLayout.NORTH, col, 10, SpringLayout.NORTH, pane);
                    layout.putConstraint(SpringLayout.WEST, type, 25, SpringLayout.EAST, col);
                    layout.putConstraint(SpringLayout.NORTH, type, 10, SpringLayout.NORTH, pane);
                    layout.putConstraint(SpringLayout.WEST, cond, 5, SpringLayout.WEST, pane);
                    layout.putConstraint(SpringLayout.NORTH, cond, 40, SpringLayout.NORTH, pane);
                    layout.putConstraint(SpringLayout.NORTH, p1, 40, SpringLayout.NORTH, pane);
                    layout.putConstraint(SpringLayout.NORTH, p2, 40, SpringLayout.NORTH, pane);
                    layout.putConstraint(SpringLayout.NORTH, p3, 40, SpringLayout.NORTH, pane);
                    layout.putConstraint(SpringLayout.WEST, p1, 25, SpringLayout.EAST, cond);
                    layout.putConstraint(SpringLayout.WEST, p2, 25, SpringLayout.EAST, p1);
                    layout.putConstraint(SpringLayout.WEST, p3, 25, SpringLayout.EAST, p2);
                    layout.putConstraint(SpringLayout.WEST, cc1, 5, SpringLayout.WEST, pane);
                    layout.putConstraint(SpringLayout.NORTH, cc1, 70, SpringLayout.NORTH, pane);
                    layout.putConstraint(SpringLayout.NORTH, cc2, 70, SpringLayout.NORTH, pane);
                    layout.putConstraint(SpringLayout.NORTH, cc3, 70, SpringLayout.NORTH, pane);
                    layout.putConstraint(SpringLayout.WEST, cc2, 100, SpringLayout.EAST, cc1);
                    layout.putConstraint(SpringLayout.WEST, cc3, 100, SpringLayout.EAST, cc2);
                    layout.putConstraint(SpringLayout.WEST, p_t, 5, SpringLayout.WEST, pane);
                    layout.putConstraint(SpringLayout.NORTH, p_t, 100, SpringLayout.NORTH, pane);
                    layout.putConstraint(SpringLayout.WEST, p_c_l, 5, SpringLayout.WEST, pane);
                    layout.putConstraint(SpringLayout.NORTH, p_c_l, 130, SpringLayout.NORTH, pane);
                    layout.putConstraint(SpringLayout.WEST, p_c, 10, SpringLayout.EAST, p_c_l);
                    layout.putConstraint(SpringLayout.NORTH, p_c, 10, SpringLayout.SOUTH, p_t);
                    layout.putConstraint(SpringLayout.WEST, q_table, 5, SpringLayout.WEST, pane);
                    layout.putConstraint(SpringLayout.NORTH, q_table, 15, SpringLayout.SOUTH, p_c);
                    pane.add(dsn);
                    pane.add(table);
                    pane.add(col);
                    pane.add(type);
                    pane.add(cond);
                    pane.add(p1);
                    pane.add(p2);
                    pane.add(p3);
                    pane.add(cc1);
                    pane.add(cc2);
                    pane.add(cc3);
                    pane.add(p_t);
                    pane.add(p_c_l);
                    pane.add(p_c);
                    pane.add(q_table);
                    JScrollPane s_p = new JScrollPane(pane);
                    s_p.setPreferredSize(new Dimension(900, 700));
                    frame.setContentPane(s_p);
                    frame.setLocation(75, 5);
                    frame.pack();
                    frame.setVisible(true);
                } else {
                    _hashInfo = (Hashtable) in.readObject();
                    JPanel top = (JPanel) in.readObject();
                    ReportTable _t_1 = (ReportTable) in.readObject();
                    ReportTable _t_2 = (ReportTable) in.readObject();
                    ReportTable _t_3 = (ReportTable) in.readObject();
                    JTabbedPane _ta_p = new JTabbedPane();
                    _ta_p.addTab("Frequency Analysis", null, _t_1, "Frequency Analyis");
                    _ta_p.addTab("Variation Analysis", null, _t_2, "Variation Analysis");
                    _ta_p.addTab("Percentile Analysis", null, _t_3, "Percentile Analysis");
                    add(_ta_p);
                    JFrame.setDefaultLookAndFeelDecorated(true);
                    JFrame frame = new JFrame("Advance Number Analysis");
                    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    JComponent newContentPane = _ta_p;
                    newContentPane.setOpaque(true);
                    frame.getContentPane().add(top, BorderLayout.PAGE_START);
                    frame.getContentPane().add(newContentPane, BorderLayout.CENTER);
                    frame.setLocation(100, 100);
                    frame.pack();
                    frame.setVisible(true);
                }
            } catch (FileNotFoundException file_exp) {
                JOptionPane.showMessageDialog(null, file_exp.getMessage(), "Error Message", JOptionPane.ERROR_MESSAGE);
                file_exp.printStackTrace();
            } catch (IOException exp) {
                JOptionPane.showMessageDialog(null, exp.getMessage(), "Error Message", JOptionPane.ERROR_MESSAGE);
                exp.printStackTrace();
            } catch (ClassNotFoundException cl_exp) {
                JOptionPane.showMessageDialog(null, cl_exp.getMessage(), "Error Message", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
