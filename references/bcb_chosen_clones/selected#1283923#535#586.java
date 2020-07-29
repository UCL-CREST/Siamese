    public static void main(String[] args) {
        try {
            boolean readExp = Utils.getFlag('l', args);
            final boolean writeExp = Utils.getFlag('s', args);
            final String expFile = Utils.getOption('f', args);
            if ((readExp || writeExp) && (expFile.length() == 0)) {
                throw new Exception("A filename must be given with the -f option");
            }
            Experiment exp = null;
            if (readExp) {
                FileInputStream fi = new FileInputStream(expFile);
                ObjectInputStream oi = new ObjectInputStream(new BufferedInputStream(fi));
                exp = (Experiment) oi.readObject();
                oi.close();
            } else {
                exp = new Experiment();
            }
            System.err.println("Initial Experiment:\n" + exp.toString());
            final JFrame jf = new JFrame("Weka Experiment Setup");
            jf.getContentPane().setLayout(new BorderLayout());
            final SetupPanel sp = new SetupPanel();
            jf.getContentPane().add(sp, BorderLayout.CENTER);
            jf.addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent e) {
                    System.err.println("\nFinal Experiment:\n" + sp.m_Exp.toString());
                    if (writeExp) {
                        try {
                            FileOutputStream fo = new FileOutputStream(expFile);
                            ObjectOutputStream oo = new ObjectOutputStream(new BufferedOutputStream(fo));
                            oo.writeObject(sp.m_Exp);
                            oo.close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            System.err.println("Couldn't write experiment to: " + expFile + '\n' + ex.getMessage());
                        }
                    }
                    jf.dispose();
                    System.exit(0);
                }
            });
            jf.pack();
            jf.setVisible(true);
            System.err.println("Short nap");
            Thread.currentThread().sleep(3000);
            System.err.println("Done");
            sp.setExperiment(exp);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println(ex.getMessage());
        }
    }
