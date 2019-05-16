package com.db.web.mapper;

import com.db.web.entity.Info;
import com.db.web.entity.Wave;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface InfoMapper {

    @Insert("INSERT INTO info_${id} (time,wave,accel) VALUES (${time},'${wave}','${accel}')")
    void insertInfo(@Param("id") String id,
                    @Param("time") String time,
                    @Param("wave") String wave,
                    @Param("accel") String accel);

    @Select("SELECT time,wave FROM info_${id} WHERE time BETWEEN ${startTime} AND ${endTime}")
    List<Wave> selectWave_timeRange(@Param("id") String id,
                                    @Param("startTime") String startTime, @Param("endTime") String endTime);

    @Select("SELECT time,wave FROM info_${id} WHERE time >= ${prevTime}")
    List<Wave> selectWave_timeAfter(@Param("id") String id, @Param("prevTime") String prevTime);

    @Select("SELECT * FROM info_${id} WHERE time BETWEEN ${startTime} AND ${endTime}")
    List<Info> selectInfo_timeRange(@Param("id") String id,
                                    @Param("startTime") String startTime, @Param("endTime") String endTime);

    @Select("SELECT * FROM info_${id} WHERE time = ${currTime}")
    Info selectInfo_time(@Param("id") String id, @Param("currTime") String currTime);

    @Select("SELECT * FROM info_${id} ORDER BY time LIMIT 1")
    Info selectInfo_minTime(@Param("id") String id);

    @Select("SELECT * FROM info_${id} ORDER BY time DESC LIMIT 1")
    Info selectInfo_maxTime(@Param("id") String id);
}
