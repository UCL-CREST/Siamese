    public ColorCombinationsComponent(Color[] pColors, boolean[][] pSelection) {
        super();
        aColors = pColors;
        aSelection = pSelection;
        addMouseListener(new CellMouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent pEvent) {
                if (isInCell(pEvent.getX(), pEvent.getY())) {
                    int lHorizontalCellIndex, lVerticalCellIndex;
                    lHorizontalCellIndex = getCellIndex(pEvent.getX());
                    lVerticalCellIndex = getCellIndex(pEvent.getY());
                    if (lHorizontalCellIndex < aColors.length && lVerticalCellIndex < aColors.length) {
                        aSelection[lHorizontalCellIndex][lVerticalCellIndex] = !aSelection[lHorizontalCellIndex][lVerticalCellIndex];
                        aSelection[lVerticalCellIndex][lHorizontalCellIndex] = aSelection[lHorizontalCellIndex][lVerticalCellIndex];
                        repaint();
                    }
                }
            }
        });
    }
