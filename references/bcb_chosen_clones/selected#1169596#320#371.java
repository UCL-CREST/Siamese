    public void run() {
        if (this.progressBar == null) {
            this.progressBar = new JProgressBar(0, 100);
            this.progressBar.setStringPainted(true);
            this.progress = new JPanel();
            final JPanel jp = new JPanel(new GridLayout(2, 1));
            this.jl = new JList();
            this.dlm = new DefaultListModel();
            this.jl.setModel(this.dlm);
            jp.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.LOWERED));
            jp.add(new JScrollPane(this.jl));
            this.progress.add(new JLabel("Overall progress:"), BorderLayout.SOUTH);
            this.progress.add(this.progressBar, BorderLayout.SOUTH);
            jp.add(this.progress);
            this.jf.add(jp);
            if (this.jf instanceof JSplitPane) {
                ((JSplitPane) this.jf).setDividerLocation(0.5);
            }
        }
        this.lhmp.addPropertyChangeListener(this);
        this.lhmp.addListener(this);
        this.lhmp.execute();
        IWorkflow iw;
        final Date d = null;
        try {
            iw = getProcess().get();
            final File result = new File(iw.getName()).getParentFile();
            if (Desktop.isDesktopSupported()) {
                try {
                    if (result.exists()) {
                        Desktop.getDesktop().browse(result.toURI());
                    } else {
                        Desktop.getDesktop().browse(result.getParentFile().toURI());
                    }
                } catch (final IOException e) {
                    this.log.error("{}", "Could not access Desktop object");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please view your results at " + result.getParentFile());
            }
            System.exit(0);
        } catch (final CancellationException e1) {
            this.jl.setModel(new DefaultListModel());
            this.progressBar.setValue(0);
            this.log.error(e1.getLocalizedMessage());
            return;
        } catch (final InterruptedException e1) {
            LocalHostLauncher.handleRuntimeException(this.log, e1, this, this.startup);
        } catch (final ExecutionException e1) {
            LocalHostLauncher.handleRuntimeException(this.log, e1, this, this.startup);
        }
    }
