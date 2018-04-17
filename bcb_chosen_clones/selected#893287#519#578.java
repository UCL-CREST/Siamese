    private void fileCopy(File filename) throws IOException {
        if (this.stdOut) {
            this.fileDump(filename);
            return;
        }
        File source_file = new File(spoolPath + "/" + filename);
        File destination_file = new File(copyPath + "/" + filename);
        FileInputStream source = null;
        FileOutputStream destination = null;
        byte[] buffer;
        int bytes_read;
        try {
            if (!source_file.exists() || !source_file.isFile()) throw new FileCopyException("no such source file: " + source_file);
            if (!source_file.canRead()) throw new FileCopyException("source file is unreadable: " + source_file);
            if (destination_file.exists()) {
                if (destination_file.isFile()) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                    if (!destination_file.canWrite()) throw new FileCopyException("destination file is unwriteable: " + destination_file);
                    if (!this.overwrite) {
                        System.out.print("File " + destination_file + " already exists. Overwrite? (Y/N): ");
                        System.out.flush();
                        if (!in.readLine().toUpperCase().equals("Y")) throw new FileCopyException("copy cancelled.");
                    }
                } else throw new FileCopyException("destination is not a file: " + destination_file);
            } else {
                File parentdir = parent(destination_file);
                if (!parentdir.exists()) throw new FileCopyException("destination directory doesn't exist: " + destination_file);
                if (!parentdir.canWrite()) throw new FileCopyException("destination directory is unwriteable: " + destination_file);
            }
            source = new FileInputStream(source_file);
            destination = new FileOutputStream(destination_file);
            buffer = new byte[1024];
            while ((bytes_read = source.read(buffer)) != -1) {
                destination.write(buffer, 0, bytes_read);
            }
            System.out.println("File " + filename + " successfull copied to " + destination_file);
            if (this.keep == false && source_file.isFile()) {
                try {
                    source.close();
                } catch (Exception e) {
                }
                if (source_file.delete()) {
                    new File(this.spoolPath + "/info/" + filename + ".desc").delete();
                }
            }
        } finally {
            if (source != null) try {
                source.close();
            } catch (IOException e) {
            }
            if (destination != null) try {
                destination.flush();
            } catch (IOException e) {
            }
            if (destination != null) try {
                destination.close();
            } catch (IOException e) {
            }
        }
    }
