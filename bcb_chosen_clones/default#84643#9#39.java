    public static void main(String[] args) {
        System.out.println("Wecome to SoDoKu Solver 0.1");
        try {
            BufferedReader br = new BufferedReader(new FileReader("sodoku.txt"));
            PrintWriter outfile = new PrintWriter(new FileWriter("result.txt"));
            String line = br.readLine();
            while (line != null) {
                int puzzleSize = 0;
                int boxSize = 0;
                String strFEN = "";
                StringTokenizer st = new StringTokenizer(line);
                if (st.hasMoreTokens()) {
                    puzzleSize = Integer.valueOf(st.nextToken());
                    boxSize = Integer.valueOf(st.nextToken());
                    strFEN = st.nextToken();
                }
                SoDoKuPuzzle puzzle = new SoDoKuPuzzle(puzzleSize, boxSize);
                ConstraintSet cs = new ConstraintSet(puzzle.getNumSquares());
                GenerateConstraints(puzzle, cs);
                boolean doArc = false;
                SolvePuzzle(outfile, puzzle, cs, doArc, Search.SearchAlg.cspBT, strFEN);
                doArc = true;
                line = br.readLine();
            }
            outfile.close();
            br.close();
        } catch (IOException ioe) {
            System.out.println("IO error reading input file.");
            System.exit(1);
        }
    }
