    public boolean import_hints(String filename) {
        int pieceId;
        int i, col, row;
        int rotation;
        int number;
        boolean byurl = true;
        e2piece temppiece;
        String lineread;
        StringTokenizer tok;
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
            lineread = entree.readLine();
            tok = new StringTokenizer(lineread, " ");
            number = Integer.parseInt(tok.nextToken());
            for (i = 0; i < number; i++) {
                lineread = entree.readLine();
                if (lineread == null) {
                    break;
                }
                tok = new StringTokenizer(lineread, " ");
                pieceId = Integer.parseInt(tok.nextToken());
                col = Integer.parseInt(tok.nextToken()) - 1;
                row = Integer.parseInt(tok.nextToken()) - 1;
                rotation = Integer.parseInt(tok.nextToken());
                System.out.println("placing hint piece : " + pieceId);
                place_piece_at(pieceId, col, row, 0);
                temppiece = board.get_piece_at(col, row);
                temppiece.reset_rotation();
                temppiece.rotate(rotation);
                temppiece.set_as_hint();
            }
            return true;
        } catch (IOException err) {
            return false;
        }
    }
