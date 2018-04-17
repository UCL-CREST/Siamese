    public void actionPerformed(ActionEvent e)
    {
        File file = null;
        int result;
        
        if (e.getActionCommand().equals("新建文件"))
        {
            fileChooser.setApproveButtonText("确定");
            fileChooser.setDialogTitle("打开文件");
            result = fileChooser.showOpenDialog(f);
            
            textarea.setText("");
    
            if (result == JFileChooser.APPROVE_OPTION)
            {
                file = fileChooser.getSelectedFile();
                label.setText("您选择打开的文件名称为："+file.getName());
            }
            else if(result == JFileChooser.CANCEL_OPTION)
            {
                label.setText("您没有选择任何文件");
            }
            
            FileInputStream fileInStream = null;
            
            if(file != null)
            {
                try{
                    fileInStream = new FileInputStream(file);
                }catch(FileNotFoundException fe){
                    label.setText("File Not Found");
                    return;
                }
                
                int readbyte;
        
                try{
                    while( (readbyte = fileInStream.read()) != -1)
                    {
                        textarea.append(String.valueOf((char)readbyte));
                    }
                }catch(IOException ioe){
                    label.setText("读取文件错误");
                }
                finally{
                    try{
                        if(fileInStream != null)
                            fileInStream.close();
                    }catch(IOException ioe2){}
                }
            }
        }
        
        if (e.getActionCommand().equals("存储文件"))
        {
            result = fileChooser.showSaveDialog(f);
            file = null;
            String fileName;
        
            if (result == JFileChooser.APPROVE_OPTION)
            {
                file = fileChooser.getSelectedFile();
                label.setText("您选择存储的文件名称为："+file.getName());
            }
            else if(result == JFileChooser.CANCEL_OPTION)
            {
                label.setText("您没有选择任何文件");
            }
            
            FileOutputStream fileOutStream = null;
            
            if(file != null)
            {
                try{
                    fileOutStream = new FileOutputStream(file);
                }catch(FileNotFoundException fe){
                    label.setText("File Not Found");
                    return;
                }

                String content = textarea.getText();
                
                try{
                    fileOutStream.write(content.getBytes());
                }catch(IOException ioe){
                    label.setText("写入文件错误");
                }
                finally{
                    try{
                        if(fileOutStream != null)
                            fileOutStream.close();
                    }catch(IOException ioe2){}
                }
            }
        }
    }
