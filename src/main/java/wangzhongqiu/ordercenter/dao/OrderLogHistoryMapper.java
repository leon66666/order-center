package wangzhongqiu.ordercenter.dao;

public interface OrderLogHistoryMapper {
    public Integer insertForBackup(Integer dayOffset);
}
