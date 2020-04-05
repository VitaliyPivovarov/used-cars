package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.CarStoreCreateUpdateDto;
import dto.CarStoreDto;
import mapper.CarStoreMapper;
import models.CarStoreModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

public class CarStoreController extends Controller {

    @Inject
    private CarStoreMapper carStoreMapper;

    @Inject
    private ModelMapper modelMapper;

    @Inject
    private ObjectMapper objectMapper;

    public Result getList(Integer mileage, Integer price, String carMarkName, String carModelName) {

        List<CarStoreModel> list = carStoreMapper.all(mileage, price,
                Objects.isNull(carMarkName) ? null : carMarkName.trim(),
                Objects.isNull(carModelName) ? null : carModelName.trim());

        if (list.isEmpty()) {
            return noContent();
        }
        Type toDto = new TypeToken<List<CarStoreDto>>() {
        }.getType();
        return ok(Json.toJson(modelMapper.map(list, toDto)));
    }

    public Result getById(Long id) {
        CarStoreModel entity = carStoreMapper.getById(id);
        if (Objects.isNull(entity)) {
            return noContent();
        }
        return ok(Json.toJson(modelMapper.map(entity, CarStoreDto.class)));
    }

    @BodyParser.Of(value = BodyParser.Json.class)
    public Result create() {

        JsonNode json = request().body().asJson();
        CarStoreCreateUpdateDto create;
        try {
            create = objectMapper.convertValue(json, CarStoreCreateUpdateDto.class);
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }

        CarStoreModel entity;
        try {
            entity = carStoreMapper.save(create.getCarMarkModelId(),
                    create.getCarModelEntityId(), create.getYearOfIssue(),
                    create.getMileage(), create.getPrice());
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
        return created(Json.toJson(modelMapper.map(entity, CarStoreDto.class)));
    }

    @BodyParser.Of(value = BodyParser.Json.class)
    public Result update(Long id) {
        JsonNode json = request().body().asJson();
        CarStoreCreateUpdateDto updateDto;
        try {
            updateDto = objectMapper.convertValue(json, CarStoreCreateUpdateDto.class);
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }

        CarStoreModel entity = carStoreMapper.getById(id);
        if (Objects.isNull(entity)) {
            return noContent();
        }

        try {
            entity = carStoreMapper.update(id, updateDto.getCarMarkModelId(), updateDto.getCarMarkModelId(), updateDto.getYearOfIssue(),
                    updateDto.getMileage(), updateDto.getPrice());
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
        return ok(Json.toJson(modelMapper.map(entity, CarStoreDto.class)));
    }

    public Result deleteById(Long id) {
        CarStoreModel entity = carStoreMapper.getById(id);
        if (Objects.isNull(entity)) {
            return noContent();
        }
        carStoreMapper.deleteById(id);
        return ok();
    }
}
