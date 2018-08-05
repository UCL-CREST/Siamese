        private double searchMax(double left, double delta, int depth) {
            double right = left + delta;
            mmVar.setVal(left);
            double leftVal = mmExpr.getVal();
            boolean incrLeft = mmDeriv.getVal() > 0;
            mmVar.setVal(right);
            double rightVal = mmExpr.getVal();
            boolean incrRight = mmDeriv.getVal() > 0;
            if (incrLeft && !incrRight) {
                double mid = (left + right) / 2;
                if (depth >= 13) {
                    mmVar.setVal(mid);
                    return mmExpr.getVal();
                }
                double slope = mmDeriv.getVal();
                if (slope > 0) return searchMax(mid, right, depth + 1); else return searchMax(left, mid, depth + 1);
            }
            return Math.max(leftVal, rightVal);
        }
