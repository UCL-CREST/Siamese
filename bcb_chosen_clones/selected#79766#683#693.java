    public void transpose() {
        Matrix t = new Matrix(_column, _row);
        for (int row = 0; row < _row; row++) {
            for (int column = 0; column < _column; column++) {
                t._cell[column][row] = _cell[row][column];
            }
        }
        _row = t._row;
        _column = t._column;
        _cell = t._cell;
    }
