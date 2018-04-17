    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {
        int returnVal = fc.showSaveDialog(DemarcationsAuto.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File userFile = fc.getSelectedFile();
            String path = userFile.getPath();
            if (!((path.substring(path.length() - 4, path.length())).equals(".csv"))) {
                userFile = new File(userFile.getPath() + ".csv");
            }
            narr.println("Saving to: " + userFile.getName());
            try {
                FileWriter writer = new FileWriter(userFile);
                writer.append("Ecotype Number");
                for (int i = 1; i <= highestSequenceNum(); i++) {
                    writer.append(',' + "Sequence " + i);
                }
                Iterator<ArrayList<String>> ecotypesIterator = ecotypes.iterator();
                for (int j = 1; ecotypesIterator.hasNext(); j++) {
                    writer.append('\n');
                    writer.append("" + j);
                    ArrayList<String> currentEcotype = ecotypesIterator.next();
                    Iterator seqIterator = currentEcotype.iterator();
                    while (seqIterator.hasNext()) {
                        writer.append("," + seqIterator.next());
                    }
                }
                writer.append('\n');
                writer.append("Outgroup");
                writer.append(outgroup);
                writer.append('\n');
                writer.append("Recombinants");
                Iterator<String> iter = recombs.iterator();
                while (iter.hasNext()) {
                    writer.append("," + iter.next());
                }
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
