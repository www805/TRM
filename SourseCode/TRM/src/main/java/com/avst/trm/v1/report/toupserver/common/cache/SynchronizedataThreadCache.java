package com.avst.trm.v1.report.toupserver.common.cache;

import com.avst.trm.v1.report.toupserver.common.conf.SynchronizedataThread;

/**
 * 数据同步的执行线程的缓存
 * 现阶段每台服务器只允许有一个线程用于同步
 */
public class SynchronizedataThreadCache {

    private static SynchronizedataThread synchronizedataThread=null;

    public static SynchronizedataThread getSynchronizedataThread(){
        return synchronizedataThread;
    }


    public static boolean setSynchronizedataThread(SynchronizedataThread thread){

        if(null==synchronizedataThread){
            synchronizedataThread=thread;
            return true;
        }
        return false;
    }

    public static void delSynchronizedataThread(){

        if(null!=synchronizedataThread){
            synchronizedataThread.workbool=false;

            try {
                synchronizedataThread.interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        synchronizedataThread=null;
    }

}
