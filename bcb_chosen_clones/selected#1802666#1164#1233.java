    private void preprocessImages(GeoImage[] detailedImages) throws IOException {
        for (int i = 0; i < detailedImages.length; i++) {
            BufferedImage img = loadImage(detailedImages[i].getPath());
            detailedImages[i].setLatDim(img.getHeight());
            detailedImages[i].setLonDim(img.getWidth());
            freeImage(img);
            String fileName = detailedImages[i].getPath();
            int dotindex = fileName.lastIndexOf(".");
            dotindex = dotindex < 0 ? 0 : dotindex;
            String tmp = dotindex < 1 ? fileName : fileName.substring(0, dotindex + 3) + "w";
            System.out.println("filename " + tmp);
            File worldFile = new File(tmp);
            if (!worldFile.exists()) {
                System.out.println("Rez: Could not find file: " + tmp);
                debug("Rez: Could not find directory: " + tmp);
                throw new IOException("File not Found");
            }
            BufferedReader worldFileReader = new BufferedReader(new InputStreamReader(new FileInputStream(worldFile)));
            if (staticDebugOn) debug("b4nextline: ");
            line = worldFileReader.readLine();
            if (staticDebugOn) debug("line: " + line);
            if (line != null) {
                tokenizer = new StringTokenizer(line, " \n\t\r\"", false);
                detailedImages[i].setLonSpacing(Double.valueOf(tokenizer.nextToken()).doubleValue());
                detailedImages[i].setLonExtent(detailedImages[i].getLonSpacing() * ((double) detailedImages[i].getLonDim() - 1d));
                System.out.println("setLonExtent " + detailedImages[i].getLonExtent());
                line = worldFileReader.readLine();
                if (staticDebugOn) debug("skip line: " + line);
                line = worldFileReader.readLine();
                if (staticDebugOn) debug("skip line: " + line);
                line = worldFileReader.readLine();
                if (staticDebugOn) debug("line: " + line);
                tokenizer = new StringTokenizer(line, " \n\t\r\"", false);
                detailedImages[i].setLatSpacing(Double.valueOf(tokenizer.nextToken()).doubleValue());
                detailedImages[i].setLatExtent(detailedImages[i].getLatSpacing() * ((double) detailedImages[i].getLatDim() - 1d));
                line = worldFileReader.readLine();
                if (staticDebugOn) debug("line: " + line);
                tokenizer = new StringTokenizer(line, " \n\t\r\"", false);
                detailedImages[i].setLon(Double.valueOf(tokenizer.nextToken()).doubleValue());
                line = worldFileReader.readLine();
                if (staticDebugOn) debug("line: " + line);
                tokenizer = new StringTokenizer(line, " \n\t\r\"", false);
                detailedImages[i].setLat(Double.valueOf(tokenizer.nextToken()).doubleValue());
                int slashindex = fileName.lastIndexOf(java.io.File.separator);
                slashindex = slashindex < 0 ? 0 : slashindex;
                if (slashindex == 0) {
                    slashindex = fileName.lastIndexOf("/");
                    slashindex = slashindex < 0 ? 0 : slashindex;
                }
                tmp = slashindex < 1 ? fileName : fileName.substring(slashindex + 1, fileName.length());
                System.out.println("filename " + destinationDirectory + XPlat.fileSep + tmp);
                detailedImages[i].setPath(tmp);
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
            } else {
                System.out.println("Rez: ERROR: World file for image is null");
            }
        }
    }
