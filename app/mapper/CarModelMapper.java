package mapper;

import models.CarModelEntity;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CarModelMapper {

    @Select("select * from car_model")
    @Results(value = {
            @Result(property = "startYear", column = "start_year"),
            @Result(property = "endYear", column = "end_year")
    })
    List<CarModelEntity> all();

    @Select("select * from car_model where id = #{id}")
    @Results(value = {
            @Result(property = "startYear", column = "start_year"),
            @Result(property = "endYear", column = "end_year")
    })
    CarModelEntity getById(Long id);

    @Select("insert into car_model (name, start_year, end_year) values(#{name}, #{startYear}, #{endYear}) returning *")
    @Results(value = {
            @Result(property = "startYear", column = "start_year"),
            @Result(property = "endYear", column = "end_year")
    })
    CarModelEntity save(@Param("name") String name, @Param("startYear") LocalDateTime startYear, @Param("endYear") LocalDateTime endYear);

    @Select("update car_model set name = #{name}, start_year = #{startYear}, end_year = #{endYear} where id = #{id} returning *")
    @Results(value = {
            @Result(property = "startYear", column = "start_year"),
            @Result(property = "endYear", column = "end_year")
    })
    CarModelEntity update(@Param("id") Long id, @Param("name") String name, @Param("startYear") LocalDateTime startYear, @Param("endYear") LocalDateTime endYear);

    @Delete("delete from car_model where id = #{id}")
    @Results(value = {
            @Result(property = "startYear", column = "start_year"),
            @Result(property = "endYear", column = "end_year")
    })
    void deleteById(Long id);
}
