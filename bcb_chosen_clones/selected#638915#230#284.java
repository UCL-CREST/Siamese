    private void weightAndPlaceClasses() {
        int rows = getRows();
        for (int curRow = _maxPackageRank; curRow < rows; curRow++) {
            xPos = getHGap() / 2;
            BOTLRuleDiagramNode[] rowObject = getObjectsInRow(curRow);
            for (int i = 0; i < rowObject.length; i++) {
                if (curRow == _maxPackageRank) {
                    int nDownlinks = rowObject[i].getDownlinks().size();
                    rowObject[i].setWeight((nDownlinks > 0) ? (1 / nDownlinks) : 2);
                } else {
                    Vector uplinks = rowObject[i].getUplinks();
                    int nUplinks = uplinks.size();
                    if (nUplinks > 0) {
                        float average_col = 0;
                        for (int j = 0; j < uplinks.size(); j++) {
                            average_col += ((BOTLRuleDiagramNode) (uplinks.elementAt(j))).getColumn();
                        }
                        average_col /= nUplinks;
                        rowObject[i].setWeight(average_col);
                    } else {
                        rowObject[i].setWeight(1000);
                    }
                }
            }
            int[] pos = new int[rowObject.length];
            for (int i = 0; i < pos.length; i++) {
                pos[i] = i;
            }
            boolean swapped = true;
            while (swapped) {
                swapped = false;
                for (int i = 0; i < pos.length - 1; i++) {
                    if (rowObject[pos[i]].getWeight() > rowObject[pos[i + 1]].getWeight()) {
                        int temp = pos[i];
                        pos[i] = pos[i + 1];
                        pos[i + 1] = temp;
                        swapped = true;
                    }
                }
            }
            for (int i = 0; i < pos.length; i++) {
                rowObject[pos[i]].setColumn(i);
                if ((i > _vMax) && (rowObject[pos[i]].getUplinks().size() == 0) && (rowObject[pos[i]].getDownlinks().size() == 0)) {
                    if (getColumns(rows - 1) > _vMax) {
                        rows++;
                    }
                    rowObject[pos[i]].setRank(rows - 1);
                } else {
                    rowObject[pos[i]].setLocation(new Point(xPos, yPos));
                    xPos += rowObject[pos[i]].getSize().getWidth() + getHGap();
                }
            }
            yPos += getRowHeight(curRow) + getVGap();
        }
    }
