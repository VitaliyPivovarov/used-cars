package controllers;

import akka.dispatch.forkjoin.ForkJoinPool;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.CarStoreCreateUpdateDto;
import dto.CarStoreDto;
import mapper.CarMarkMapper;
import mapper.CarModelMapper;
import mapper.CarStoreMapper;
import models.CarMarkModel;
import models.CarModelEntity;
import models.CarStoreModel;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class CarStoreController extends Controller implements CrudController {

    private final ForkJoinPool commonPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

    private CarStoreMapper carStoreMapper;
    private CarMarkMapper carMarkMapper;
    private CarModelMapper carModelMapper;
    private ModelMapper modelMapper;
    private ObjectMapper objectMapper;

    @Inject
    public CarStoreController(ObjectMapper objectMapper, ModelMapper modelMapper, CarStoreMapper carStoreMapper, CarMarkMapper carMarkMapper, CarModelMapper carModelMapper) {
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
        this.carStoreMapper = carStoreMapper;
        this.carMarkMapper = carMarkMapper;
        this.carModelMapper = carModelMapper;

        this.modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
    }

    /**
     * * Get list car store
     *
     * @param mileage search by mileage
     * @param price search by price
     * @param carMarkName search by carMarkName
     * @param carModelName search by carModelName
     * @return list object car model
     */
    public CompletionStage<Result> getList(Integer mileage, Integer price, String carMarkName, String carModelName) {
        return supplyAsync(() -> {
            List<CarStoreModel> list = carStoreMapper.all(mileage, price,
                    Objects.isNull(carMarkName) ? null : carMarkName.trim(),
                    Objects.isNull(carModelName) ? null : carModelName.trim());

            if (list.isEmpty()) {
                return notFound(("Car store list not found!"));
            }
            Type toDto = new TypeToken<List<CarStoreDto>>() {
            }.getType();
            return ok(Json.toJson(modelMapper.map(list, toDto)));
        }, commonPool);
    }

    /**
     * Get car store by id
     *
     * @param id car store
     * @return Object car store
     */
    @Override
    public CompletionStage<Result> getById(Long id) {
        return supplyAsync(() -> {
            CarStoreModel entity = carStoreMapper.getById(id);
            if (Objects.isNull(entity)) {
                return notFound(String.format("Car store id = %d not found!", id));
            }
            return ok(Json.toJson(modelMapper.map(entity, CarStoreDto.class)));
        }, commonPool);
    }

    /**
     * Create car store
     *
     * @return created car store
     */
    @Override
    public CompletionStage<Result> create(Http.Request request) {
        return supplyAsync(() -> {
            JsonNode json = request.body().asJson();
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
        }, commonPool);

    }

    /**
     * Update car store
     *
     * @param id car store
     * @return updated car store
     */
    @Override
    public CompletionStage<Result> update(Long id, Http.Request request) {
        return supplyAsync(() -> {
            JsonNode json = request.body().asJson();
            CarStoreCreateUpdateDto updateDto;
            try {
                updateDto = objectMapper.convertValue(json, CarStoreCreateUpdateDto.class);
            } catch (Exception e) {
                return badRequest(e.getMessage());
            }

            CarStoreModel entity = carStoreMapper.getById(id);
            if (Objects.isNull(entity)) {
                return notFound(String.format("Car store id = %d not found!", id));
            }

            CarMarkModel carMarkModel = carMarkMapper.getById(id);
            if (Objects.isNull(carMarkModel)) {
                return notFound(String.format("Car mark id = %d not found!", id));
            }

            CarModelEntity carModelEntity = carModelMapper.getById(id);
            if (Objects.isNull(carModelEntity)) {
                return notFound(String.format("Car model id = %d not found!", id));
            }

            try {
                entity = carStoreMapper.update(id, updateDto.getCarMarkModelId(), updateDto.getCarModelEntityId(), updateDto.getYearOfIssue(),
                        updateDto.getMileage(), updateDto.getPrice());
            } catch (Exception e) {
                return internalServerError(e.getMessage());
            }
            return ok(Json.toJson(modelMapper.map(entity, CarStoreDto.class)));
        }, commonPool);
    }

    /**
     * Delete car store
     * @param id car store
     * @return code status
     */
    @Override
    public CompletionStage<Result> deleteById(Long id) {
        return supplyAsync(() -> {
            CarStoreModel entity = carStoreMapper.getById(id);
            if (Objects.isNull(entity)) {
                return notFound(String.format("Car store id = %d not found!", id));
            }
            carStoreMapper.deleteById(id);
            return ok();
        }, commonPool);
    }
}
