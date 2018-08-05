	public static void parseCSV(File file) throws IOException {
		StringTokenizer toks;
		BufferedReader in = new BufferedReader(new FileReader(file));
		String line;
		int lnum = 0;
		while((line = in.readLine()) != null) {
			lnum++;
			toks = new StringTokenizer(line, ",");
			int tnum = 0;
			while(toks.hasMoreTokens()) {
				tnum++;
				System.out.println("Line# " + lnum + " Token# " + tnum + ": " + toks.nextToken());
			}
		}
		in.close();
	}
