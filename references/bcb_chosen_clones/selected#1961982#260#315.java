    public void afterCompose() {
        timer = new Timer(500);
        appendChild(timer);
        timer.setRepeats(true);
        timer.addEventListener(Events.ON_TIMER, new EventListener() {

            public void onEvent(Event event) {
                updateRobotMsg();
            }
        });
        timer.start();
        if (getCaption() == null) {
            Component before = null;
            if (!getChildren().isEmpty()) {
                before = (Component) getChildren().get(0);
            }
            insertBefore(new Caption(), before);
        }
        if (!getCaption().isImageAssigned()) {
            hasImage = true;
            getCaption().setImage("~./zk/img/progress.gif");
        }
        if (getTask() == null && taskClass != null) {
            try {
                Class<?> tclass = getPage().getZScriptClass(taskClass);
                if (tclass == null) {
                    tclass = Thread.currentThread().getContextClassLoader().loadClass(taskClass);
                }
                setTask((WebBotTask) tclass.newInstance());
            } catch (Exception e) {
                student.web.internal.Exceptions.addSimpleExceptionGrid(this, e, "In &lt;robot-viewer&gt; tag, creating robot task using " + "\"taskClass\" attribute", false);
                robotErrMsg = BAD_TASK_CLASS_MSG;
            }
        } else if (getRobot() == null && robotClass != null) {
            try {
                Class<?> rclass = getPage().getZScriptClass(robotClass);
                if (rclass == null) {
                    rclass = Thread.currentThread().getContextClassLoader().loadClass(robotClass);
                } else if (robotUrl == null) {
                    setRobot((WebBot) rclass.newInstance());
                } else {
                    setRobot((WebBot) rclass.getConstructor(String.class).newInstance(robotUrl));
                }
            } catch (Exception e) {
                student.web.internal.Exceptions.addSimpleExceptionGrid(this, e, "In &lt;robot-viewer&gt; tag, creating robot using " + "\"robotClass\" attribute", false);
                robotErrMsg = BAD_ROBOT_CLASS_MSG;
            }
        }
        runner = new RobotRunner();
        runner.start();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
        }
        updateRobotMsg();
    }
