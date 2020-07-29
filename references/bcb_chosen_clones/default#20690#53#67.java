    public void actionPerformed(ActionEvent e) 
    {
        fileChooser = new JFileChooser("c:\\winnt");
        fileChooser.addChoosableFileFilter(new JAVAFileFilter("class"));
        fileChooser.addChoosableFileFilter(new JAVAFileFilter("java"));
        fileChooser.setFileView(new FileIcon());
        int result = fileChooser.showOpenDialog(f);
        if(result == JFileChooser.APPROVE_OPTION)
        {
            File file = fileChooser.getSelectedFile();
            label.setText("您选择了："+file.getName()+"文件");
        }else if (result == fileChooser.CANCEL_OPTION){
            label.setText("您没有选择任何文件");
        }
    }
