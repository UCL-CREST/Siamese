	public static void unzip2(File zipfile, File outputdir) throws IOException {
		//Buffer for extracting files
		byte[] buffer = new byte[1024];
				
		//Zip file
		ZipFile zip = new ZipFile(zipfile);
				
		//Get entries
		Enumeration<ZipArchiveEntry> files = zip.getEntries();
				
		//Iterate through the entries
		while(files.hasMoreElements()) {
			//Get entry
			ZipArchiveEntry ze = files.nextElement();
					
			//Resolve entry file
			File newFile = new File(outputdir + File.separator + ze.getName());
					
			//Make parent directories
			newFile.getParentFile().mkdirs();
					
			if(ze.isDirectory()) { //If directory, create it
				newFile.mkdir();
			} else { //If file, extract it
				InputStream is = zip.getInputStream(ze);
				FileOutputStream fos = new FileOutputStream(newFile);
				int len;
				while((len = is.read(buffer)) > 0) {
					fos.write(buffer, 0, len);
				}
				fos.close();
				is.close();
			}	
		}
				
		//Cleanup
		zip.close();
	}
