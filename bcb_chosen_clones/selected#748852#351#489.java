    public DialogSongList(JFrame frame) {
        super(frame, "Menu_SongList", "songList");
        setMinimumSize(new Dimension(400, 200));
        JPanel panel, spanel;
        Container contentPane;
        (contentPane = getContentPane()).add(songSelector = new SongSelector(configKey, null, true));
        songSelector.setSelectionAction(new Runnable() {

            public void run() {
                final Item<URL, MidiFileInfo> item = songSelector.getSelectedInfo();
                if (item != null) {
                    try {
                        selection = new File(item.getKey().toURI());
                        author.setEnabled(true);
                        title.setEnabled(true);
                        difficulty.setEnabled(true);
                        save.setEnabled(true);
                        final MidiFileInfo info = item.getValue();
                        author.setText(info.getAuthor());
                        title.setText(info.getTitle());
                        Util.selectKey(difficulty, info.getDifficulty());
                        return;
                    } catch (Exception e) {
                    }
                }
                selection = null;
                author.setEnabled(false);
                title.setEnabled(false);
                difficulty.setEnabled(false);
                save.setEnabled(false);
            }
        });
        contentPane.add(panel = new JPanel(), BorderLayout.SOUTH);
        panel.setLayout(new BorderLayout());
        JScrollPane scrollPane;
        panel.add(scrollPane = new JScrollPane(spanel = new JPanel()), BorderLayout.NORTH);
        scrollPane.setPreferredSize(new Dimension(0, 60));
        Util.addLabeledComponent(spanel, "Lbl_Author", author = new JTextField(10));
        Util.addLabeledComponent(spanel, "Lbl_Title", title = new JTextField(14));
        Util.addLabeledComponent(spanel, "Lbl_Difficulty", difficulty = new JComboBox());
        difficulty.addItem(new Item<Byte, String>((byte) -1, ""));
        for (Map.Entry<Byte, String> entry : SongSelector.DIFFICULTIES.entrySet()) {
            final String value = entry.getValue();
            difficulty.addItem(new Item<Byte, String>(entry.getKey(), Util.getMsg(value, value), value));
        }
        spanel.add(save = new JButton());
        Util.updateButtonText(save, "Save");
        save.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final File selected = MidiSong.setMidiFileInfo(selection, author.getText(), title.getText(), getAsByte(difficulty));
                SongSelector.refresh();
                try {
                    songSelector.setSelected(selected == null ? null : selected.toURI().toURL());
                } catch (MalformedURLException ex) {
                }
            }
        });
        author.setEnabled(false);
        title.setEnabled(false);
        difficulty.setEnabled(false);
        save.setEnabled(false);
        JButton button;
        panel.add(spanel = new JPanel(), BorderLayout.WEST);
        spanel.add(button = new JButton());
        Util.updateButtonText(button, "Import");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                final File inputFile = KeyboardHero.midiFile();
                try {
                    if (inputFile == null) return;
                    final File dir = (new File(Util.DATA_FOLDER + MidiSong.MIDI_FILES_DIR));
                    if (dir.exists()) {
                        if (!dir.isDirectory()) {
                            Util.error(Util.getMsg("Err_MidiFilesDirNotDirectory"), dir.getParent());
                            return;
                        }
                    } else if (!dir.mkdirs()) {
                        Util.error(Util.getMsg("Err_CouldntMkDir"), dir.getParent());
                        return;
                    }
                    File outputFile = new File(dir.getPath() + File.separator + inputFile.getName());
                    if (!outputFile.exists() || KeyboardHero.confirm("Que_FileExistsOverwrite")) {
                        final FileChannel inChannel = new FileInputStream(inputFile).getChannel();
                        inChannel.transferTo(0, inChannel.size(), new FileOutputStream(outputFile).getChannel());
                    }
                } catch (Exception ex) {
                    Util.getMsg(Util.getMsg("Err_CouldntImportSong"), ex.toString());
                }
                SongSelector.refresh();
            }
        });
        spanel.add(button = new JButton());
        Util.updateButtonText(button, "Delete");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (KeyboardHero.confirm(Util.getMsg("Que_SureToDelete"))) {
                    try {
                        new File(songSelector.getSelectedFile().toURI()).delete();
                    } catch (Exception ex) {
                        Util.error(Util.getMsg("Err_CouldntDeleteFile"), ex.toString());
                    }
                    SongSelector.refresh();
                }
            }
        });
        panel.add(spanel = new JPanel(), BorderLayout.CENTER);
        spanel.setLayout(new FlowLayout());
        spanel.add(button = new JButton());
        Util.updateButtonText(button, "Close");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                close();
            }
        });
        spanel.add(button = new JButton());
        Util.updateButtonText(button, "Play");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Game.newGame(songSelector.getSelectedFile());
                close();
            }
        });
        panel.add(spanel = new JPanel(), BorderLayout.EAST);
        spanel.add(button = new JButton());
        Util.updateButtonText(button, "Refresh");
        button.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                SongSelector.refresh();
            }
        });
        getRootPane().setDefaultButton(button);
        instance = this;
    }
