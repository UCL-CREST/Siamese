    void createAgentProgram(PrintStream ps, String agentName, String agentID) throws IOException, ConfigException {
        Agent agent = service.getAgent(agentName);
        File instanceDir = getInstanceDir();
        double agent_timeout_seconds = engine.getTimeout();
        String ii = "\"";
        String agentScript = "agent-" + agentName + ".py";
        String serviceScript = "service.py";
        Set<String> taskNames = agent.getTasks().keySet();
        File scriptDir = getService().getScriptDir();
        final boolean embedScripts = true;
        {
            ps.println("#!/bin/sh");
            ps.println("umask 0077");
        }
        for (String taskName : taskNames) {
            Task task = getService().getTask(taskName);
            String script = task.getScriptContent();
            if (embedScripts) {
                ps.println();
                ps.println("cat > " + task.getFile() + " << " + ii + "EOF_" + task.getFile() + ii);
                ps.println(script);
                ps.println("EOF_" + task.getFile());
            } else {
                File taskScriptFile = new File(instanceDir, task.getFile());
                PrintStream xs = new PrintStream(taskScriptFile);
                xs.print(script);
                xs.close();
            }
        }
        {
            PrintStream ss = null;
            if (embedScripts) {
                ps.println();
                ps.println("cat > " + serviceScript + " << " + ii + "EOF_" + serviceScript + ii);
                ss = ps;
            } else {
                File serviceScriptFile = new File(instanceDir, serviceScript);
                ss = new PrintStream(serviceScriptFile);
            }
            service.createServiceScript(ss);
            if (embedScripts) ps.println("EOF_" + serviceScript); else ss.close();
        }
        {
            URL url = new URL(engine.getAgentContextURL() + "/" + engine.getAgentURLPath());
            log.debug("url for agent = " + url.toExternalForm());
            PrintStream as = null;
            if (embedScripts) {
                ps.println();
                ps.println("cat > " + agentScript + " << " + ii + "EOF_" + agentScript + ii);
                as = ps;
            } else {
                File agentScriptFile = new File(instanceDir, agentScript);
                as = new PrintStream(agentScriptFile);
            }
            String tab[] = { "", "    ", "        ", "            ", "                ", "                    " };
            int i = 0;
            as.println(tab[i] + "from wrapper import ZSIWrapper");
            as.println(tab[i] + "from service import Service");
            as.println(tab[i] + "import threading");
            as.println(tab[i] + "from sys import exit");
            as.println();
            as.println(tab[i] + "URL=" + ii + url.toString() + ii);
            as.println(tab[i] + "agentID=" + ii + agentID + ii);
            as.println(tab[i] + "instanceID=" + instanceID);
            as.println(tab[i] + "instanceKey=" + ii + key + ii);
            as.println(tab[i] + "timeout=" + ii + agent_timeout_seconds + ii);
            as.println(tab[i] + "stop_flag=0");
            as.println(tab[i] + "kill_flag=0");
            as.println();
            as.println(tab[i] + "# Need extra port for each thread, " + "ZSIWrapper (actually ZSI) not thread save");
            as.println(tab[i] + "port0=ZSIWrapper(URL, agentID, instanceID, instanceKey, timeout)");
            as.println(tab[i] + "port0.setAgentStatus(" + Status.RUNNING.ordinal() + ")");
            as.println();
            as.println(tab[i] + "def create_namespace(service) :");
            as.println(tab[i] + "    " + ii + "create a namespace for injection " + "into a task script and returns it." + ii);
            as.println(tab[i] + "# first get most (not all) variables");
            as.print(tab[i] + "    arglist = [ ");
            int counter = 0;
            for (VarAttributes va : attributesMap.values()) {
                counter++;
                String varname = va.getName();
                if (Constants.VAR_STDOUT.equals(varname)) continue;
                VarTypeEnum vartype = va.getType();
                switch(vartype) {
                    case DOUBLE:
                    case STRING:
                    case LONG:
                    case BOOLEAN:
                    case CHOICE:
                        as.print(tab[i] + "    '" + varname + "'");
                        if (counter != attributesMap.size()) as.println(",\\");
                        break;
                    case FILES:
                        break;
                    default:
                        throw new RuntimeException("unkown data type in switch");
                }
            }
            as.println("]");
            as.println(tab[i] + "    vars=port0.getManyVars(arglist)");
            as.println(tab[i] + "    namesp = vars ");
            as.println(tab[i] + "    namesp[ 'service' ] = service ");
            as.println(tab[i] + "    return namesp");
            as.println();
            for (String taskName : taskNames) {
                Task task = getService().getTask(taskName);
                boolean background = task.isBackground();
                as.println(tab[i] + "def task_" + taskName + "():");
                {
                    i++;
                    as.println(tab[i] + "try :");
                    if (background) {
                        as.println(tab[i] + "    # as ZSIWrapper is not task save," + " create a new one");
                        as.println(tab[i] + "    port_task=ZSIWrapper(URL, agentID, " + "instanceID, instanceKey, timeout)");
                        as.println(tab[i] + "    service = Service(port_task)");
                    } else {
                        as.println(tab[i] + "    # this is foreground, " + "use port0 from current task");
                        as.println(tab[i] + "    service = Service(port0)");
                    }
                    as.println(tab[i] + "    globals = create_namespace(service)");
                    as.println(tab[i] + "    locals = { }");
                    as.println(tab[i] + "    execfile('" + task.getFile() + "', globals, locals )");
                    as.println(tab[i] + "except :");
                    as.println(tab[i] + "    port0.setTaskStatus('" + taskName + "', " + Status.FAILED.ordinal() + " )");
                    as.println(tab[i] + "else :");
                    as.println(tab[i] + "    if kill_flag :");
                    as.println(tab[i] + "        task_exit_status = " + Status.FAILED.ordinal());
                    as.println(tab[i] + "    elif stop_flag :");
                    as.println(tab[i] + "        task_exit_status = " + Status.TERMINATED.ordinal());
                    as.println(tab[i] + "    else :");
                    as.println(tab[i] + "        task_exit_status = " + Status.FINISHED.ordinal());
                    as.println(tab[i] + "    port0.setTaskStatus('" + taskName + "', task_exit_status )");
                    as.println();
                    i--;
                }
            }
            as.println(tab[i] + "try:");
            i++;
            as.println(tab[i] + "tasklist = []");
            as.println(tab[i] + "action=None");
            as.println(tab[i] + "while action != '" + Agent.STOP + "' and action != '" + Agent.KILL + "' and action != '" + Agent.FINISH + "' :");
            {
                i++;
                as.println(tab[i] + "# print 'ACTION=', action");
                as.println(tab[i] + "action = port0.getAction()");
                as.println(tab[i] + "");
                as.println(tab[i] + "# cleanup tasklist (only to save memory)");
                as.println(tab[i] + "# In two cycles, don't change list during iter.");
                as.println(tab[i] + "to_be_removed = []");
                as.println(tab[i] + "for tr2 in tasklist:");
                as.println(tab[i] + "    if (not tr2.isAlive()):");
                as.println(tab[i] + "        to_be_removed.append(tr2)");
                as.println(tab[i] + "for tr3 in to_be_removed:");
                as.println(tab[i] + "    # print 'old task removed: ',tr3.getName()");
                as.println(tab[i] + "    tasklist.remove(tr3)");
                as.println(tab[i] + "");
                as.println(tab[i] + "if action == None :");
                as.println(tab[i] + "    # print 'processing None'");
                as.println(tab[i] + "    None");
                as.println(tab[i] + "if action == '" + Agent.FINISH + "' :");
                as.println(tab[i] + "    # print 'processing FINISH'");
                as.println(tab[i] + "    None");
                as.println(tab[i] + "elif action == '" + Agent.STOP + "' :");
                as.println(tab[i] + "    stop_flag=1");
                as.println(tab[i] + "    # print 'processing STOP'");
                String onStop = agent.onStop;
                if (onStop != null) {
                    String function = "task_" + onStop;
                    as.println(tab[i] + "    " + function + "()");
                }
                as.println(tab[i] + "");
                as.println(tab[i] + "elif action == '" + Agent.KILL + "' :");
                as.println(tab[i] + "    kill_flag=1");
                as.println(tab[i] + "    # print 'processing KILL'");
                String onKill = agent.onKill;
                if (onKill != null) {
                    String function = "task_" + onKill;
                    as.println(tab[i] + "    " + function + "()");
                }
                as.println(tab[i] + "    # This would be the place " + "to kill external programs");
                as.println(tab[i] + "    port0.setAgentStatus(" + Status.FAILED.ordinal() + ")");
                as.println(tab[i] + "    # exit without waiting for bg threads");
                as.println(tab[i] + "    exit(13)");
                as.println(tab[i] + "");
                for (String taskName : taskNames) {
                    Task task = getService().getTask(taskName);
                    boolean background = task.isBackground();
                    as.println(tab[i] + "elif action == '" + Agent.TASK + taskName + "':");
                    String function = "task_" + taskName;
                    if (background) {
                        as.println(tab[i] + "    # create, register and start task");
                        as.println(tab[i] + "    t = threading.Thread(target=" + function + ", name='" + function + "')");
                        as.println(tab[i] + "    tasklist.append(t)");
                        as.println(tab[i] + "    t.start()");
                    } else {
                        as.println(tab[i] + "    " + function + "()");
                    }
                }
                as.println(tab[i] + "else :");
                as.println(tab[i] + "    print 'undefined case in agent script iov0j23fds, " + "action=', action");
                i--;
            }
            as.println();
            as.println(tab[i] + "# wait for running background tasks");
            as.println(tab[i] + "# print 'remaining threads (inclides demonic ones!):'" + ", threading.activeCount()");
            as.println(tab[i] + "# for tr in threading.enumerate():");
            as.println(tab[i] + "#    print 'remaining thread: '," + " tr.getName()");
            as.println(tab[i] + "for tr2 in tasklist:");
            as.println(tab[i] + "    # print 'joining with ',tr2.getName()");
            as.println(tab[i] + "    tr2.join()");
            i--;
            as.println(tab[i] + "# Set agent status");
            as.println(tab[i] + "except :");
            as.println(tab[i] + "    port0.setAgentStatus(" + Status.FAILED.ordinal() + ")");
            as.println(tab[i] + "else :");
            as.println(tab[i] + "    if kill_flag :");
            as.println(tab[i] + "        agent_exit_status = " + Status.FAILED.ordinal());
            as.println(tab[i] + "    elif stop_flag :");
            as.println(tab[i] + "        agent_exit_status = " + Status.TERMINATED.ordinal());
            as.println(tab[i] + "    else :");
            as.println(tab[i] + "        agent_exit_status = " + Status.FINISHED.ordinal());
            as.println(tab[i] + "    port0.setAgentStatus( agent_exit_status )");
            if (embedScripts) ps.println("EOF_" + agentScript); else as.close();
        }
        for (String agentFileName : agent.getFilesSet()) {
            ps.println();
            File agentRelFile = new File(agentFileName);
            File directory = agentRelFile.getParentFile();
            if (directory != null) {
                ps.println("mkdir -p " + directory.getPath());
            }
            ps.println("cat > " + agentFileName + " << " + ii + "EOF_" + agentFileName + ii);
            File file = new File(scriptDir, agentFileName);
            byte bytes[] = FileUtils.readFileToByteArray(file);
            ps.write(bytes);
            ps.println();
            ps.println("EOF_" + agentFileName);
        }
        if (embedScripts) {
            ps.println("SKIP=`awk '/^__ARCHIVE_FOLLOWS__/ { print NR + 1; exit 0; }' $0`");
            ps.println("tail -n +$SKIP $0 | gzip -dc | tar x ");
            ps.println("python -u " + agentScript);
            ps.println("exit 0");
            ps.println("__ARCHIVE_FOLLOWS__");
            java.io.InputStream istream = this.getClass().getClassLoader().getResourceAsStream("sc/fgrid/agent.tar.gz");
            IOUtils.copy(istream, ps);
        } else {
            String pypath = engine.getAgentPythonPath();
            ps.println("export PYTHONPATH=" + ii + pypath + ii);
            ps.println("python -u " + agentScript);
            ps.println("exit 0");
        }
    }
