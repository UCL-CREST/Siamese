    public void gzipCompress(String file) {
        try {
            File inputFile = new File(file);
            FileInputStream fileinput = new FileInputStream(inputFile);
            File outputFile = new File(file.substring(0, file.length() - 1) + "z");
            FileOutputStream stream = new FileOutputStream(outputFile);
            GZIPOutputStream gzipstream = new GZIPOutputStream(stream);
            BufferedInputStream bis = new BufferedInputStream(fileinput);
            int bytes_read = 0;
            byte[] buf = new byte[READ_BUFFER_SIZE];
            while ((bytes_read = bis.read(buf, 0, BLOCK_SIZE)) != -1) {
                gzipstream.write(buf, 0, bytes_read);
            }
            bis.close();
            inputFile.delete();
            gzipstream.finish();
            gzipstream.close();
        } catch (FileNotFoundException fnfe) {
            System.out.println("Compressor: Cannot find file " + fnfe.getMessage());
        } catch (SecurityException se) {
            System.out.println("Problem saving file " + se.getMessage());
        } catch (IOException ioe) {
            System.out.println("Problem saving file " + ioe.getMessage());
        }
    }
