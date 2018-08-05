    public void copy(File aSource, File aDestDir) throws IOException {
        FileInputStream myInFile = new FileInputStream(aSource);
        FileOutputStream myOutFile = new FileOutputStream(new File(aDestDir, aSource.getName()));
        FileChannel myIn = myInFile.getChannel();
        FileChannel myOut = myOutFile.getChannel();
        boolean end = false;
        while (true) {
            int myBytes = myIn.read(theBuffer);
            if (myBytes != -1) {
                theBuffer.flip();
                myOut.write(theBuffer);
                theBuffer.clear();
            } else break;
        }
        myIn.close();
        myOut.close();
        myInFile.close();
        myOutFile.close();
        long myEnd = System.currentTimeMillis();
    }
