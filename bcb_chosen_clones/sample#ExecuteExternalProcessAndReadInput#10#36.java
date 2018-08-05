	public static void execute1() throws IOException, InterruptedException {
		//Create Process Builder with Command and Arguments
		ProcessBuilder pb = new ProcessBuilder("myCommand", "myArg1", "myArg2");
		
		//Setup Execution Environment
		Map<String, String> env = pb.environment();
		env.put("VAR1", "myValue");
		env.remove("OTHERVAR");
		env.put("VAR2", env.get("VAR1") + "suffix");
		
		//Setup execution directory
		pb.directory(new File("myDir"));
		
		//Handle Output Streams
		File log = new File("log");
		pb.redirectErrorStream(true);
		pb.redirectOutput(Redirect.appendTo(log));
		
		// Start Process
		Process p = pb.start();
		
		//Wait for process to complete
		p.waitFor();
		
		//Cleanup
		p.destroy();
	}
