    public void startElement(String uri, String tag, String qName, org.xml.sax.Attributes attributes) throws SAXException {
        wabclient.Attributes prop = new wabclient.Attributes(attributes);
        try {
            if (tag.equals("window")) {
                if (prop == null) {
                    System.err.println("window without properties");
                    return;
                }
                int x = prop.getValue("x", 0);
                int y = prop.getValue("y", 0);
                int width = prop.getValue("width", 0);
                int height = prop.getValue("height", 0);
                Color bgcolor = prop.getValue("bgcolor", Color.white);
                String caption = prop.getValue("caption", "");
                layout = prop.getValue("layout", 0);
                boolean statusbar = prop.getValue("statusbar", false);
                if (sheet.opentype == WABSheet.LAYERED) {
                    Point pos = frame.getNextMDIPos();
                    sheet.setBounds(pos.x, pos.y, 400, 200);
                    sheet.setNormalBounds(new Rectangle(pos.x, pos.y, 400, 200));
                } else {
                    sheet.setBounds(x, y, width, height);
                    sheet.setNormalBounds(new Rectangle(x, y, width, height));
                }
                if (sheet.opentype == WABSheet.MAXIMIZED) sheet.setMaximum(true); else sheet.setMaximum(false);
                sheet.setTitle(caption);
                frame.addSheetToMenu(caption);
                sheet.setBackground(bgcolor);
                if (layout == 1) sheet.getContentPane().setLayout(new FlowLayout()); else if (layout == 2) sheet.getContentPane().setLayout(new BorderLayout()); else sheet.getContentPane().setLayout(null);
            } else if (tag.equals("menu")) {
                if (prop == null) {
                    System.err.println("menu without properties");
                    return;
                }
                String id = prop.getValue("id", "");
                String label = prop.getValue("label", "");
                if ((id != null && id.equals("WINDOW_MENU") || label.equalsIgnoreCase("window"))) {
                    windowMenu = new JMenu();
                    menu = windowMenu;
                    sheet.setWindowMenu(menu);
                } else {
                    menu = new JMenu();
                }
                menu.setText(label);
                sheet.menubar.add(menu);
            } else if (tag.equals("menuitem")) {
                if (prop == null) {
                    System.err.println("menuitem without properties");
                    return;
                }
                JMenuItem item;
                String action = prop.getValue("action", "");
                String label = prop.getValue("label", "");
                boolean visible = prop.getValue("visible", true);
                String icon = prop.getValue("icon", "");
                if (action.equals("WINDOW_OVERLAPPED")) {
                    item = windowMenuOverlapped = new JMenuItem();
                    item.setActionCommand("10001");
                    item.addActionListener(frame);
                } else if (action.equals("WINDOW_TILE_HORIZONTALLY")) {
                    item = windowMenuTile = new JMenuItem();
                    item.setActionCommand("10002");
                    item.addActionListener(frame);
                } else if (action.equals("WINDOW_TILE_VERTICALLY")) {
                    item = windowMenuArrange = new JMenuItem();
                    item.setActionCommand("10003");
                    item.addActionListener(frame);
                } else {
                    item = new JMenuItem();
                    item.setActionCommand(action);
                    item.addActionListener((WABClient) global);
                }
                item.setText(label);
                if (!visible) menu.setVisible(false);
                menu.add(item);
                if (frame.getToolBar() != null) {
                    if (icon.length() > 0) {
                        try {
                            ImageIcon img = new ImageIcon(new URL(icon));
                            BufferedImage image = new BufferedImage(25, 25, BufferedImage.TYPE_4BYTE_ABGR);
                            Graphics g = image.createGraphics();
                            g.setColor(new Color(0, 0, 0, 0));
                            g.fillRect(0, 0, 25, 25);
                            g.drawImage(img.getImage(), 4, 4, 16, 16, (ImageObserver) null);
                            BufferedImage pressed = new BufferedImage(25, 25, BufferedImage.TYPE_4BYTE_ABGR);
                            g = pressed.createGraphics();
                            g.setColor(new Color(0, 0, 0, 0));
                            g.fillRect(0, 0, 25, 25);
                            g.drawImage(img.getImage(), 5, 5, 16, 16, (ImageObserver) null);
                            g.setColor(new Color(132, 132, 132));
                            g.drawLine(0, 0, 24, 0);
                            g.drawLine(0, 0, 0, 24);
                            g.setColor(new Color(255, 255, 255));
                            g.drawLine(24, 24, 24, 0);
                            g.drawLine(24, 24, 0, 24);
                            BufferedImage over = new BufferedImage(25, 25, BufferedImage.TYPE_4BYTE_ABGR);
                            g = over.createGraphics();
                            g.setColor(new Color(0, 0, 0, 0));
                            g.fillRect(0, 0, 25, 25);
                            g.drawImage(img.getImage(), 4, 4, 16, 16, (ImageObserver) null);
                            g.setColor(new Color(255, 255, 255));
                            g.drawLine(0, 0, 24, 0);
                            g.drawLine(0, 0, 0, 24);
                            g.setColor(new Color(132, 132, 132));
                            g.drawLine(24, 24, 24, 0);
                            g.drawLine(24, 24, 0, 24);
                            JButton b = new JButton(new ImageIcon(image));
                            b.setRolloverEnabled(true);
                            b.setPressedIcon(new ImageIcon(pressed));
                            b.setRolloverIcon(new ImageIcon(over));
                            b.setToolTipText(label);
                            b.setActionCommand(action);
                            b.setFocusPainted(false);
                            b.setBorderPainted(false);
                            b.setContentAreaFilled(false);
                            b.setMargin(new Insets(0, 0, 0, 0));
                            b.addActionListener(sheet);
                            sheet.toolbar.add(b);
                        } catch (Exception e) {
                        }
                    }
                }
            } else if (tag.equals("separator")) {
                menu.addSeparator();
            } else if (tag.equals("choice")) {
                if (prop == null) {
                    System.err.println("choice without properties");
                    return;
                }
                combo = new JComboBox();
                list = null;
                int x = prop.getValue("x", 0);
                int y = prop.getValue("y", 0);
                int width = prop.getValue("width", 0);
                int height = prop.getValue("height", 0);
                String id = prop.getValue("id", "");
                Object constraints = prop.getValue("constraints");
                boolean editable = prop.getValue("editable", false);
                boolean visible = prop.getValue("visible", true);
                boolean enabled = prop.getValue("enabled", true);
                combo_text = prop.getValue("text", "");
                combo.setBounds(x, y, width, height);
                combo.setName((String) id);
                if (editable) {
                    combo.setEditable(editable);
                    combo.setSelectedItem(combo_text);
                }
                if (!visible) combo.setVisible(false);
                if (!enabled) combo.setEnabled(false);
                if (layout == 0) sheet.getContentPane().add(combo); else sheet.getContentPane().add(combo, constraints);
            } else if (tag.equals("list")) {
                if (prop == null) {
                    System.err.println("list without properties");
                    return;
                }
                list = new JList();
                combo = null;
                listdata = new Vector();
                int x = prop.getValue("x", 0);
                int y = prop.getValue("y", 0);
                int width = prop.getValue("width", 0);
                int height = prop.getValue("height", 0);
                String id = prop.getValue("id", "");
                Object constraints = prop.getValue("constraints");
                list.setName((String) id);
                list.setListData(listdata);
                JScrollPane sp = new JScrollPane(list);
                sp.setBounds(x, y, width, height);
                if (layout == 0) sheet.getContentPane().add(sp); else sheet.getContentPane().add(sp, constraints);
            } else if (tag.equals("option")) {
                if (prop == null) {
                    System.err.println("choice.option without properties");
                    return;
                }
                String value = prop.getValue("value", "");
                String text = prop.getValue("text", "");
                if (list != null) listdata.addElement(new ComboOption(text, value));
                if (combo != null) {
                    ComboOption co = new ComboOption(text, value);
                    combo.addItem(co);
                    if (combo_text.equals(text.trim())) combo.setSelectedItem(co);
                }
            } else if (tag.equals("label")) {
                if (prop == null) {
                    System.err.println("label without properties");
                    return;
                }
                JLabel label = new JLabel();
                int x = prop.getValue("x", 0);
                int y = prop.getValue("y", 0);
                int width = prop.getValue("width", 0);
                int height = prop.getValue("height", 0);
                String text = prop.getValue("text", "");
                Object constraints = prop.getValue("constraints");
                boolean visible = prop.getValue("visible", true);
                label.setBounds(x, y, width, height);
                label.setText(text);
                if (!visible) label.setVisible(false);
                if (layout == 0) sheet.getContentPane().add(label); else sheet.getContentPane().add(label, constraints);
            } else if (tag.equals("button")) {
                if (prop == null) {
                    System.err.println("button without properties");
                    return;
                }
                JButton btn = new JButton();
                int x = prop.getValue("x", 0);
                int y = prop.getValue("y", 0);
                int width = prop.getValue("width", 0);
                int height = prop.getValue("height", 0);
                String id = prop.getValue("id", "");
                String text = prop.getValue("text", "");
                String onmouseup = prop.getValue("onmouseup", "");
                Object constraints = prop.getValue("constraints");
                btn.setBounds(x, y, width, height);
                btn.setText(text);
                btn.addActionListener(sheet);
                btn.setActionCommand(onmouseup);
                if (layout == 0) sheet.getContentPane().add(btn); else sheet.getContentPane().add(btn, constraints);
            } else if (tag.equals("radiobutton")) {
                if (prop == null) {
                    System.err.println("radiobutton without properties");
                    return;
                }
                JRadioButton rb = new JRadioButton();
                int x = prop.getValue("x", 0);
                int y = prop.getValue("y", 0);
                int width = prop.getValue("width", 0);
                int height = prop.getValue("height", 0);
                String id = prop.getValue("id", "");
                String text = prop.getValue("text", "");
                Object constraints = prop.getValue("constraints");
                String checked = prop.getValue("checked", "false");
                rb.setBounds(x, y, width, height);
                rb.setText(text);
                rb.setName((String) id);
                rb.addActionListener(sheet);
                rb.setSelected(checked.equalsIgnoreCase("true"));
                if (layout == 0) sheet.getContentPane().add(rb); else sheet.getContentPane().add(rb, constraints);
            } else if (tag.equals("checkbox")) {
                if (prop == null) {
                    System.err.println("checkbox without properties");
                    return;
                }
                JCheckBox cb = new JCheckBox();
                int x = prop.getValue("x", 0);
                int y = prop.getValue("y", 0);
                int width = prop.getValue("width", 0);
                int height = prop.getValue("height", 0);
                String id = prop.getValue("id", "");
                String text = prop.getValue("text", "");
                String onmouseup = prop.getValue("onmouseup", "");
                Object constraints = prop.getValue("constraints");
                String checked = prop.getValue("checked", "false");
                cb.setBounds(x, y, width, height);
                cb.setText(text);
                cb.setName((String) id);
                cb.setSelected(checked.equalsIgnoreCase("true"));
                if (layout == 0) sheet.getContentPane().add(cb); else sheet.getContentPane().add(cb, constraints);
            } else if (tag.equals("image")) {
                if (prop == null) {
                    System.err.println("image without properties");
                    return;
                }
                JLabel label = new JLabel();
                int x = prop.getValue("x", 0);
                int y = prop.getValue("y", 0);
                int width = prop.getValue("width", 0);
                int height = prop.getValue("height", 0);
                String src = prop.getValue("src", "");
                Object constraints = prop.getValue("constraints");
                label.setIcon(new ImageIcon(new URL(src)));
                label.setBounds(x, y, width, height);
                if (layout == 0) sheet.getContentPane().add(label); else sheet.getContentPane().add(label, constraints);
            } else if (tag.equals("singlelineedit")) {
                if (prop == null) {
                    System.err.println("singlelineedit without properties");
                    return;
                }
                String pwd = prop.getValue("password", "");
                JTextField sle;
                if (pwd.equalsIgnoreCase("true")) sle = new JPasswordField(); else sle = new JTextField();
                int x = prop.getValue("x", 0);
                int y = prop.getValue("y", 0);
                int width = prop.getValue("width", 0);
                int height = prop.getValue("height", 0);
                Object id = prop.getValue("id");
                String text = prop.getValue("text", "");
                Object constraints = prop.getValue("constraints");
                sle.setBounds(x, y, width, height);
                sle.setText(text);
                sle.setName((String) id);
                if (layout == 0) sheet.getContentPane().add(sle); else sheet.getContentPane().add(sle, constraints);
            } else if (tag.equals("treeview")) {
                if (prop == null) {
                    System.err.println("treeview without properties");
                    return;
                }
                treeview_root = new DefaultMutableTreeNode("root");
                treeview = new JTree(treeview_root);
                Object constraints = prop.getValue("constraints");
                sheet.getContentPane().add(new JScrollPane(treeview), constraints);
            } else if (tag.equals("treeitem")) {
                if (prop == null) {
                    System.err.println("treeview.treeitem without properties");
                    return;
                }
                String text = prop.getValue("text", "");
                String value = prop.getValue("value", "");
                DefaultMutableTreeNode node = new DefaultMutableTreeNode(text);
                treeview_root.add(node);
            } else if (tag.equals("table")) {
                if (prop == null) {
                    System.err.println("table without properties");
                    return;
                }
                String id = prop.getValue("id", "");
                table = new JTable();
                model = new DefaultTableModel() {

                    public boolean isCellEditable(int row, int col) {
                        return false;
                    }
                };
                table.setModel(model);
                table.setName((String) id);
                Object constraints = prop.getValue("constraints");
                sheet.getContentPane().add(new JScrollPane(table), constraints);
                rowNumber = 0;
                columnNumber = 0;
                headerWidths = new Vector();
            } else if (tag.equals("header")) {
                if (prop == null) {
                    System.err.println("table.header without properties");
                    return;
                }
                String text = prop.getValue("text", "");
                int width = prop.getValue("width", 0);
                headerWidths.addElement(new Integer(width));
                model.addColumn(text);
            } else if (tag.equals("row")) {
                rowNumber++;
                columnNumber = 0;
                model.setRowCount(rowNumber);
            } else if (tag.equals("column")) {
                columnNumber++;
                if (prop == null) {
                    System.err.println("table.column without properties");
                    return;
                }
                String value = prop.getValue("value", "");
                model.setValueAt(value, rowNumber - 1, columnNumber - 1);
            } else if (tag.equals("script")) {
                sheet.beginScript();
                String url = prop.getValue("src");
                if (url.length() > 0) {
                    try {
                        BufferedReader r = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
                        String buffer;
                        while (true) {
                            buffer = r.readLine();
                            if (buffer == null) break;
                            sheet.script += buffer + "\n";
                        }
                        r.close();
                        sheet.endScript();
                    } catch (IOException ioe) {
                        System.err.println("[IOError] " + ioe.getMessage());
                        System.exit(0);
                    }
                }
            } else System.err.println("[sheet] unparsed tag: " + tag);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
