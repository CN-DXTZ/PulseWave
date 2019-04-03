package com.db.web.mapper;

import com.db.web.entity.User;
import com.db.web.entity.Wave;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface WaveMapper {

    @Select("SELECT * FROM wave_${hrbustID}")
    List<Wave> getAllWave(@Param("hrbustID") String hrbustID);

    @Insert("INSERT INTO wave_${hrbustID} (time,value) VALUES (${time},${value})")
    void insertWave(@Param("hrbustID") String hrbustID, @Param("time") String time, @Param("value") String value);
}
