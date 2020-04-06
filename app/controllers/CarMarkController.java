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

public class CarMarkController extends Controller {

    private final ForkJoinPool commonPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

    @Inject
    private ObjectMapper objectMapper;

    @Inject
    private CarMarkMapper carMarkMapper;

    @Inject
    private ModelMapper modelMapper;

    /**
     * Get list car mark
     * @return list object car mark
     */
    public CompletionStage<Result> getList() {
        return supplyAsync(() -> {
            List<CarMarkModel> carMarkModelList = carMarkMapper.all();
            if (carMarkModelList.isEmpty()) {
                return noContent();
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
    public CompletionStage<Result> getById(Long id) {
        return supplyAsync(() -> {
            CarMarkModel carMarkModel = carMarkMapper.getById(id);
            if (Objects.isNull(carMarkModel)) {
                return noContent();
            }
            return ok(Json.toJson(modelMapper.map(carMarkModel, CarMarkDto.class)));
        }, commonPool);
    }

    /**
     * Create car mark
     *
     * @return created car mark
     */
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
                return noContent();
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
    public CompletionStage<Result> deleteById(Long id) {
        return supplyAsync(() -> {
            carMarkMapper.getById(id);

            // find exist object
            CarMarkModel existingCarMarkModel = carMarkMapper.getById(id);
            if (Objects.isNull(existingCarMarkModel)) {
                return noContent();
            }
            carMarkMapper.deleteById(id);
            return ok();
        }, commonPool);

    }
}
