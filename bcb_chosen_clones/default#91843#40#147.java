    private static void eval(String[] tokens) throws Exception {
        char command = tokens == null ? EOF : tokens.length == 0 ? ' ' : tokens[0].charAt(0);
        switch(command) {
            case ' ':
                if (previousTokens != null) eval(previousTokens);
                return;
            case '*':
                if (previousTokens != null) for (VM_Scheduler.debugRequested = false; VM_Scheduler.debugRequested == false; ) {
                    VM.sysWrite("\033[H\033[2J");
                    eval(previousTokens);
                    sleep(1000);
                }
                return;
        }
        previousTokens = tokens;
        switch(command) {
            case 't':
                if (tokens.length == 1) {
                    for (int i = 0, col = 0; i < VM_Scheduler.threads.length; ++i) {
                        VM_Thread thread = VM_Scheduler.threads[i];
                        if (thread == null) continue;
                        VM.sysWrite(rightJustify(thread.getIndex() + " ", 4) + leftJustify(thread.toString(), 40) + getThreadState(thread) + "\n");
                    }
                } else if (tokens.length == 2) {
                    int threadIndex = Integer.valueOf(tokens[1]).intValue();
                    VM_Thread thread = VM_Scheduler.threads[threadIndex];
                    VM.sysWrite(thread.getIndex() + " " + thread + " " + getThreadState(thread) + "\n");
                    int fp = thread == VM_Thread.getCurrentThread() ? VM_Magic.getFramePointer() : thread.contextRegisters.getInnermostFramePointer();
                    VM_Processor.getCurrentProcessor().disableThreadSwitching();
                    VM_Scheduler.dumpStack(fp);
                    VM_Processor.getCurrentProcessor().enableThreadSwitching();
                } else VM.sysWrite("please specify a thread id\n");
                return;
            case 'd':
                VM_Scheduler.dumpVirtualMachine();
                return;
            case 'e':
                if (VM.BuildForEventLogging) {
                    VM.EventLoggingEnabled = !VM.EventLoggingEnabled;
                    VM.sysWrite("event logging " + (VM.EventLoggingEnabled ? "enabled\n" : "disabled\n"));
                } else VM.sysWrite("sorry, not built for event logging\n");
                return;
            case 'l':
                if (VM.BuildForEventLogging) {
                    String fileName = "temp.eventLog";
                    try {
                        FileOutputStream out = new FileOutputStream(fileName);
                        VM_EventLogger.dump(out);
                        out.close();
                        VM.sysWrite("event log written to \"" + fileName + "\"\n");
                    } catch (IOException e) {
                        VM.sysWrite("couldn't write \"" + fileName + "\": " + e + "\n");
                    }
                } else VM.sysWrite("sorry, not built for event logging\n");
                return;
            case 'c':
                VM_Scheduler.debugRequested = false;
                VM_Scheduler.debuggerMutex.lock();
                yield(VM_Scheduler.debuggerQueue, VM_Scheduler.debuggerMutex);
                return;
            case 'q':
                VM.sysWrite("terminating execution\n");
                VM.sysExit(0);
                return;
            case EOF:
                VM_Scheduler.writeString("\n-- Stacks --\n");
                for (int i = 1; i < VM_Scheduler.threads.length; ++i) {
                    VM_Thread t = VM_Scheduler.threads[i];
                    if (t != null) {
                        VM_Scheduler.writeString("\n Thread: ");
                        t.dump();
                        VM_Processor.getCurrentProcessor().disableThreadSwitching();
                        VM_Scheduler.dumpStack(t.contextRegisters.getInnermostFramePointer());
                        VM_Processor.getCurrentProcessor().enableThreadSwitching();
                    }
                }
                VM_Scheduler.writeString("\n");
                VM_Scheduler.dumpVirtualMachine();
                VM_Scheduler.debugRequested = false;
                VM_Scheduler.debuggerMutex.lock();
                yield(VM_Scheduler.debuggerQueue, VM_Scheduler.debuggerMutex);
                return;
            default:
                if (tokens.length == 1) {
                    VM.sysWrite("Try one of:\n");
                    VM.sysWrite("   t                - display all threads\n");
                    VM.sysWrite("   t <threadIndex>  - display specified thread\n");
                    VM.sysWrite("   d                - dump virtual machine state\n");
                    VM.sysWrite("   e                - enable/disable event logging\n");
                    VM.sysWrite("   l                - write event log to a file\n");
                    VM.sysWrite("   c                - continue execution\n");
                    VM.sysWrite("   q                - terminate execution\n");
                    VM.sysWrite("   <class>.<method> - call a method\n");
                    VM.sysWrite("Or:\n");
                    VM.sysWrite("   <enter>          - repeat previous command once\n");
                    VM.sysWrite("   *                - repeat previous command once per second until SIGQUIT is received\n");
                    return;
                }
                if (tokens.length != 3 || !tokens[1].equals(".")) VM.sysWrite("please specify <class>.<method>\n"); else {
                    Class cls = Class.forName(tokens[0]);
                    Class[] signature = new Class[0];
                    Method method = cls.getMethod(tokens[2], signature);
                    Object[] args = new Object[0];
                    method.invoke(null, args);
                }
                return;
        }
    }
