    @Override
    public void run() {
        try {
            FileChannel in = new FileInputStream(inputfile).getChannel();
            long pos = 0;
            for (int i = 1; i <= noofparts; i++) {
                FileChannel out = new FileOutputStream(outputfile.getAbsolutePath() + "." + "v" + i).getChannel();
                status.setText("Rozdělovač: Rozděluji část " + i + "..");
                if (remainingsize >= splitsize) {
                    in.transferTo(pos, splitsize, out);
                    pos += splitsize;
                    remainingsize -= splitsize;
                } else {
                    in.transferTo(pos, remainingsize, out);
                }
                pb.setValue(100 * i / noofparts);
                out.close();
            }
            in.close();
            if (deleteOnFinish) new File(inputfile + "").delete();
            status.setText("Rozdělovač: Hotovo..");
            JOptionPane.showMessageDialog(null, "Rozděleno!", "Rozdělovač", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
        }
    }
