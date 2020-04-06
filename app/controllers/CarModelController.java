package controllers;

import akka.dispatch.forkjoin.ForkJoinPool;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.CarModelCreateUpdateDto;
import dto.CarModelDto;
import mapper.CarModelMapper;
import models.CarModelEntity;
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

public class CarModelController extends Controller {

    private final ForkJoinPool commonPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());

    @Inject
    private ObjectMapper objectMapper;

    @Inject
    private CarModelMapper carModelMapper;

    @Inject
    private ModelMapper modelMapper;

    public CompletionStage<Result> getList() {
        return supplyAsync(() -> {
            List<CarModelEntity> list = carModelMapper.all();
            if (list.isEmpty()) {
                return noContent();
            }
            Type toDto = new TypeToken<List<CarModelDto>>() {
            }.getType();
            return ok(Json.toJson(modelMapper.map(list, toDto)));
        }, commonPool);

    }

    public CompletionStage<Result> getById(Long id) {
        return supplyAsync(() -> {
            CarModelEntity entity = carModelMapper.getById(id);
            if (Objects.isNull(entity)) {
                return noContent();
            }
            return ok(Json.toJson(modelMapper.map(entity, CarModelDto.class)));
        }, commonPool);
    }

    public CompletionStage<Result> create(Http.Request request) {
        return supplyAsync(() -> {
            JsonNode json = request.body().asJson();
            CarModelCreateUpdateDto create;
            try {
                create = objectMapper.convertValue(json, CarModelCreateUpdateDto.class);
            } catch (Exception e) {
                return badRequest(e.getMessage());
            }

            CarModelEntity entity = modelMapper.map(create, CarModelEntity.class);
            try {
                entity = carModelMapper.save(entity.getName(), entity.getStartYear(), entity.getEndYear());
            } catch (Exception e) {
                return internalServerError(e.getMessage());
            }
            return created(Json.toJson(modelMapper.map(entity, CarModelDto.class)));
        }, commonPool);
    }

    public CompletionStage<Result> update(Long id, Http.Request request) {
        return supplyAsync(() -> {
            JsonNode json = request.body().asJson();
            CarModelCreateUpdateDto updateDto;
            try {
                updateDto = objectMapper.convertValue(json, CarModelCreateUpdateDto.class);
            } catch (Exception e) {
                return badRequest(e.getMessage());
            }

            CarModelEntity entity = carModelMapper.getById(id);
            if (Objects.isNull(entity)) {
                return noContent();
            }
            modelMapper.map(updateDto, entity);
            try {
                entity = carModelMapper.update(entity.getId(), entity.getName(), entity.getStartYear(), entity.getEndYear());
            } catch (Exception e) {
                return internalServerError(e.getMessage());
            }
            return ok(Json.toJson(modelMapper.map(entity, CarModelDto.class)));
        }, commonPool);
    }

    public CompletionStage<Result> deleteById(Long id) {
        return supplyAsync(() -> {
            CarModelEntity entity = carModelMapper.getById(id);
            if (Objects.isNull(entity)) {
                return noContent();
            }
            carModelMapper.deleteById(id);
            return ok();
        }, commonPool);
    }
}
