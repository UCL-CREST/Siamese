    protected void saveSelectedFiles() {
        if (dataList.getSelectedRowCount() == 0) {
            return;
        }
        if (dataList.getSelectedRowCount() == 1) {
            Object obj = model.getItemAtRow(sorter.convertRowIndexToModel(dataList.getSelectedRow()));
            AttachFile entry = (AttachFile) obj;
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new File(fc.getCurrentDirectory(), entry.getCurrentPath().getName()));
            if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File current = entry.getCurrentPath();
                File dest = fc.getSelectedFile();
                try {
                    FileInputStream in = new FileInputStream(current);
                    FileOutputStream out = new FileOutputStream(dest);
                    byte[] readBuf = new byte[1024 * 512];
                    int readLength;
                    while ((readLength = in.read(readBuf)) != -1) {
                        out.write(readBuf, 0, readLength);
                    }
                    in.close();
                    out.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return;
        } else {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                for (Integer idx : dataList.getSelectedRows()) {
                    Object obj = model.getItemAtRow(sorter.convertRowIndexToModel(idx));
                    AttachFile entry = (AttachFile) obj;
                    File current = entry.getCurrentPath();
                    File dest = new File(fc.getSelectedFile(), entry.getName());
                    try {
                        FileInputStream in = new FileInputStream(current);
                        FileOutputStream out = new FileOutputStream(dest);
                        byte[] readBuf = new byte[1024 * 512];
                        int readLength;
                        while ((readLength = in.read(readBuf)) != -1) {
                            out.write(readBuf, 0, readLength);
                        }
                        in.close();
                        out.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return;
        }
    }
