    public void setGrid(AccumulatorDefinition ad) {
        int measure_metric_column_count = 0;
        Vector<MeasureDefinition> measures_list = null;
        if (ad != null) {
            measure_metric_column_count = ad.getMeasureMetricColumnCount();
            measures_list = ad.getMeasureDefinitions();
        }
        if (measures_on_this_axis) {
            if (ad.isMultiMetric() && ad.isMultiMeasure()) axis_grid = new String[slices.size() + 2][getColumnCount(measure_metric_column_count)]; else if (ad.isMultiMeasure() || ad.isMultiMetric()) axis_grid = new String[slices.size() + 1][getColumnCount(measure_metric_column_count)]; else axis_grid = new String[slices.size()][getColumnCount(measure_metric_column_count)];
        } else axis_grid = new String[slices.size()][getColumnCount(1)];
        int current_slice = 0;
        for (int i = 0; i < first_slice.sub_slice.size(); i++) {
            Slice slice = first_slice.sub_slice.get(i);
            Vector<String> grid_slice_values = new Vector<String>();
            if ((measures_on_this_axis) && (ad.isMultiMeasure() || ad.isMultiMetric())) current_slice = slice.getGridSlice(grid_slice_values, current_slice, axis_grid, ad); else current_slice = slice.getGridSlice(grid_slice_values, current_slice, axis_grid, null);
        }
        if (!is_horizontal) {
            String[][] rotated_grid;
            if (measures_on_this_axis) {
                if (ad.isMultiMeasure() && ad.isMultiMetric()) rotated_grid = new String[getColumnCount(measure_metric_column_count)][slices.size() + 2]; else rotated_grid = new String[getColumnCount(measure_metric_column_count)][slices.size() + 1];
            } else rotated_grid = new String[getColumnCount(1)][slices.size()];
            for (int i = 0; i < axis_grid.length; i++) {
                for (int j = 0; j < axis_grid[i].length; j++) {
                    rotated_grid[j][i] = axis_grid[i][j];
                }
            }
            axis_grid = rotated_grid;
        }
    }
