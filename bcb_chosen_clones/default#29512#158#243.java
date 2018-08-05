    public void loadUnistrok(String filename) throws IOException {
        unistrokFilename = filename;
        BufferedReader reader = new BufferedReader(new FileReader(unistrokFilename));
        String line;
        boolean simplified = false;
        while ((line = reader.readLine()) != null) {
            Character currentCharacter = new Character();
            if (line.length() == 0) {
                continue;
            }
            if (line.charAt(0) == '#') {
                if (line.toLowerCase().contains("traditional")) {
                    simplified = false;
                } else if (line.toLowerCase().contains("simplified")) {
                    simplified = true;
                }
                continue;
            }
            int pipe;
            String unicode = line.substring(0, line.indexOf(' '));
            line = line.substring(line.indexOf(" ") + 1);
            if (line.indexOf(" ") < 0) {
                continue;
            }
            line = line.substring(line.indexOf(" "));
            pipe = line.indexOf('|');
            if (pipe == -1) {
                continue;
            }
            if (simplified) {
                currentCharacter.simplified = (char) Integer.parseInt(unicode, 16);
            } else {
                currentCharacter.traditional = (char) Integer.parseInt(unicode, 16);
            }
            currentCharacter = this.getCharacter(currentCharacter);
            if (currentCharacter.strokes.size() > 0 && (currentCharacter.simplified == currentCharacter.traditional)) {
                continue;
            }
            line = line.substring(pipe + 1);
            String tokline, argline;
            int tokindex = line.indexOf('|');
            if (tokindex != -1) {
                tokline = line.substring(0, tokindex);
                argline = line.substring(tokindex + 1);
            } else {
                argline = null;
                tokline = line;
            }
            StringTokenizer st = new StringTokenizer(tokline);
            WhileLoop: while (st.hasMoreTokens()) {
                String tok = st.nextToken();
                for (int i = 0; i < tok.length(); i++) {
                    switch(tok.charAt(i)) {
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            char c = tok.charAt(i);
                            currentCharacter.addStroke(tok.charAt(i) - '0', Double.MAX_VALUE);
                            break;
                        case 'b':
                            currentCharacter.addStroke(62, Double.MAX_VALUE);
                            break;
                        case 'c':
                            currentCharacter.addStroke(26, Double.MAX_VALUE);
                            break;
                        case 'x':
                            currentCharacter.addStroke(21, Double.MAX_VALUE);
                            break;
                        case 'y':
                            currentCharacter.addStroke(23, Double.MAX_VALUE);
                            break;
                        case '|':
                            break WhileLoop;
                        default:
                            continue;
                    }
                }
            }
        }
        reader.close();
    }
