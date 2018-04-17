    protected void setModel(final PlateModel model) {
        if (model == null) {
            this.model = null;
            this.solution = null;
            return;
        }
        this.model = model;
        PlateCoords coords;
        Integer val;
        String plate = null;
        byte[][] data = new byte[PlateModel.LINE_NUMBER][PlateModel.COLUMN_NUMBER];
        int boxh = (int) Math.sqrt(PlateModel.COLUMN_NUMBER);
        int boxl = (int) Math.sqrt(PlateModel.LINE_NUMBER);
        Grid grid = new Grid(boxh, boxl);
        for (int l = 0, c = 0; l < PlateModel.LINE_NUMBER; l++) {
            for (; c < PlateModel.COLUMN_NUMBER; c++) {
                coords = new PlateCoords(c, l);
                val = this.model.getValue(coords);
                data[l][c] = (val == null) ? 0 : (byte) val.intValue();
            }
            c = 0;
        }
        plate = SuDokuUtils.toString(data, boxh, PlateModel.COLUMN_NUMBER * PlateModel.LINE_NUMBER, SuDokuUtils.NUMERIC);
        grid.populate(plate);
        LeastCandidatesHybrid strat = new LeastCandidatesHybrid(true, true);
        int sol = -1;
        int total = PlateModel.LINE_NUMBER * PlateModel.COLUMN_NUMBER;
        while (true) {
            sol = grid.solve(strat, 128);
            if (grid.countFilledCells() == total) {
                break;
            } else {
                grid.populate(plate);
            }
        }
        if (sol > 1) {
            System.err.println("Multiple solution");
        }
        SuDokuUtils.populate(data, grid.toString());
        this.solution = new byte[PlateModel.COLUMN_NUMBER][PlateModel.LINE_NUMBER];
        for (int l = 0; l < PlateModel.LINE_NUMBER; l++) {
            for (int c = 0; c < PlateModel.COLUMN_NUMBER; c++) {
                this.solution[c][l] = data[l][c];
            }
        }
        this.strategyManager.assertModel(this.model);
    }
