package com.school.Redis;

import com.school.Utils.TimeUtils;
import org.apache.http.util.TextUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.swing.plaf.TextUI;

@Component
public class DistributedLock {
    @Resource
    private StoredCacheService storedCacheService;

    //单位毫秒
    private long lockTimeOut = TimeUtils.ONE_HOUR_MILLIONS;

    /**
     * @return the lockTimeOut
     */
    public long getLockTimeOut() {

        return lockTimeOut;

    }

    /**
     * 得不到锁立即返回，得到锁返回TRUE
     * @param key
     * @return
     */
    public Boolean tryLock(String key){
        //得到锁后设置的过期时间，未得到锁返回0
        long expireTime = 0;
        expireTime = System.currentTimeMillis() + lockTimeOut +1;
        if (storedCacheService.setnx(key, String.valueOf(expireTime)) == 1)
        {
            //得到了锁返回
            return true;
        }
        else
        {
            String curLockTimeStr =  storedCacheService.get(key);
            //判断是否过期
            if (TextUtils.isEmpty(curLockTimeStr) || System.currentTimeMillis() > Long.valueOf(curLockTimeStr))
            {
                expireTime = System.currentTimeMillis() + lockTimeOut +1;
                curLockTimeStr = storedCacheService.getSet(key, String.valueOf(expireTime));
                //仍然过期,则得到锁
                if (TextUtils.isEmpty(curLockTimeStr) || System.currentTimeMillis() > Long.valueOf(curLockTimeStr))
                    return true;
                else
                    return false;
            }
            else
                return false;
        }
    }



    /**
     * @param key
     */
    public void unlock(String key){
        String curLockTimeStr = storedCacheService.get(key);
        if (!TextUtils.isEmpty(curLockTimeStr)&&Long.valueOf(curLockTimeStr)>System.currentTimeMillis())
            storedCacheService.del(key);
    }

    public void setLockTimeOut(long lockTimeOut) {
        this.lockTimeOut = lockTimeOut;
    }
}
