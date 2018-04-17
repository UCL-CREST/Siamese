    public void Save() throws IOException {
        File ausgabedatei;
        FileWriter fw;
        BufferedWriter bw;
        JFileChooser fc = new JFileChooser();
        int returnVal = fc.showSaveDialog(panel);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            ausgabedatei = new File(file.getAbsolutePath());
            fw = new FileWriter(ausgabedatei);
            bw = new BufferedWriter(fw);
            for (int i = 1; i < this.RestriktionsResultList.size(); i++) {
                bw.write((this.RestriktionsResultList.get(i).getSequenzID()) + "!");
                bw.write(this.RestriktionsResultList.get(i).getEnzym().getName() + "!" + this.RestriktionsResultList.get(i).getEnzym().getSequenz() + "!" + this.RestriktionsResultList.get(i).getEnzym().getCutpos() + "!");
                StringBuffer xy = new StringBuffer();
                for (int ii = 0; ii < this.RestriktionsResultList.get(i).getSchnittpos().size(); ii++) {
                    if (ii == this.RestriktionsResultList.get(i).getSchnittpos().size() - 1) {
                        xy.append(this.RestriktionsResultList.get(i).getSchnittpos().get(ii) + "!");
                    } else {
                        xy.append(this.RestriktionsResultList.get(i).getSchnittpos().get(ii) + ",");
                    }
                }
                bw.write(xy.toString());
                xy = new StringBuffer();
                for (int ii = 0; ii < this.RestriktionsResultList.get(i).getMultimenge().size(); ii++) {
                    if (ii == this.RestriktionsResultList.get(i).getMultimenge().size() - 1) {
                        xy.append(this.RestriktionsResultList.get(i).getMultimenge().get(ii) + "!");
                    } else {
                        xy.append(this.RestriktionsResultList.get(i).getMultimenge().get(ii) + ",");
                    }
                }
                bw.write(xy.toString());
            }
            bw.close();
            fw.close();
        }
    }
