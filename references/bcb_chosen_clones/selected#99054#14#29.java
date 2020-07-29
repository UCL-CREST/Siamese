    public RandomOrderIntCG(IntChoiceGenerator sub) {
        super(sub.id);
        setPreviousChoiceGenerator(sub.getPreviousChoiceGenerator());
        choices = new int[sub.getTotalNumberOfChoices()];
        for (int i = 0; i < choices.length; i++) {
            sub.advance();
            choices[i] = sub.getNextChoice();
        }
        for (int i = choices.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int tmp = choices[i];
            choices[i] = choices[j];
            choices[j] = tmp;
        }
        nextIdx = -1;
    }
