package mapper;

import models.CarStoreModel;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface CarStoreMapper {

    @Select("select * from car_store")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "carMarkModel", column = "car_mark_id",
                    many = @Many(select = "mapper.CarMarkMapper.getById")),
            @Result(property = "carModelEntity", column = "car_model_id",
                    many = @Many(select = "mapper.CarModelMapper.getById")),
            @Result(property = "yearOfIssue", column = "year_of_issue"),
            @Result(property = "mileage", column = "mileage"),
            @Result(property = "price", column = "price"),
    })
    List<CarStoreModel> all();

    @Select("select * from car_store where id = #{id}")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "carMarkModel", column = "car_mark_id",
                    many = @Many(select = "mapper.CarMarkMapper.getById")),
            @Result(property = "carModelEntity", column = "car_model_id",
                    many = @Many(select = "mapper.CarModelMapper.getById")),
            @Result(property = "yearOfIssue", column = "year_of_issue"),
            @Result(property = "mileage", column = "mileage"),
            @Result(property = "price", column = "price")})
    CarStoreModel getById(Long id);

    @Select("insert into car_store (car_mark_id, car_model_id, year_of_issue, mileage, price) " +
            "values(#{carMarkId}, #{carModelId}, #{yearOfIssue}, #{mileage}, #{price}) returning *")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "carMarkModel", column = "car_mark_id",
                    many = @Many(select = "mapper.CarMarkMapper.getById")),
            @Result(property = "carModelEntity", column = "car_model_id",
                    many = @Many(select = "mapper.CarModelMapper.getById")),
            @Result(property = "yearOfIssue", column = "year_of_issue"),
            @Result(property = "mileage", column = "mileage"),
            @Result(property = "price", column = "price")})
    CarStoreModel save(@Param("carMarkId") Long carMarkId,
                       @Param("carModelId") Long carModelId,
                       @Param("yearOfIssue") LocalDateTime yearOfIssue,
                       @Param("mileage") int mileage,
                       @Param("price") BigDecimal price);

    @Select("update car_store set " +
            "car_mark_id = #{carMarkId}, " +
            "car_model_id = #{carModelId}, " +
            "year_of_issue = #{yearOfIssue}, " +
            "mileage = #{mileage}, " +
            "price = #{price} " +
            "where id = #{id} returning *")
    @Results(value = {
            @Result(property = "id", column = "id"),
            @Result(property = "carMarkModel", column = "car_mark_id",
                    many = @Many(select = "mapper.CarMarkMapper.getById")),
            @Result(property = "carModelEntity", column = "car_model_id",
                    many = @Many(select = "mapper.CarModelMapper.getById")),
            @Result(property = "yearOfIssue", column = "year_of_issue"),
            @Result(property = "mileage", column = "mileage"),
            @Result(property = "price", column = "price")})
    CarStoreModel update(@Param("id") Long id,
                         @Param("carMarkId") Long carMarkId,
                         @Param("carModelId") Long carModelId,
                         @Param("yearOfIssue") LocalDateTime yearOfIssue,
                         @Param("mileage") int mileage,
                         @Param("price") BigDecimal price);

    @Delete("delete from car_store where id = #{id}")
    void deleteById(Long id);
}
