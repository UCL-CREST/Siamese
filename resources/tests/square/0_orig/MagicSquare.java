public class MagicSquare {
    private int[][] square;
    private boolean[] possible;
    private int totalSqs;
    private int sum;
    private int numsquares;
    public static void main ( String[] args ) {
        MagicSquare m = new MagicSquare ( 3 );
        m.fill ( 0, 0 );
        System.out.println ( "There were " + m.getTotalSolutions() + " number of solutions." );
    }
    public MagicSquare ( int n ) {
        square = new int[n][n];
        for ( int i = 0; i < n; i++ )
            for ( int j = 0; j < n; j++ ) {
                square[i][j] = 0;
            }
        totalSqs = n * n;
        possible = new boolean[totalSqs];
        for ( int i = 0; i < totalSqs; i++ ) {
            possible[i] = true;
        }
        sum = n * ( n * n + 1 ) / 2;
        numsquares = 0;
    }
    public void fill ( int row, int col ) {
        if ( !checkRows() || !checkCols() || !checkDiags() ) {
            return;
        }
        if ( row == square.length ) {
            System.out.println ( this );
            numsquares++;
            return;
        }
        for ( int i = 0; i < totalSqs; i++ ) {
            if ( possible[i] ) {
                square[row][col] = i + 1;
                possible[i] = false;
                int newcol = col + 1;
                int newrow = row;
                if ( newcol == square.length ) {
                    newrow++;
                    newcol = 0;
                }
                fill ( newrow, newcol );
                square[row][col] = 0;
                possible[i] = true;
            }
        }
    }
    public boolean checkRows() {
        for ( int i = 0; i < square.length; i++ ) {
            int test = 0;
            boolean unFilled = false;
            for ( int j = 0; j < square[i].length; j++ ) {
                test += square[i][j];
                if ( square[i][j] == 0 ) {
                    unFilled = true;
                }
            }
            if ( !unFilled && test != sum ) {
                return false;
            }
        }
        return true;
    }
    public boolean checkCols() {
        for ( int j = 0; j < square[0].length; j++ ) {
            int test = 0;
            boolean unFilled = false;
            for ( int i = 0; i < square.length; i++ ) {
                test += square[i][j];
                if ( square[i][j] == 0 ) {
                    unFilled = true;
                }
            }
            if ( !unFilled && test != sum ) {
                return false;
            }
        }
        return true;
    }
    public boolean checkDiags() {
        int test = 0;
        boolean unFilled = false;
        for ( int i = 0; i < square.length; i++ ) {
            test += square[i][i];
            if ( square[i][i] == 0 ) {
                unFilled = true;
            }
        }
        if ( !unFilled && test != sum ) {
            return false;
        }
        test = 0;
        unFilled = false;
        for ( int i = 0; i < square.length; i++ ) {
            test += square[i][square.length - 1 - i];
            if ( square[i][square.length - 1 - i] == 0 ) {
                unFilled = true;
            }
        }
        if ( !unFilled && test != sum ) {
            return false;
        }
        return true;
    }
    public int getTotalSolutions() {
        return numsquares;
    }
    public String toString() {
        String ans = "";
        for ( int i = 0; i < square.length; i++ ) {
            for ( int j = 0; j < square[i].length; j++ ) {
                ans = ans + square[i][j] + "\t";
            }
            ans = ans + "\n";
        }
        return ans;
    }
}
