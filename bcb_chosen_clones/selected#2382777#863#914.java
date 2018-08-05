    public void startElement(String uri, String tag, String qName, org.xml.sax.Attributes attributes) throws SAXException {
        wabclient.Attributes prop = new wabclient.Attributes(attributes);
        try {
            if (tag.equals("window")) startWindow(prop); else if (tag.equals("splitpanel")) startSplitPanel(prop); else if (tag.equals("desktoppane")) startDesktopPane(prop); else if (tag.equals("tabcontrol")) startTabcontrol(prop); else if (tag.equals("panel")) startPanel(prop); else if (tag.equals("statusbar")) startStatusbar(prop); else if (tag.equals("toolbar")) startToolbar(prop); else if (tag.equals("toolbarbutton")) startToolbarbutton(prop); else if (tag.equals("menu")) startMenu(prop); else if (tag.equals("menuitem")) startMenuitem(prop); else if (tag.equals("separator")) menu.addSeparator(); else if (tag.equals("choice")) startChoice(prop); else if (tag.equals("list")) startList(prop); else if (tag.equals("option")) startOption(prop); else if (tag.equals("label")) startLabel(prop); else if (tag.equals("button")) startButton(prop); else if (tag.equals("groupbox")) startGroupbox(prop); else if (tag.equals("radiobutton")) startRadioButton(prop); else if (tag.equals("checkbox")) startCheckbox(prop); else if (tag.equals("image")) startImage(prop); else if (tag.equals("textarea")) startTextArea(prop); else if (tag.equals("singlelineedit")) startSingleLineEdit(prop); else if (tag.equals("treeview")) startTreeview(prop); else if (tag.equals("treeitem")) startTreeitem(prop); else if (tag.equals("table")) startTable(prop); else if (tag.equals("header")) startHeader(prop); else if (tag.equals("row")) {
                rowNumber++;
                columnNumber = 0;
                model.addRow();
            } else if (tag.equals("column")) {
                columnNumber++;
                if (prop == null) {
                    System.err.println("table.column without properties");
                    return;
                }
                String value = prop.getValue("value", "");
                model.setValueAt(value, rowNumber - 1, columnNumber - 1);
            } else if (tag.equals("rmbmenuitem")) {
                if (prop == null) {
                    System.err.println("datawindow.menuitem without properties");
                    return;
                }
                String action = prop.getValue("action", "");
                String label = prop.getValue("label", "");
                JMenuItem mi = new JMenuItem(label);
                mi.setActionCommand(action);
                mi.addActionListener(win);
                rmbmenu.add(mi);
            } else if (tag.equals("rmbseparator")) {
                rmbmenu.addSeparator();
            } else if (tag.equals("script")) {
                win.beginScript();
                String url = prop.getValue("src");
                if (url.length() > 0) {
                    try {
                        BufferedReader r = new BufferedReader(new InputStreamReader(new URL(url).openStream()));
                        String buffer;
                        while (true) {
                            buffer = r.readLine();
                            if (buffer == null) break;
                            win.script += buffer + "\n";
                        }
                        r.close();
                        win.endScript();
                    } catch (IOException ioe) {
                        System.err.println("[IOError] " + ioe.getMessage());
                        System.exit(0);
                    }
                }
            } else System.err.println("[win] unparsed tag: " + tag);
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
