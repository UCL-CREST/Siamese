            public void actionPerformed(ActionEvent e) {
                SwingWorker<Void, Void> worker = null;
                int value;
                Simulator.pause();
                value = chooser.showOpenDialog(frame);
                Simulator.resume();
                if (value == JFileChooser.APPROVE_OPTION) try {
                    Simulator.readTestData(chooser.getSelectedFile());
                } catch (FileNotFoundException ex) {
                    return;
                } else if (value == JFileChooser.CANCEL_OPTION) return;
                Simulator.pause();
                if (worker != null) worker.cancel(true);
                worker = new SwingWorker<Void, Void>() {

                    public Void doInBackground() {
                        Trace.clear();
                        Simulator.resume();
                        Simulator.mainLoop();
                        return null;
                    }
                };
                worker.execute();
            }
