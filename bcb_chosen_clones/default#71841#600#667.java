    public void constructAssociationView() {
        String className;
        String methodName;
        String field;
        boolean foundRead = false;
        boolean foundWrite = false;
        boolean classWritten = false;
        try {
            AssocView = new BufferedWriter(new FileWriter("InfoFiles/AssociationView.txt"));
            FileInputStream fstreamPC = new FileInputStream("InfoFiles/PrincipleClassGroup.txt");
            DataInputStream inPC = new DataInputStream(fstreamPC);
            BufferedReader PC = new BufferedReader(new InputStreamReader(inPC));
            while ((field = PC.readLine()) != null) {
                className = field;
                AssocView.write(className);
                AssocView.newLine();
                classWritten = true;
                while ((methodName = PC.readLine()) != null) {
                    if (methodName.contentEquals("EndOfClass")) break;
                    AssocView.write("StartOfMethod");
                    AssocView.newLine();
                    AssocView.write(methodName);
                    AssocView.newLine();
                    for (int i = 0; i < readFileCount && foundRead == false; i++) {
                        if (methodName.compareTo(readArray[i]) == 0) {
                            foundRead = true;
                            for (int j = 1; readArray[i + j].compareTo("EndOfMethod") != 0; j++) {
                                if (readArray[i + j].indexOf(".") > 0) {
                                    field = readArray[i + j].substring(0, readArray[i + j].indexOf("."));
                                    if (isPrincipleClass(field) == true) {
                                        AssocView.write(readArray[i + j]);
                                        AssocView.newLine();
                                    }
                                }
                            }
                        }
                    }
                    for (int i = 0; i < writeFileCount && foundWrite == false; i++) {
                        if (methodName.compareTo(writeArray[i]) == 0) {
                            foundWrite = true;
                            for (int j = 1; writeArray[i + j].compareTo("EndOfMethod") != 0; j++) {
                                if (writeArray[i + j].indexOf(".") > 0) {
                                    field = writeArray[i + j].substring(0, writeArray[i + j].indexOf("."));
                                    if (isPrincipleClass(field) == true) {
                                        AssocView.write(writeArray[i + j]);
                                        AssocView.newLine();
                                    }
                                }
                            }
                        }
                    }
                    AssocView.write("EndOfMethod");
                    AssocView.newLine();
                    foundRead = false;
                    foundWrite = false;
                }
                if (classWritten == true) {
                    AssocView.write("EndOfClass");
                    AssocView.newLine();
                    classWritten = false;
                }
            }
            PC.close();
            AssocView.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
