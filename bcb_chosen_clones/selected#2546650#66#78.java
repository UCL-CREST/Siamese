    public int NthLowestSkill(int n) {
        int[] skillIds = new int[] { 0, 1, 2, 3 };
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 3 - j; i++) {
                if (Skills()[skillIds[i]] > Skills()[skillIds[i + 1]]) {
                    int temp = skillIds[i];
                    skillIds[i] = skillIds[i + 1];
                    skillIds[i + 1] = temp;
                }
            }
        }
        return skillIds[n - 1];
    }
