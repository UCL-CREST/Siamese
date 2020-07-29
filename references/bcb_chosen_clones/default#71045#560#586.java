    public void run() {
        Method[] methods = this.getClass().getMethods();
        int countTestCases = 0;
        int countSuccessTestCases = 0;
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].getName().length() > 4) {
                if (methods[i].getName().substring(0, 4).equals("Test")) {
                    countTestCases++;
                    try {
                        if ((Boolean) (methods[i].invoke(this, methods[i].getName().substring(4)))) {
                            countSuccessTestCases++;
                        }
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        System.out.println("------------------------");
        System.out.println("Testcases: " + countTestCases);
        System.out.println("Success  : " + countSuccessTestCases);
        System.out.println("Failure  : " + (countTestCases - countSuccessTestCases));
    }
