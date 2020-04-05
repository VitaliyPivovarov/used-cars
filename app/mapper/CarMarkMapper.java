package mapper;

import models.CarMarkModel;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CarMarkMapper {

    @Select("select * from car_mark")
    @Results(id="CarMarkModelResult", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "name"),
            @Result(property = "country", column = "country")
    })
    List<CarMarkModel> all();

    @Select("select * from car_mark where id = #{id}")
    @ResultMap("CarMarkModelResult")
    CarMarkModel getById(Long id);

    @Select("insert into car_mark (name, country) values(#{name}, #{country}) returning *")
    @ResultMap("CarMarkModelResult")
    CarMarkModel save(CarMarkModel carMarkModel);

    @Select("update car_mark set name = #{name}, country = #{country} where id = #{id} returning *")
    @ResultMap("CarMarkModelResult")
    CarMarkModel update(CarMarkModel carMarkModel);

    @Delete("delete from car_mark where id = #{id}")
    void deleteById(Long id);
}
