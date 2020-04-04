package mapper;

import models.CarMarkModel;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CarMarkMapper {

    @Select("select * from car_mark")
    List<CarMarkModel> all();

    @Select("select * from car_mark where id = #{id}")
    CarMarkModel getById(Long id);

    @Select("select * from car_mark where name like #{name}")
    CarMarkModel getByName(String name);

    @Select("insert into car_mark (name, country) values(#{name}, #{country}) returning *")
    CarMarkModel save(CarMarkModel carMarkModel);

    @Select("update car_mark set name = #{name}, country = #{country} where id = #{id} returning *")
    CarMarkModel update(CarMarkModel carMarkModel);

    @Delete("delete from car_mark where id = #{id}")
    void deleteById(Long id);
}
