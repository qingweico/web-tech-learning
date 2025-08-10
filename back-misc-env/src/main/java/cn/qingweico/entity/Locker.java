package cn.qingweico.entity;

import cn.qingweico.validate.anno.Length;
import cn.qingweico.validate.anno.Name;
import cn.qingweico.validate.anno.Required;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author zqw
 * @date 2025/8/2
 */
public class Locker {
    @Name("锁名称")
    @Length(max = 100)
    @Required
    private String lockName;

    @Name("锁密钥")
    @Length(max = 100)
    @Required
    private String lockKey;

    @Name("上锁次数")
    @Required
    private Integer lockCount;

    @Name("创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Required
    private Date createTime;

    @Name("失效时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Required
    private Date limitTime;

    public String getLockName() {
        return lockName;
    }

    public Locker withLockName(String lockName) {
        this.setLockName(lockName);
        return this;
    }

    public void setLockName(String lockName) {
        this.lockName = lockName == null ? null : lockName.trim();
    }

    public String getLockKey() {
        return lockKey;
    }

    public Locker withLockKey(String lockKey) {
        this.setLockKey(lockKey);
        return this;
    }

    public void setLockKey(String lockKey) {
        this.lockKey = lockKey == null ? null : lockKey.trim();
    }

    public Integer getLockCount() {
        return lockCount;
    }

    public Locker withLockCount(Integer lockCount) {
        this.setLockCount(lockCount);
        return this;
    }

    public void setLockCount(Integer lockCount) {
        this.lockCount = lockCount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Locker withCreateTime(Date createTime) {
        this.setCreateTime(createTime);
        return this;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getLimitTime() {
        return limitTime;
    }

    public Locker withLimitTime(Date limitTime) {
        this.setLimitTime(limitTime);
        return this;
    }

    public void setLimitTime(Date limitTime) {
        this.limitTime = limitTime;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", lockName=").append(lockName);
        sb.append(", lockKey=").append(lockKey);
        sb.append(", lockCount=").append(lockCount);
        sb.append(", createTime=").append(createTime);
        sb.append(", limitTime=").append(limitTime);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Locker other = (Locker) that;
        return (this.getLockName() == null ? other.getLockName() == null : this.getLockName().equals(other.getLockName()))
                && (this.getLockKey() == null ? other.getLockKey() == null : this.getLockKey().equals(other.getLockKey()))
                && (this.getLockCount() == null ? other.getLockCount() == null : this.getLockCount().equals(other.getLockCount()))
                && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
                && (this.getLimitTime() == null ? other.getLimitTime() == null : this.getLimitTime().equals(other.getLimitTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getLockName() == null) ? 0 : getLockName().hashCode());
        result = prime * result + ((getLockKey() == null) ? 0 : getLockKey().hashCode());
        result = prime * result + ((getLockCount() == null) ? 0 : getLockCount().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getLimitTime() == null) ? 0 : getLimitTime().hashCode());
        return result;
    }
}
