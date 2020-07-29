    public SynthMixerSlot(JPanel synthListPanel, GridBagConstraints gc, SynthRack ms, int synthNo) {
        this.ms = ms;
        this.synthNo = synthNo;
        gc.fill = GridBagConstraints.HORIZONTAL;
        gc.gridwidth = GridBagConstraints.RELATIVE;
        gc.anchor = GridBagConstraints.WEST;
        synthListPanel.add(new JLabel("Program " + synthNo), gc);
        synthListPanel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        synthCB = new JComboBox();
        synthCB.addItem("");
        for (SynthRegisterEntry synthEntry : synthRegister.values()) synthCB.addItem(synthEntry);
        synthCB.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    try {
                        SynthMixerSlot.this.ms.setSynth(SynthMixerSlot.this.synthNo, (Synth) (((SynthRegisterEntry) e.getItem()).synthClass.getConstructors()[0].newInstance(new Object[] { SynthMixerSlot.this.ms })));
                    } catch (Exception ex) {
                        SynthMixerSlot.this.ms.setSynth(SynthMixerSlot.this.synthNo, null);
                    }
                }
            }
        });
        if (ms.getSynth(synthNo) != null) instrumentName.setText(ms.getSynth(synthNo).getInstrumentName());
        synthListPanel.add(instrumentName, gc);
        synthListPanel.add(synthCB, gc);
        JButton showSynthGUIButton = new JButton("Edit");
        showSynthGUIButton.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (SynthMixerSlot.this.ms.synths[SynthMixerSlot.this.synthNo] != null) SynthMixerSlot.this.ms.synths[SynthMixerSlot.this.synthNo].showGUI();
            }
        });
        gc.gridwidth = GridBagConstraints.REMAINDER;
        synthListPanel.add(showSynthGUIButton, gc);
        if (ms.getSynth(synthNo) != null) setSynth(ms.getSynth(synthNo));
    }
