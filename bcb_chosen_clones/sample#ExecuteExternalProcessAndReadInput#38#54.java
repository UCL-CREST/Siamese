	public static void execute2() throws IOException, InterruptedException {
		// Get Runtime
		Runtime rt = Runtime.getRuntime();
		
		//Execute Process
		Process p = rt.exec("myCommand");
		
		//Redirect external process stdout to this program's stdout
		BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
		String line = null;
		while((line = in.readLine()) != null)
			System.out.println(line);
		
		//Wait for process to complete and cleanup
		p.waitFor();
		p.destroy();
	}
