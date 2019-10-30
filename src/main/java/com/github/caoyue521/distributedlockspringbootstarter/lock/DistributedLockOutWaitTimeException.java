package com.github.caoyue521.distributedlockspringbootstarter.lock;

/**
 * @author caoyue
 */
public class DistributedLockOutWaitTimeException extends RuntimeException {
    DistributedLockOutWaitTimeException(String message){
        super(message);
    }
}
