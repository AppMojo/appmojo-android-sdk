package com.appmojo.sdk.connections;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AMConnectionHandler {

    public static final int TASK_COMPLETE = 1;
    public static final int TASK_FAIL = -1;

    private Handler mHandler;
    private ExecutorService mExecutor;

    public AMConnectionHandler() {
        mHandler = new ConnectionHandler(Looper.getMainLooper());
        mExecutor = Executors.newFixedThreadPool(1);
    }

    public void post(String urlStr, Map<String, String> headers, String jBody, AMConnectionHandlerListener listener) {
        AMConnectionTask task = new AMConnectionTask(this);
        task.prepareTask(urlStr, "POST", headers, jBody, listener);

        Runnable worker = new AMConnectionThread(task);
        mExecutor.execute(worker);
    }

    public void get(String urlStr, Map<String, String> headers, String jBody, AMConnectionHandlerListener listener) {
        AMConnectionTask task = new AMConnectionTask(this);
        task.prepareTask(urlStr, "GET", headers, jBody, listener);

        Runnable worker = new AMConnectionThread(task);
        mExecutor.execute(worker);
    }

    public void put(String urlStr, Map<String, String> headers, String jBody, AMConnectionHandlerListener listener) {
        AMConnectionTask task = new AMConnectionTask(this);
        task.prepareTask(urlStr, "PUT", headers, jBody, listener);

        Runnable worker = new AMConnectionThread(task);
        mExecutor.execute(worker);
    }

    // Handle status messages from tasks
    public void handleState(AMConnectionTask connTask, int state) {
        Message completeMessage;
        if (state == TASK_COMPLETE) {
            completeMessage = mHandler.obtainMessage(state, connTask);
            completeMessage.sendToTarget();
        }
    }

    public void  shutdownAllTask() {
        if(mExecutor != null)
            mExecutor.shutdown();
    }

    public void  onDestroy() {
        shutdownAllTask();
        mExecutor = null;
    }


    //   _____ __  __ __  __             ____ _
    //  |_   _|  \ | |  | | | ___  __ __/ ___| | ____    ___  ___
    //    | | | \| | | \| | |/ _ \| |/ | |   | |/ _  \  / __|/ __|
    //   _| |_| |\ | | |\ | |  __/|   /| |___| | (_)  \ \__ \\__ \
    //  |_____|_| \__|_| \__|\___||__|  \____|_|\___/|_\|___/|___/

    static class ConnectionHandler extends Handler {

        public ConnectionHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message inputMessage) {
            AMConnectionTask connTask = (AMConnectionTask) inputMessage.obj;
            if (inputMessage.what == TASK_COMPLETE) {
                connTask.handleOutput(connTask.getConnectionResponse());
            } else {
                super.handleMessage(inputMessage);
            }
        }
    }
}
