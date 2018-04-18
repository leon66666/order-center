package wangzhongqiu.ordercenter.dao;

import org.apache.ibatis.annotations.Param;

public interface OrderHistoryMapper {


	/**
	 * 插入备份数据
	 * @param dayOffset
	 * @return
     */
	public int insertForBackup(@Param("dayOffset") Integer dayOffset);
	
}
