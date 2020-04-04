package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.CarMarkCreateUpdateDto;
import dto.CarMarkDto;
import mapper.CarMarkMapper;
import models.CarMarkModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Result;
import play.mvc.Controller;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

public class CarMarkController extends Controller {

    @Inject
    private ObjectMapper objectMapper;

    @Inject
    private CarMarkMapper carMarkMapper;

    @Inject
    private ModelMapper modelMapper;

    public Result getList() {
        List<CarMarkModel> carMarkModelList = carMarkMapper.all();
        if (carMarkModelList.isEmpty()) {
            return noContent();
        }
        Type toDto = new TypeToken<List<CarMarkModel>>() {
        }.getType();
        return ok(Json.toJson(modelMapper.map(carMarkModelList, toDto)));
    }

    public Result getById(Long id) {
        CarMarkModel carMarkModel = carMarkMapper.getById(id);
        if (Objects.isNull(carMarkModel)) {
            return noContent();
        }
        return ok(Json.toJson(modelMapper.map(carMarkModel, CarMarkDto.class)));
    }

    @BodyParser.Of(value = BodyParser.Json.class)
    public Result create() {

        JsonNode json = request().body().asJson();
        CarMarkCreateUpdateDto carMarkCreateUpdateDto;
        try {
            carMarkCreateUpdateDto = objectMapper.convertValue(json, CarMarkCreateUpdateDto.class);
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }

        CarMarkModel carMarkModel = modelMapper.map(carMarkCreateUpdateDto, CarMarkModel.class);
        try {
            carMarkModel = carMarkMapper.save(carMarkModel);
        } catch (Exception e){
            return internalServerError(e.getMessage());
        }
        return created(Json.toJson(modelMapper.map(carMarkModel, CarMarkDto.class)));
    }

    @BodyParser.Of(value = BodyParser.Json.class)
    public Result update(Long id) {
        JsonNode json = request().body().asJson();
        CarMarkCreateUpdateDto carMarkCreateUpdateDto;
        try {
            carMarkCreateUpdateDto = objectMapper.convertValue(json, CarMarkCreateUpdateDto.class);
        } catch (Exception e) {
            return badRequest(e.getMessage());
        }

        CarMarkModel existingCarMarkModel = carMarkMapper.getById(id);
        if (Objects.isNull(existingCarMarkModel)) {
            return noContent();
        }
        modelMapper.map(carMarkCreateUpdateDto, existingCarMarkModel);
        try {
            existingCarMarkModel = carMarkMapper.update(existingCarMarkModel);
        } catch (Exception e) {
            return internalServerError(e.getMessage());
        }
        return ok(Json.toJson(modelMapper.map(existingCarMarkModel, CarMarkDto.class)));
    }

    public Result deleteById(Long id) {
        CarMarkModel carMarkModel = carMarkMapper.getById(id);
        if (Objects.isNull(carMarkModel)) {
            return noContent();
        }
        carMarkMapper.deleteById(id);
        return ok();
    }
}
