    @Override
    public void run() {
        try {
            FileChannel out = new FileOutputStream(outputfile).getChannel();
            long pos = 0;
            status.setText("Slučovač: Proces Slučování spuštěn.. Prosím čekejte..");
            for (int i = 1; i <= noofparts; i++) {
                FileChannel in = new FileInputStream(originalfilename.getAbsolutePath() + "." + "v" + i).getChannel();
                status.setText("Slučovač: Slučuji část " + i + "..");
                this.splitsize = in.size();
                out.transferFrom(in, pos, splitsize);
                pos += splitsize;
                in.close();
                if (deleteOnFinish) new File(originalfilename + ".v" + i).delete();
                pb.setValue(100 * i / noofparts);
            }
            out.close();
            status.setText("Slučovač: Hotovo..");
            JOptionPane.showMessageDialog(null, "Sloučeno!", "Slučovač", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
        }
    }
