            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    try {
                        SynthMixerSlot.this.ms.setSynth(SynthMixerSlot.this.synthNo, (Synth) (((SynthRegisterEntry) e.getItem()).synthClass.getConstructors()[0].newInstance(new Object[] { SynthMixerSlot.this.ms })));
                    } catch (Exception ex) {
                        SynthMixerSlot.this.ms.setSynth(SynthMixerSlot.this.synthNo, null);
                    }
                }
            }
