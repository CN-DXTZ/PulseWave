package com.db.web.mapper;

import com.db.web.entity.User;
import com.db.web.entity.Wave;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WaveMapper {

    @Insert("INSERT INTO wave_${id} (time,value) VALUES (${time},${value})")
    void insertWave(@Param("id") String id,
                    @Param("time") String time, @Param("value") String value);

    @Select("SELECT * FROM wave_${id}")
    List<Wave> selectAllWave(@Param("id") String id);

    @Select("SELECT * FROM wave_${id} WHERE time BETWEEN ${startTime} AND ${endTime}")
    List<Wave> selectWave_timeRange(@Param("id") String id,
                                    @Param("startTime") String startTime, @Param("endTime") String endTime);

    @Select("SELECT * FROM wave_${id} WHERE time >= ${prevTime}")
    List<Wave> selectWave_timeAfter(@Param("id") String id, @Param("prevTime") String prevTime);
}
