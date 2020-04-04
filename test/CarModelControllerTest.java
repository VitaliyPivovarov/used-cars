import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dto.CarModelCreateUpdateDto;
import dto.CarModelDto;
import mapper.CarModelMapper;
import models.CarModelEntity;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;
import utils.TimeUtils;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static play.test.Helpers.*;

public class CarModelControllerTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    private final String CAR_MODEL_URI = "/car-model";
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    /**
     * test: GET list - no content
     */
    @Test
    public void getListNoContent() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri(CAR_MODEL_URI);

        Result result = route(app, request);
        assertEquals(NO_CONTENT, result.status());
    }

    /**
     * test: GET list - OK
     */
    @Test
    public void getListOk() {
        CarModelMapper carModelMapper = app.injector().instanceOf(CarModelMapper.class);
        CarModelEntity entity = new CarModelEntity();
        entity.setName("2110");
        entity.setStartYear(TimeUtils.now());
        entity.setEndYear(TimeUtils.now());
        carModelMapper.save(entity.getName(), entity.getStartYear(), entity.getEndYear());

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri(CAR_MODEL_URI);

        Result result = route(app, request);
        assertEquals(OK, result.status());
    }

    /**
     * test: GET by ID - no content
     */
    @Test
    public void getByIdNoContent() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri(CAR_MODEL_URI.concat("/100500"));

        Result result = route(app, request);
        assertEquals(NO_CONTENT, result.status());
    }

    /**
     * test: GET by ID - OK
     */
    @Test
    public void getByIdOk() throws IOException {
        CarModelMapper carModelMapper = app.injector().instanceOf(CarModelMapper.class);
        CarModelEntity newEntity = new CarModelEntity();
        newEntity.setName("2110");
        newEntity.setStartYear(TimeUtils.now());
        newEntity.setEndYear(TimeUtils.now());
        newEntity = carModelMapper.save(newEntity.getName(), newEntity.getStartYear(), newEntity.getEndYear());

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri(CAR_MODEL_URI.concat("/").concat(String.valueOf(newEntity.getId())));

        Result result = route(app, request);
        assertEquals(OK, result.status());

        String json = contentAsString(result);
        CarModelEntity created = objectMapper.readValue(json, CarModelEntity.class);

        assertEquals(created.getName(), newEntity.getName());
        assertEquals(created.getStartYear(), newEntity.getStartYear());
        assertEquals(created.getEndYear(), newEntity.getEndYear());
    }

    /**
     * test: Create Model - BadRequest
     */
    @Test
    public void createModelBadRequest() {
        JsonNode json = Json.toJson("abracadabra");
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri(CAR_MODEL_URI).bodyJson(json);

        Result result = route(app, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    /**
     * test: Create Model - Created
     */
    @Test
    public void createModelCreated() throws IOException {
        CarModelCreateUpdateDto dto = new CarModelCreateUpdateDto();
        dto.setName("createdTest");
        dto.setStartYear(TimeUtils.now());
        dto.setEndYear(TimeUtils.now());

        JsonNode json = Json.toJson(dto);

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri(CAR_MODEL_URI).bodyJson(json);

        Result result = route(app, request);

        assertEquals(CREATED, result.status());

        CarModelDto created = objectMapper.readValue(contentAsString(result), CarModelDto.class);

        assertNotNull(created.getId());
        assertEquals(dto.getName(), created.getName());
        assertEquals(dto.getStartYear(), created.getStartYear());
        assertEquals(dto.getEndYear(), created.getEndYear());
    }

    /**
     * test: Update Model - BadRequest
     */
    @Test
    public void updateModelBadRequest() {
        CarModelMapper carModelMapper = app.injector().instanceOf(CarModelMapper.class);
        CarModelEntity entity = new CarModelEntity();
        entity.setName("2112");
        entity.setStartYear(TimeUtils.now());
        entity.setEndYear(TimeUtils.now());
        entity = carModelMapper.save(entity.getName(), entity.getStartYear(), entity.getEndYear());

        JsonNode json = Json.toJson("abracadabra");
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(PUT)
                .uri(CAR_MODEL_URI.concat("/").concat(String.valueOf(entity.getId()))).bodyJson(json);

        Result result = route(app, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    /**
     * test: Update Model - NoContent
     */
    @Test
    public void updateModelNoContent() {
        CarModelCreateUpdateDto dto = new CarModelCreateUpdateDto();
        dto.setName("test");
        dto.setStartYear(TimeUtils.now());
        dto.setEndYear(TimeUtils.now());

        JsonNode json = Json.toJson(dto);

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(PUT)
                .uri(CAR_MODEL_URI.concat("/100500")).bodyJson(json);

        Result result = route(app, request);
        assertEquals(NO_CONTENT, result.status());
    }

    /**
     * test: Update Model - OK
     */
    @Test
    public void updateModelOk() throws IOException {
        CarModelMapper carModelMapper = app.injector().instanceOf(CarModelMapper.class);
        CarModelEntity entity = new CarModelEntity();
        entity.setName("2114");
        entity.setStartYear(TimeUtils.now());
        entity.setEndYear(TimeUtils.now());
        entity = carModelMapper.save(entity.getName(), entity.getStartYear(), entity.getEndYear());

        CarModelCreateUpdateDto dto = new CarModelCreateUpdateDto();
        dto.setName("updateTest");
        dto.setStartYear(TimeUtils.now().minusYears(1));
        dto.setEndYear(TimeUtils.now().plusYears(1));

        JsonNode json = Json.toJson(dto);

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(PUT)
                .uri(CAR_MODEL_URI.concat("/").concat(String.valueOf(entity.getId()))).bodyJson(json);

        Result result = route(app, request);

        assertEquals(OK, result.status());

        CarModelDto updated = objectMapper.readValue(contentAsString(result), CarModelDto.class);

        assertNotNull(updated.getId());
        assertEquals(entity.getId(), updated.getId());
        assertEquals(dto.getName(), updated.getName());
        assertEquals(dto.getStartYear(), updated.getStartYear());
        assertEquals(dto.getEndYear(), updated.getEndYear());
    }

    /**
     * test: Delete Model - NoContent
     */
    @Test
    public void deleteModelNoContent() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(DELETE)
                .uri(CAR_MODEL_URI.concat("/100500"));

        Result result = route(app, request);
        assertEquals(NO_CONTENT, result.status());
    }

    /**
     * test: Delete Model - OK
     */
    @Test
    public void deleteModelOk() {
        CarModelMapper carModelMapper = app.injector().instanceOf(CarModelMapper.class);
        CarModelEntity entity = new CarModelEntity();
        entity.setName("deleteTest");
        entity.setStartYear(TimeUtils.now());
        entity.setEndYear(TimeUtils.now());
        entity = carModelMapper.save(entity.getName(), entity.getStartYear(), entity.getEndYear());

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(DELETE)
                .uri(CAR_MODEL_URI.concat("/").concat(String.valueOf(entity.getId())));

        Result result = route(app, request);

        assertEquals(OK, result.status());

        request = new Http.RequestBuilder()
                .method(GET)
                .uri(CAR_MODEL_URI.concat("/").concat(String.valueOf(entity.getId())));

        result = route(app, request);
        assertEquals(NO_CONTENT, result.status());
    }
}
