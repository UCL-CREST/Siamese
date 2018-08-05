	public void mouseClicked(MouseEvent e) {
		 if(e.getClickCount()==2){
			 BufferedImage bimg = robot.createScreenCapture(new Rectangle(head.x+2, head.y+2, width-3, height-3));;
				if (fcSave.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
				{
					File file = fcSave.getSelectedFile();
					StringBuilder path = new StringBuilder( file.getAbsolutePath().toLowerCase() );
					String formart = "png";
					javax.swing.filechooser.FileFilter ff = fcSave.getFileFilter();
					
					if(ff instanceof BMPfilter)
					{
						formart = "bmp";
						
					}else if(ff instanceof JPGfilter)
					{
						formart = "jpg";
						
					}else if(ff instanceof GIFfilter)
					{
						formart = "gif";
						
					}
					
					if (!path.toString().endsWith("."+formart))
					{
						path = path.append(".");
						path.append(formart);
						file = new File(path.toString());
					}
					
					try {
						ImageIO.write(bimg, formart.toUpperCase(), file);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					
					this.dispose();
				}
				else
				{
					this.dispose();
					/*
					this.setVisible(false);
					
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					
					this.setVisible(true);*/
				}
		 }
	}
