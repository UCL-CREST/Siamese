    private void openFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.showOpenDialog(chooser);
        File file = chooser.getSelectedFile();
        String filename = file.getName();
        System.out.println("open custom open file:");
        File file2 = new File(getCurrentDir() + "/customAlgorithms/" + filename);
        try {
            CopyFile(file, file2);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ui.txt_classname.setText(filename.split(".java")[0] + "");
    }
