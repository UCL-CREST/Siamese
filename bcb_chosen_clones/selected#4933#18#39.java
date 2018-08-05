    public ValueTableComponent(int[][] pValues, ValueValidator pValidator, boolean[][] pSelection) {
        super();
        aValues = pValues;
        aValidator = pValidator;
        aSelection = pSelection;
        addMouseListener(new CellMouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent pEvent) {
                if (isInCell(pEvent.getX(), pEvent.getY())) {
                    int lHorizontalCellIndex, lVerticalCellIndex;
                    lHorizontalCellIndex = getCellIndex(pEvent.getX());
                    lVerticalCellIndex = getCellIndex(pEvent.getY());
                    if (lHorizontalCellIndex < aValues.length && lVerticalCellIndex < aValues.length) {
                        aSelection[lHorizontalCellIndex][lVerticalCellIndex] = !aSelection[lHorizontalCellIndex][lVerticalCellIndex];
                        aSelection[lVerticalCellIndex][lHorizontalCellIndex] = aSelection[lHorizontalCellIndex][lVerticalCellIndex];
                        repaint();
                    }
                }
            }
        });
    }
