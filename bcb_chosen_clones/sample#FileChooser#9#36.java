	public static File chooseFileOpen(JFrame frame) {
		File retval;
		
		//Create and configure file chooser
		JFileChooser fc = new JFileChooser();
		fc.setDialogTitle("Select input file.");
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setMultiSelectionEnabled(false);
		
		//Show dialog and wait for user input
		int status = fc.showOpenDialog(frame);
		
		//React to input
		if(status == JFileChooser.APPROVE_OPTION) {
			retval = fc.getSelectedFile();
		} else if (status == JFileChooser.CANCEL_OPTION) {
			retval = null;
		} else {
			retval = null;
		}
		
		//Cleanup
		fc.setEnabled(false);
		fc.setVisible(false);
		
		//Return
		return retval;
	}
