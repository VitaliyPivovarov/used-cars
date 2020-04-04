import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.CarMarkCreateUpdateDto;
import dto.CarMarkDto;
import mapper.CarMarkMapper;
import models.CarMarkModel;
import org.junit.Test;
import play.libs.Json;
import play.mvc.Http;
import play.test.WithApplication;
import play.inject.guice.GuiceApplicationBuilder;
import play.Application;
import play.mvc.Result;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static play.test.Helpers.*;

public class CarMarkControllerTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    private final String CAR_MARK_URI = "/car-mark";
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * test: GET list - no content
     */
    @Test
    public void getListNoContent() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri(CAR_MARK_URI);

        Result result = route(app, request);
        assertEquals(NO_CONTENT, result.status());
    }

    /**
     * test: GET list - OK
     */
    @Test
    public void getListOk() {
        CarMarkMapper carMarkMapper = app.injector().instanceOf(CarMarkMapper.class);
        CarMarkModel carMarkModel = new CarMarkModel();
        carMarkModel.setName("Lada");
        carMarkModel.setCountry("Russia");
        carMarkMapper.save(carMarkModel);

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri(CAR_MARK_URI);

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
                .uri(CAR_MARK_URI.concat("/100500"));

        Result result = route(app, request);
        assertEquals(NO_CONTENT, result.status());
    }

    /**
     * test: GET by ID - OK
     */
    @Test
    public void getByIdOk() throws IOException {

        CarMarkMapper carMarkMapper = app.injector().instanceOf(CarMarkMapper.class);
        CarMarkModel carMarkModel = new CarMarkModel();
        carMarkModel.setName("Lada");
        carMarkModel.setCountry("Russia");
        carMarkModel = carMarkMapper.save(carMarkModel);

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri(CAR_MARK_URI.concat("/").concat(String.valueOf(carMarkModel.getId())));

        Result result = route(app, request);
        assertEquals(OK, result.status());

        String json = contentAsString(result);
        CarMarkModel createdModel = objectMapper.readValue(json, CarMarkModel.class);

        assertEquals(carMarkModel.getName(), createdModel.getName());
        assertEquals(carMarkModel.getCountry(), createdModel.getCountry());
    }

    /**
     * test: Create Model - BadRequest
     */
    @Test
    public void createModelBadRequest() {
        JsonNode json = Json.toJson("abracadabra");
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri(CAR_MARK_URI).bodyJson(json);

        Result result = route(app, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    /**
     * test: Create Model - Created
     */
    @Test
    public void createModelCreated() throws IOException {
        CarMarkCreateUpdateDto dto = new CarMarkCreateUpdateDto();
        dto.setName("createdTest");
        dto.setCountry("createdTest");

        JsonNode json = Json.toJson(dto);

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri(CAR_MARK_URI).bodyJson(json);

        Result result = route(app, request);

        assertEquals(CREATED, result.status());

        CarMarkDto created = objectMapper.readValue(contentAsString(result), CarMarkDto.class);

        assertEquals(dto.getName(), created.getName());
        assertEquals(dto.getCountry(), created.getCountry());
        assertNotNull(created.getId());
    }

    /**
     * test: Update Model - BadRequest
     */
    @Test
    public void updateModelBadRequest() {
        CarMarkMapper carMarkMapper = app.injector().instanceOf(CarMarkMapper.class);
        CarMarkModel carMarkModel = new CarMarkModel();
        carMarkModel.setName("Lada");
        carMarkModel.setCountry("Russia");
        carMarkModel = carMarkMapper.save(carMarkModel);

        JsonNode json = Json.toJson("abracadabra");
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(PUT)
                .uri(CAR_MARK_URI.concat("/").concat(String.valueOf(carMarkModel.getId()))).bodyJson(json);

        Result result = route(app, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    /**
     * test: Update Model - NoContent
     */
    @Test
    public void updateModelNoContent() {
        CarMarkCreateUpdateDto dto = new CarMarkCreateUpdateDto();
        dto.setName("test");
        dto.setCountry("test");

        JsonNode json = Json.toJson(dto);

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(PUT)
                .uri(CAR_MARK_URI.concat("/100500")).bodyJson(json);

        Result result = route(app, request);
        assertEquals(NO_CONTENT, result.status());
    }

    /**
     * test: Update Model - OK
     */
    @Test
    public void updateModelOk() throws IOException {
        CarMarkMapper carMarkMapper = app.injector().instanceOf(CarMarkMapper.class);
        CarMarkModel carMarkModel = new CarMarkModel();
        carMarkModel.setName("createTest");
        carMarkModel.setCountry("createTest");
        carMarkModel = carMarkMapper.save(carMarkModel);

        CarMarkCreateUpdateDto dto = new CarMarkCreateUpdateDto();
        dto.setName("updateTest");
        dto.setCountry("updateTest");

        JsonNode json = Json.toJson(dto);

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(PUT)
                .uri(CAR_MARK_URI.concat("/").concat(String.valueOf(carMarkModel.getId()))).bodyJson(json);

        Result result = route(app, request);

        assertEquals(OK, result.status());

        CarMarkDto created = objectMapper.readValue(contentAsString(result), CarMarkDto.class);

        assertNotNull(created.getId());
        assertEquals(carMarkModel.getId(), created.getId());
        assertEquals(dto.getName(), created.getName());
        assertEquals(dto.getCountry(), created.getCountry());
    }

    /**
     * test: Delete Model - NoContent
     */
    @Test
    public void deleteModelNoContent() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(DELETE)
                .uri(CAR_MARK_URI.concat("/100500"));

        Result result = route(app, request);
        assertEquals(NO_CONTENT, result.status());
    }

    /**
     * test: Delete Model - OK
     */
    @Test
    public void deleteModelOk() {
        CarMarkMapper carMarkMapper = app.injector().instanceOf(CarMarkMapper.class);
        CarMarkModel carMarkModel = new CarMarkModel();
        carMarkModel.setName("deleteTest");
        carMarkModel.setCountry("deleteTest");
        carMarkModel = carMarkMapper.save(carMarkModel);

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(DELETE)
                .uri(CAR_MARK_URI.concat("/").concat(String.valueOf(carMarkModel.getId())));

        Result result = route(app, request);

        assertEquals(OK, result.status());

        request = new Http.RequestBuilder()
                .method(GET)
                .uri(CAR_MARK_URI.concat("/").concat(String.valueOf(carMarkModel.getId())));

        result = route(app, request);
        assertEquals(NO_CONTENT, result.status());
    }
}
