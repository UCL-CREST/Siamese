		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == button1)
			{
				System.out.println("Just capture the screen using a robot");
				frame.setVisible(false);
				Rectangle bounds = new Rectangle (0, 0, mode.getWidth (), mode.getHeight ());    
				
				BufferedImage bimg = robot.createScreenCapture(bounds);
								
				String fileName = String.valueOf(System.currentTimeMillis())+".png";
				File outputFile = new File(fileName);
				try 
				{
					ImageIO.write(bimg, "png", outputFile);
				} 
				catch (IOException e1) 
				{
					e1.printStackTrace();
				}
				frame.setVisible(true);
				System.out.println("File save as "+fileName);
				
			}
			else if(e.getSource() == button2)
			{
				System.out.println("Create a full screen frame");
				TranslucentFrame tframe = new TranslucentFrame();

				tframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				tframe.setUndecorated(true);

				tframe.setSize(mode.getWidth(), mode.getHeight());
				
				if(!tframe.isShowing())
				{
					frame.setVisible(false);
					
					try {
						Thread.sleep(500);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					tframe.updateBackground();
					tframe.setVisible(true);
					
					
					frame.setVisible(true);
					frame.toBack();
					
				}
				else
				{
					tframe.setVisible(true);
				}
			}
			else if(e.getSource() == button3)
			{
				frame.dispose();
				System.exit(0);
			}
			else if(e.getSource() == button4)
			{
			  
			        JOptionPane.showMessageDialog(frame, 
			                "A small tool to snap the screen.\r\n\r\n"
			                + "Version "+ MenuFrame.VERSION + "\r\nMade in March 2007 by Hao Ji.\r\n"
			                + " Added resize selected area feature June 2008.\r\n" 
			                + " Added save as *.BMP|*.GIF|*.JPG|*.PNG feature June 2008.\r\n"
			                + "\r\n"
			                + "<html><i>Resize selected area feature refer to 千里冰封</i></html>\r\n"
			                + "\r\n"
			                + "Contact me: jacky.jihao@gmail.com \r\n",
			                "About MyScreenSnap", 
			                JOptionPane.INFORMATION_MESSAGE);
			  
			}
			
		}
