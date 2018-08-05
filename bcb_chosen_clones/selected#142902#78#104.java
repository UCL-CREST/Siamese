    private void internalTransferComplete(File tmpfile) {
        System.out.println("transferComplete : " + tmpfile);
        try {
            File old = new File(m_destination.toString() + ".old");
            old.delete();
            File current = m_destination;
            current.renameTo(old);
            FileInputStream fis = new FileInputStream(tmpfile);
            FileOutputStream fos = new FileOutputStream(m_destination);
            BufferedInputStream in = new BufferedInputStream(fis);
            BufferedOutputStream out = new BufferedOutputStream(fos);
            for (int read = in.read(); read != -1; read = in.read()) {
                out.write(read);
            }
            out.flush();
            in.close();
            out.close();
            fis.close();
            fos.close();
            tmpfile.delete();
            setVisible(false);
            transferComplete();
        } catch (Exception exc) {
            exc.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while downloading!", "ACLocator Error", JOptionPane.ERROR_MESSAGE);
        }
    }
