package vaop.util;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class AppExecutors {

    private static AppExecutors mInst;

    /**
     * 单线程池
     */
    private final ExecutorService mSingleIO;

    /**
     * 多线程池
     */
    private ExecutorService mPoolIO;

    /**
     * 主线程
     */
    private final Executor mMainThread;

    private AppExecutors(ExecutorService singleIO, ExecutorService poolIO, Executor mainThread) {
        mSingleIO = singleIO;
        mPoolIO = poolIO;
        mMainThread = mainThread;
    }

    /**
     * 更新多线程池
     *
     * @param nThreads 线程池线程的数量
     * @return
     */
    public AppExecutors updatePoolIO(int nThreads) {
        mPoolIO = Executors.newFixedThreadPool(nThreads);
        return this;
    }

    private AppExecutors() {
        this(Executors.newSingleThreadExecutor(), Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors()),
                new MainThreadExecutor());
    }

    /**
     * 获取线程管理实例
     *
     * @return
     */
    public static AppExecutors inst() {
        if (mInst == null) {
            synchronized (AppExecutors.class) {
                if (mInst == null) {
                    mInst = new AppExecutors();
                }
            }
        }
        return mInst;
    }

    public ExecutorService singleIO() {
        return mSingleIO;
    }

    public ExecutorService poolIO() {
        return mPoolIO;
    }

    public Executor mainThread() {
        return mMainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mHandler.post(command);
        }
    }
}