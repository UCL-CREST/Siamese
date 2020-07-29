    private void save() {
        int[] selection = list.getSelectionIndices();
        String dir = System.getProperty("user.dir");
        for (int i = 0; i < selection.length; i++) {
            File src = files[selection[i]];
            FileDialog dialog = new FileDialog(shell, SWT.SAVE);
            dialog.setFilterPath(dir);
            dialog.setFileName(src.getName());
            String destination = dialog.open();
            if (destination != null) {
                File dest = new File(destination);
                try {
                    dest.createNewFile();
                    FileChannel srcC = new FileInputStream(src).getChannel();
                    FileChannel destC = new FileOutputStream(dest).getChannel();
                    destC.transferFrom(srcC, 0, srcC.size());
                    destC.close();
                    srcC.close();
                    updateSaved(selection[i], true);
                    files[selection[i]] = dest;
                } catch (FileNotFoundException e) {
                    IVC.showError("Error!", "" + e.getMessage(), "");
                } catch (IOException e) {
                    IVC.showError("Error!", "" + e.getMessage(), "");
                }
            }
        }
    }
