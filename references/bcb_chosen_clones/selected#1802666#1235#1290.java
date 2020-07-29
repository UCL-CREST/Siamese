    private void preprocessObjects(GeoObject[] objects) throws IOException {
        System.out.println("objects.length " + objects.length);
        for (int i = 0; i < objects.length; i++) {
            String fileName = objects[i].getPath();
            int dotindex = fileName.lastIndexOf(".");
            dotindex = dotindex < 0 ? 0 : dotindex;
            String tmp = dotindex < 1 ? fileName : fileName.substring(0, dotindex + 3) + "w";
            System.out.println("i: " + " world filename " + tmp);
            File worldFile = new File(tmp);
            if (worldFile.exists()) {
                BufferedReader worldFileReader = new BufferedReader(new InputStreamReader(new FileInputStream(worldFile)));
                if (staticDebugOn) debug("b4nextline: ");
                line = worldFileReader.readLine();
                if (staticDebugOn) debug("line: " + line);
                if (line != null) {
                    line = worldFileReader.readLine();
                    if (staticDebugOn) debug("line: " + line);
                    tokenizer = new StringTokenizer(line, " \n\t\r\"", false);
                    objects[i].setLon(Double.valueOf(tokenizer.nextToken()).doubleValue());
                    line = worldFileReader.readLine();
                    if (staticDebugOn) debug("line: " + line);
                    tokenizer = new StringTokenizer(line, " \n\t\r\"", false);
                    objects[i].setLat(Double.valueOf(tokenizer.nextToken()).doubleValue());
                }
            }
            File file = new File(objects[i].getPath());
            if (file.exists()) {
                System.out.println("object src file found ");
                int slashindex = fileName.lastIndexOf(java.io.File.separator);
                slashindex = slashindex < 0 ? 0 : slashindex;
                if (slashindex == 0) {
                    slashindex = fileName.lastIndexOf("/");
                    slashindex = slashindex < 0 ? 0 : slashindex;
                }
                tmp = slashindex < 1 ? fileName : fileName.substring(slashindex + 1, fileName.length());
                System.out.println("filename " + destinationDirectory + XPlat.fileSep + tmp);
                objects[i].setPath(tmp);
                file = new File(fileName);
                if (file.exists()) {
                    DataInputStream dataIn = new DataInputStream(new BufferedInputStream(new FileInputStream(fileName)));
                    DataOutputStream dataOut = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(destinationDirectory + XPlat.fileSep + tmp)));
                    System.out.println("copying to " + destinationDirectory + XPlat.fileSep + tmp);
                    for (; ; ) {
                        try {
                            dataOut.writeShort(dataIn.readShort());
                        } catch (EOFException e) {
                            break;
                        } catch (IOException e) {
                            break;
                        }
                    }
                    dataOut.close();
                }
            }
        }
    }
