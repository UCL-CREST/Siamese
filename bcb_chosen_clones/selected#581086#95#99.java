    private int calculateSpeed() {
        int weight = (punchPower + kickPower) / 2;
        int height = (punchReach + kickReach) / 2;
        return (int) Math.round(Math.abs(0.5 * (height - weight)));
    }
