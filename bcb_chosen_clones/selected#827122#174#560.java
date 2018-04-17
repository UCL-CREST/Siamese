    private void createActions() {
        newAction = new NBTAction("New", "New", "New", KeyEvent.VK_N) {

            {
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('N', Event.CTRL_MASK));
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                updateTreeTable(new CompoundTag(""));
            }
        };
        browseAction = new NBTAction("Browse...", "Open", "Browse...", KeyEvent.VK_O) {

            {
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('O', Event.CTRL_MASK));
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = createFileChooser();
                switch(fc.showOpenDialog(TreeFrame.this)) {
                    case JFileChooser.APPROVE_OPTION:
                        File file = fc.getSelectedFile();
                        Preferences prefs = getPreferences();
                        prefs.put(KEY_FILE, file.getAbsolutePath());
                        doImport(file);
                        break;
                }
            }
        };
        saveAction = new NBTAction("Save", "Save", "Save", KeyEvent.VK_S) {

            {
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('S', Event.CTRL_MASK));
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                String path = textFile.getText();
                File file = new File(path);
                if (file.canWrite()) {
                    doExport(file);
                } else {
                    saveAsAction.actionPerformed(e);
                }
            }
        };
        saveAsAction = new NBTAction("Save As...", "SaveAs", "Save As...", KeyEvent.VK_UNDEFINED) {

            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = createFileChooser();
                switch(fc.showSaveDialog(TreeFrame.this)) {
                    case JFileChooser.APPROVE_OPTION:
                        File file = fc.getSelectedFile();
                        Preferences prefs = getPreferences();
                        prefs.put(KEY_FILE, file.getAbsolutePath());
                        doExport(file);
                        break;
                }
            }
        };
        refreshAction = new NBTAction("Refresh", "Refresh", "Refresh", KeyEvent.VK_F5) {

            {
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("F5"));
            }

            public void actionPerformed(ActionEvent e) {
                String path = textFile.getText();
                File file = new File(path);
                if (file.canRead()) doImport(file); else showErrorDialog("The file could not be read.");
            }
        };
        exitAction = new NBTAction("Exit", "Exit", KeyEvent.VK_ESCAPE) {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        };
        cutAction = new DefaultEditorKit.CutAction() {

            {
                String name = "Cut";
                putValue(NAME, name);
                putValue(SHORT_DESCRIPTION, name);
                putValue(MNEMONIC_KEY, KeyEvent.VK_X);
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('X', Event.CTRL_MASK));
                ImageFactory factory = new ImageFactory();
                try {
                    putValue(SMALL_ICON, new ImageIcon(factory.readGeneralImage(name, NBTAction.smallIconSize)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    putValue(LARGE_ICON_KEY, new ImageIcon(factory.readGeneralImage(name, NBTAction.largeIconSize)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        copyAction = new DefaultEditorKit.CopyAction() {

            {
                String name = "Copy";
                putValue(NAME, name);
                putValue(SHORT_DESCRIPTION, name);
                putValue(MNEMONIC_KEY, KeyEvent.VK_C);
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('C', Event.CTRL_MASK));
                ImageFactory factory = new ImageFactory();
                try {
                    putValue(SMALL_ICON, new ImageIcon(factory.readGeneralImage(name, NBTAction.smallIconSize)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    putValue(LARGE_ICON_KEY, new ImageIcon(factory.readGeneralImage(name, NBTAction.largeIconSize)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        pasteAction = new DefaultEditorKit.CutAction() {

            {
                String name = "Paste";
                putValue(NAME, name);
                putValue(SHORT_DESCRIPTION, name);
                putValue(MNEMONIC_KEY, KeyEvent.VK_V);
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('V', Event.CTRL_MASK));
                ImageFactory factory = new ImageFactory();
                try {
                    putValue(SMALL_ICON, new ImageIcon(factory.readGeneralImage(name, NBTAction.smallIconSize)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    putValue(LARGE_ICON_KEY, new ImageIcon(factory.readGeneralImage(name, NBTAction.largeIconSize)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        deleteAction = new NBTAction("Delete", "Delete", "Delete", KeyEvent.VK_DELETE) {

            {
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("DELETE"));
            }

            public void actionPerformed(ActionEvent e) {
                int row = treeTable.getSelectedRow();
                TreePath path = treeTable.getPathForRow(row);
                Object last = path.getLastPathComponent();
                if (last instanceof NBTFileBranch) {
                    NBTFileBranch branch = (NBTFileBranch) last;
                    File file = branch.getFile();
                    String name = file.getName();
                    String message = "Are you sure you want to delete " + name + "?";
                    String title = "Continue?";
                    int option = JOptionPane.showConfirmDialog(TreeFrame.this, message, title, JOptionPane.OK_CANCEL_OPTION);
                    switch(option) {
                        case JOptionPane.CANCEL_OPTION:
                            return;
                    }
                    if (!FileUtils.deleteQuietly(file)) {
                        showErrorDialog(name + " could not be deleted.");
                        return;
                    }
                }
                TreePath parentPath = path.getParentPath();
                Object parentLast = parentPath.getLastPathComponent();
                NBTTreeTableModel model = treeTable.getTreeTableModel();
                int index = model.getIndexOfChild(parentLast, last);
                if (parentLast instanceof Mutable<?>) {
                    Mutable<?> mutable = (Mutable<?>) parentLast;
                    if (last instanceof ByteWrapper) {
                        ByteWrapper wrapper = (ByteWrapper) last;
                        index = wrapper.getIndex();
                    }
                    mutable.remove(index);
                } else {
                    System.err.println(last.getClass());
                    return;
                }
                updateTreeTable();
                treeTable.expandPath(parentPath);
                scrollTo(parentLast);
                treeTable.setRowSelectionInterval(row, row);
            }
        };
        openAction = new NBTAction("Open...", "Open...", KeyEvent.VK_T) {

            {
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('T', Event.CTRL_MASK));
                final int diamondPickaxe = 278;
                SpriteRecord record = NBTTreeTable.register.getRecord(diamondPickaxe);
                BufferedImage image = record.getImage();
                setSmallIcon(image);
                int width = 24, height = 24;
                Dimension size = new Dimension(width, height);
                Map<RenderingHints.Key, ?> hints = Thumbnail.createRenderingHints(Thumbnail.QUALITY);
                BufferedImage largeImage = Thumbnail.createThumbnail(image, size, hints);
                setLargeIcon(largeImage);
            }

            public void actionPerformed(ActionEvent e) {
                TreePath path = treeTable.getPath();
                if (path == null) return;
                Object last = path.getLastPathComponent();
                if (last instanceof Region) {
                    Region region = (Region) last;
                    createAndShowTileCanvas(new TileCanvas.TileWorld(region));
                    return;
                } else if (last instanceof World) {
                    World world = (World) last;
                    createAndShowTileCanvas(world);
                    return;
                }
                if (last instanceof NBTFileBranch) {
                    NBTFileBranch fileBranch = (NBTFileBranch) last;
                    File file = fileBranch.getFile();
                    try {
                        open(file);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        showErrorDialog(ex.getMessage());
                    }
                }
            }

            private void open(File file) throws IOException {
                if (Desktop.isDesktopSupported()) {
                    Desktop desktop = Desktop.getDesktop();
                    if (desktop.isSupported(Desktop.Action.OPEN)) {
                        desktop.open(file);
                    }
                }
            }
        };
        addByteAction = new NBTAction("Add Byte", NBTConstants.TYPE_BYTE, "Add Byte", KeyEvent.VK_1) {

            {
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('1', Event.CTRL_MASK));
            }

            public void actionPerformed(ActionEvent e) {
                addTag(new ByteTag("new byte", (byte) 0));
            }
        };
        addShortAction = new NBTAction("Add Short", NBTConstants.TYPE_SHORT, "Add Short", KeyEvent.VK_2) {

            {
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('2', Event.CTRL_MASK));
            }

            public void actionPerformed(ActionEvent e) {
                addTag(new ShortTag("new short", (short) 0));
            }
        };
        addIntAction = new NBTAction("Add Integer", NBTConstants.TYPE_INT, "Add Integer", KeyEvent.VK_3) {

            {
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('3', Event.CTRL_MASK));
            }

            public void actionPerformed(ActionEvent e) {
                addTag(new IntTag("new int", 0));
            }
        };
        addLongAction = new NBTAction("Add Long", NBTConstants.TYPE_LONG, "Add Long", KeyEvent.VK_4) {

            {
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('4', Event.CTRL_MASK));
            }

            public void actionPerformed(ActionEvent e) {
                addTag(new LongTag("new long", 0));
            }
        };
        addFloatAction = new NBTAction("Add Float", NBTConstants.TYPE_FLOAT, "Add Float", KeyEvent.VK_5) {

            {
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('5', Event.CTRL_MASK));
            }

            public void actionPerformed(ActionEvent e) {
                addTag(new FloatTag("new float", 0));
            }
        };
        addDoubleAction = new NBTAction("Add Double", NBTConstants.TYPE_DOUBLE, "Add Double", KeyEvent.VK_6) {

            {
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('6', Event.CTRL_MASK));
            }

            public void actionPerformed(ActionEvent e) {
                addTag(new DoubleTag("new double", 0));
            }
        };
        addByteArrayAction = new NBTAction("Add Byte Array", NBTConstants.TYPE_BYTE_ARRAY, "Add Byte Array", KeyEvent.VK_7) {

            {
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('7', Event.CTRL_MASK));
            }

            public void actionPerformed(ActionEvent e) {
                addTag(new ByteArrayTag("new byte array"));
            }
        };
        addStringAction = new NBTAction("Add String", NBTConstants.TYPE_STRING, "Add String", KeyEvent.VK_8) {

            {
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('8', Event.CTRL_MASK));
            }

            public void actionPerformed(ActionEvent e) {
                addTag(new StringTag("new string", "..."));
            }
        };
        addListAction = new NBTAction("Add List Tag", NBTConstants.TYPE_LIST, "Add List Tag", KeyEvent.VK_9) {

            {
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('9', Event.CTRL_MASK));
            }

            public void actionPerformed(ActionEvent e) {
                Class<? extends Tag> type = queryType();
                if (type != null) addTag(new ListTag("new list", null, type));
            }

            private Class<? extends Tag> queryType() {
                Object[] items = { NBTConstants.TYPE_BYTE, NBTConstants.TYPE_SHORT, NBTConstants.TYPE_INT, NBTConstants.TYPE_LONG, NBTConstants.TYPE_FLOAT, NBTConstants.TYPE_DOUBLE, NBTConstants.TYPE_BYTE_ARRAY, NBTConstants.TYPE_STRING, NBTConstants.TYPE_LIST, NBTConstants.TYPE_COMPOUND };
                JComboBox comboBox = new JComboBox(new DefaultComboBoxModel(items));
                comboBox.setRenderer(new DefaultListCellRenderer() {

                    @Override
                    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                        if (value instanceof Integer) {
                            Integer i = (Integer) value;
                            Class<? extends Tag> c = NBTUtils.getTypeClass(i);
                            String name = NBTUtils.getTypeName(c);
                            setText(name);
                        }
                        return this;
                    }
                });
                Object[] message = { new JLabel("Please select a type."), comboBox };
                String title = "Title goes here";
                int result = JOptionPane.showOptionDialog(TreeFrame.this, message, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                switch(result) {
                    case JOptionPane.OK_OPTION:
                        ComboBoxModel model = comboBox.getModel();
                        Object item = model.getSelectedItem();
                        if (item instanceof Integer) {
                            Integer i = (Integer) item;
                            return NBTUtils.getTypeClass(i);
                        }
                }
                return null;
            }
        };
        addCompoundAction = new NBTAction("Add Compound Tag", NBTConstants.TYPE_COMPOUND, "Add Compound Tag", KeyEvent.VK_0) {

            {
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke('0', Event.CTRL_MASK));
            }

            public void actionPerformed(ActionEvent e) {
                addTag(new CompoundTag());
            }
        };
        String name = "About " + TITLE;
        helpAction = new NBTAction(name, "Help", name, KeyEvent.VK_F1) {

            {
                putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("F1"));
            }

            public void actionPerformed(ActionEvent e) {
                Object[] message = { new JLabel(TITLE + " " + VERSION), new JLabel("© Copyright Taggart Spilman 2011.  All rights reserved."), new Hyperlink("<html><a href=\"#\">NamedBinaryTag.com</a></html>", "http://www.namedbinarytag.com"), new Hyperlink("<html><a href=\"#\">Contact</a></html>", "mailto:tagadvance@gmail.com"), new JLabel(" "), new Hyperlink("<html><a href=\"#\">JNBT was written by Graham Edgecombe</a></html>", "http://jnbt.sf.net"), new Hyperlink("<html><a href=\"#\">Available open-source under the BSD license</a></html>", "http://jnbt.sourceforge.net/LICENSE.TXT"), new JLabel(" "), new JLabel("This product includes software developed by"), new Hyperlink("<html><a href=\"#\">The Apache Software Foundation</a>.</html>", "http://www.apache.org"), new JLabel(" "), new JLabel("Default texture pack:"), new Hyperlink("<html><a href=\"#\">SOLID COLOUR. SOLID STYLE.</a></html>", "http://www.minecraftforum.net/topic/72253-solid-colour-solid-style/"), new JLabel("Bundled with the permission of Trigger_Proximity.") };
                String title = "About";
                JOptionPane.showMessageDialog(TreeFrame.this, message, title, JOptionPane.INFORMATION_MESSAGE);
            }
        };
    }
