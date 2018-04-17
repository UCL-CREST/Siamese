    public void actionPerformed(ActionEvent e)
    {
        File file = null;
        int result = fileChooser.showOpenDialog(f);
        
        textarea.setText("");

        if (result == JFileChooser.APPROVE_OPTION)
        {
            file = fileChooser.getSelectedFile();
            label.setText("您选择的文件名称为："+file.getName());
        }
        else if(result == JFileChooser.CANCEL_OPTION)
        {
            label.setText("您没有选择任何文件");
        }
        
        FileInputStream inputStream;
        
        if(file != null)
        {
            try{
                inputStream = new FileInputStream(file);
            }catch(FileNotFoundException fe){
                label.setText("File Not Found");
                return;
            }
            
            ProgressMonitorInputStream pmInputStream = new 
                ProgressMonitorInputStream(f,      //parant component
                                   "Get File Content.....", //message
                                   inputStream);       //input stream
            
            ProgressMonitor pMonitor = 
                pmInputStream.getProgressMonitor();
            pMonitor.setMillisToDecideToPopup(10);
            pMonitor.setMillisToPopup(0);
            int readbyte;
    
            try{
                while( (readbyte = pmInputStream.read()) != -1)
                {
                    textarea.append(String.valueOf((char)readbyte));

                    try{
                        Thread.sleep(10);
                    }catch(InterruptedException ie){}
                    
                    if(pMonitor.isCanceled())
                    {
                        textarea.append("\n\n读取文件中断！！");
                    }
                }
            }catch(IOException ioe){
                label.setText("读取文件错误");
            }
            finally{
                try{
                    if(pmInputStream != null)
                        pmInputStream.close();
                }catch(IOException ioe2){}
            }
        }
    }
