package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.CarMarkCreateUpdateDto;
import dto.CarMarkDto;
import mapper.CarMarkMapper;
import models.CarMarkModel;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Controller;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ForkJoinPool;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class CarMarkController extends Controller implements CrudController {

    private final ForkJoinPool commonPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

    private final ObjectMapper objectMapper;
    private final CarMarkMapper carMarkMapper;
    private final ModelMapper modelMapper;

    @Inject
    public CarMarkController(ObjectMapper objectMapper, ModelMapper modelMapper, CarMarkMapper carMarkMapper) {
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
        this.carMarkMapper = carMarkMapper;

        this.modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    }

    /**
     * Get list car mark
     * @return list object car mark
     */
    public CompletionStage<Result> getList() {
        return supplyAsync(() -> {
            List<CarMarkModel> carMarkModelList = carMarkMapper.all();
            if (carMarkModelList.isEmpty()) {
                return notFound("Car mark list not found!");
            }
            Type toDto = new TypeToken<List<CarMarkModel>>() {}.getType();
            return ok(Json.toJson(modelMapper.map(carMarkModelList, toDto)));
        }, commonPool);
    }

    /**
     * Get car mark by id
     *
     * @param id car mark
     * @return Object car mark
     */
    @Override
    public CompletionStage<Result> getById(Long id) {
        return supplyAsync(() -> {
            CarMarkModel carMarkModel = carMarkMapper.getById(id);
            if (Objects.isNull(carMarkModel)) {
                return notFound(String.format("Car mark id = %d not found!", id));
            }
            return ok(Json.toJson(modelMapper.map(carMarkModel, CarMarkDto.class)));
        }, commonPool);
    }

    /**
     * Create car mark
     *
     * @return created car mark
     */
    @Override
    public CompletionStage<Result> create(Http.Request request) {
        return supplyAsync(() -> {
            JsonNode json = request.body().asJson();
            CarMarkCreateUpdateDto carMarkCreateUpdateDto;
            try {
                carMarkCreateUpdateDto = objectMapper.convertValue(json, CarMarkCreateUpdateDto.class);
            } catch (Exception e) {
                return badRequest(e.getMessage());
            }

            CarMarkModel carMarkModel = modelMapper.map(carMarkCreateUpdateDto, CarMarkModel.class);
            try {
                carMarkModel = carMarkMapper.save(carMarkModel);
            } catch (Exception e) {
                return internalServerError(e.getMessage());
            }
            return created(Json.toJson(modelMapper.map(carMarkModel, CarMarkDto.class)));
        }, commonPool);
    }

    /**
     * Update car mark
     *
     * @param id car mark
     * @return updated car mark
     */
    @Override
    public CompletionStage<Result> update(Long id, Http.Request request) {
        return supplyAsync(() -> {
            JsonNode json = request.body().asJson();
            CarMarkCreateUpdateDto carMarkCreateUpdateDto;
            try {
                carMarkCreateUpdateDto = objectMapper.convertValue(json, CarMarkCreateUpdateDto.class);
            } catch (Exception e) {
                return badRequest(e.getMessage());
            }

            CarMarkModel existingCarMarkModel = carMarkMapper.getById(id);
            if (Objects.isNull(existingCarMarkModel)) {
                return notFound(String.format("Car mark id = %d not found!", id));
            }
            modelMapper.map(carMarkCreateUpdateDto, existingCarMarkModel);
            try {
                existingCarMarkModel = carMarkMapper.update(existingCarMarkModel);
            } catch (Exception e) {
                return internalServerError(e.getMessage());
            }
            return ok(Json.toJson(modelMapper.map(existingCarMarkModel, CarMarkDto.class)));
        }, commonPool);
    }

    /**
     * Delete car mark
     * @param id car mark
     * @return code status
     */
    @Override
    public CompletionStage<Result> deleteById(Long id) {
        return supplyAsync(() -> {
            carMarkMapper.getById(id);

            // find exist object
            CarMarkModel existingCarMarkModel = carMarkMapper.getById(id);
            if (Objects.isNull(existingCarMarkModel)) {
                return notFound(String.format("Car mark id = %d not found!", id));
            }
            carMarkMapper.deleteById(id);
            return ok();
        }, commonPool);

    }
}
