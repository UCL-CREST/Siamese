    public boolean import_pieces(String filename) {
        int pieceId;
        int i;
        int n;
        int[] color;
        boolean byurl = true;
        e2piece temppiece;
        color = new int[4];
        BufferedReader entree;
        try {
            if (byurl == true) {
                URL url = new URL(baseURL, filename);
                InputStream in = url.openStream();
                entree = new BufferedReader(new InputStreamReader(in));
            } else {
                entree = new BufferedReader(new FileReader(filename));
            }
            pieceId = 0;
            while (true) {
                String lineread = entree.readLine();
                if (lineread == null) {
                    break;
                }
                StringTokenizer tok = new StringTokenizer(lineread, " ");
                n = tok.countTokens();
                if (n == 2) {
                } else {
                    for (i = 0; i < 4; i++) {
                        color[i] = Integer.parseInt(tok.nextToken());
                    }
                    pieceId++;
                    System.out.println("Read Piece : " + pieceId + ":" + color[0] + " " + color[1] + " " + color[2] + " " + color[3]);
                    temppiece = new e2piece(pieceId, color[0] + 1, color[1] + 1, color[2] + 1, color[3] + 1);
                    allpieces.add_piece(temppiece);
                    unplacedpieces.add_piece(temppiece);
                }
            }
            return true;
        } catch (IOException err) {
            return false;
        }
    }
