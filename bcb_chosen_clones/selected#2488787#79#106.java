    public boolean compressIt() {
        try {
            File f = new File(this.outFileName);
            f.createNewFile();
            f.deleteOnExit();
            ZipOutputStream outStream = new ZipOutputStream(new FileOutputStream(this.outFileName));
            System.out.println("Creating compressed file " + this.outFileName + "...");
            for (int i = 0; i < this.fileNamesToCompress.length; i++) {
                System.out.print("\tAdding " + this.fileNamesToCompress[i] + "... ");
                FileInputStream inStream = new FileInputStream(this.directory + File.separator + this.fileNamesToCompress[i]);
                outStream.putNextEntry(new ZipEntry(this.fileNamesToCompress[i]));
                Integer auxLength;
                while ((auxLength = inStream.read(this.buffer)) > 0) {
                    outStream.write(this.buffer, 0, auxLength);
                }
                inStream.close();
                System.out.println("DONE.");
            }
            outStream.close();
        } catch (java.io.FileNotFoundException ex) {
            System.out.println("ERROR :: File NOT found!!! " + ex.getMessage());
            return false;
        } catch (IOException ex) {
            System.out.println("ERRROR :: Input/Ouput error!!! " + ex.getMessage());
            return false;
        }
        return true;
    }
