import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dto.CarStoreCreateUpdateDto;
import dto.CarStoreDto;
import mapper.CarMarkMapper;
import mapper.CarModelMapper;
import mapper.CarStoreMapper;
import models.CarMarkModel;
import models.CarModelEntity;
import models.CarStoreModel;
import org.junit.Test;
import play.Application;
import play.inject.guice.GuiceApplicationBuilder;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.test.WithApplication;
import utils.TimeUtils;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static play.test.Helpers.*;

public class CarStoreControllerTest extends WithApplication {

    @Override
    protected Application provideApplication() {
        return new GuiceApplicationBuilder().build();
    }

    private final String CAR_STORE_URI = "/car-store";
    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    /**
     * test: GET list - no content
     */
    @Test
    public void getListNoContent() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri(CAR_STORE_URI);

        Result result = route(app, request);
        assertEquals(NO_CONTENT, result.status());
    }

    /**
     * test: GET list - OK
     */
    @Test
    public void getListOk() {
        CarStoreMapper carStoreMapper = app.injector().instanceOf(CarStoreMapper.class);
        CarMarkMapper carMarkMapper = app.injector().instanceOf(CarMarkMapper.class);
        CarModelMapper carModelMapper = app.injector().instanceOf(CarModelMapper.class);

        //create car-model
        CarModelEntity carModelEntity = new CarModelEntity();
        carModelEntity.setName("2110");
        carModelEntity.setStartYear(TimeUtils.now());
        carModelEntity.setEndYear(TimeUtils.now());
        carModelEntity = carModelMapper.save(carModelEntity.getName(), carModelEntity.getStartYear(), carModelEntity.getEndYear());

        //create car-mark
        CarMarkModel carMarkModel = new CarMarkModel();
        carMarkModel.setName("2110");
        carMarkModel.setCountry("Ukraine");
        carMarkModel = carMarkMapper.save(carMarkModel);

        //create car-store
        CarStoreModel carStoreModel = new CarStoreModel();
        carStoreModel.setCarMarkModel(carMarkModel);
        carStoreModel.setCarModelEntity(carModelEntity);
        carStoreModel.setMileage(10500);
        carStoreModel.setPrice(new BigDecimal(200));
        carStoreModel.setYearOfIssue(TimeUtils.now());
        carStoreMapper.save(carStoreModel.getCarMarkModel().getId(), carStoreModel.getCarModelEntity().getId(), carStoreModel.getYearOfIssue(),
                carStoreModel.getMileage(), carStoreModel.getPrice());

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri(CAR_STORE_URI);

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
                .uri(CAR_STORE_URI.concat("/100500"));

        Result result = route(app, request);
        assertEquals(NO_CONTENT, result.status());
    }

    /**
     * test: GET by ID - OK
     */
    @Test
    public void getByIdOk() throws IOException {
        CarStoreMapper carStoreMapper = app.injector().instanceOf(CarStoreMapper.class);
        CarMarkMapper carMarkMapper = app.injector().instanceOf(CarMarkMapper.class);
        CarModelMapper carModelMapper = app.injector().instanceOf(CarModelMapper.class);

        //create car-model
        CarModelEntity carModelEntity = new CarModelEntity();
        carModelEntity.setName("2110");
        carModelEntity.setStartYear(TimeUtils.now());
        carModelEntity.setEndYear(TimeUtils.now());
        carModelEntity = carModelMapper.save(carModelEntity.getName(), carModelEntity.getStartYear(), carModelEntity.getEndYear());

        //create car-mark
        CarMarkModel carMarkModel = new CarMarkModel();
        carMarkModel.setName("2110");
        carMarkModel.setCountry("Ukraine");
        carMarkModel = carMarkMapper.save(carMarkModel);

        //create car-store
        CarStoreModel carStoreModel = new CarStoreModel();
        carStoreModel.setCarMarkModel(carMarkModel);
        carStoreModel.setCarModelEntity(carModelEntity);
        carStoreModel.setMileage(10500);
        carStoreModel.setPrice(new BigDecimal(200));
        carStoreModel.setYearOfIssue(TimeUtils.now());
        carStoreModel = carStoreMapper.save(carStoreModel.getCarMarkModel().getId(), carStoreModel.getCarModelEntity().getId(), carStoreModel.getYearOfIssue(),
                carStoreModel.getMileage(), carStoreModel.getPrice());

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri(CAR_STORE_URI.concat("/").concat(String.valueOf(carStoreModel.getId())));

        Result result = route(app, request);
        assertEquals(OK, result.status());

        String json = contentAsString(result);
        CarStoreModel created = objectMapper.readValue(json, CarStoreModel.class);

        assertEquals(created.getCarMarkModel(), carStoreModel.getCarMarkModel());
        assertEquals(created.getCarModelEntity(), carStoreModel.getCarModelEntity());
        assertEquals(created.getMileage(), carStoreModel.getMileage());
        assertEquals(created.getPrice().intValue(), carStoreModel.getPrice().intValue());
        assertEquals(created.getYearOfIssue(), carStoreModel.getYearOfIssue());
    }

    /**
     * test: Create Model - BadRequest
     */
    @Test
    public void createModelBadRequest() {
        JsonNode json = Json.toJson("abracadabra");
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri(CAR_STORE_URI).bodyJson(json);

        Result result = route(app, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    /**
     * test: Create Model - Created
     */
    @Test
    public void createModelCreated() throws IOException {
        CarMarkMapper carMarkMapper = app.injector().instanceOf(CarMarkMapper.class);
        CarModelMapper carModelMapper = app.injector().instanceOf(CarModelMapper.class);

        //create car-model
        CarModelEntity carModelEntity = new CarModelEntity();
        carModelEntity.setName("2110");
        carModelEntity.setStartYear(TimeUtils.now());
        carModelEntity.setEndYear(TimeUtils.now());
        carModelEntity = carModelMapper.save(carModelEntity.getName(), carModelEntity.getStartYear(), carModelEntity.getEndYear());

        //create car-mark
        CarMarkModel carMarkModel = new CarMarkModel();
        carMarkModel.setName("2110");
        carMarkModel.setCountry("Ukraine");
        carMarkModel = carMarkMapper.save(carMarkModel);

        //create car-store
        CarStoreCreateUpdateDto createDto = new CarStoreCreateUpdateDto();
        createDto.setCarMarkModelId(carMarkModel.getId());
        createDto.setCarModelEntityId(carModelEntity.getId());
        createDto.setMileage(100500);
        createDto.setYearOfIssue(TimeUtils.now());
        createDto.setPrice(new BigDecimal(200));

        JsonNode json = Json.toJson(createDto);

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(POST)
                .uri(CAR_STORE_URI).bodyJson(json);

        Result result = route(app, request);

        assertEquals(CREATED, result.status());

        CarStoreDto created = objectMapper.readValue(contentAsString(result), CarStoreDto.class);

        assertNotNull(created.getId());
        assertEquals(createDto.getCarMarkModelId(), created.getCarMarkModel().getId());
        assertEquals(createDto.getCarModelEntityId(), created.getCarModelEntity().getId());
        assertEquals(createDto.getMileage(), created.getMileage());
        assertEquals(createDto.getYearOfIssue(), created.getYearOfIssue());
        assertEquals(createDto.getPrice().intValue(), created.getPrice().intValue());
    }

    /**
     * test: Update Model - BadRequest
     */
    @Test
    public void updateModelBadRequest() {
        JsonNode json = Json.toJson("abracadabra");
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(PUT)
                .uri(CAR_STORE_URI.concat("/1")).bodyJson(json);

        Result result = route(app, request);
        assertEquals(BAD_REQUEST, result.status());
    }

    /**
     * test: Update Model - NoContent
     */
    @Test
    public void updateModelNoContent() {
        //create car-store
        CarStoreCreateUpdateDto createDto = new CarStoreCreateUpdateDto();
        createDto.setCarMarkModelId(1L);
        createDto.setCarModelEntityId(1L);
        createDto.setMileage(100500);
        createDto.setYearOfIssue(TimeUtils.now());
        createDto.setPrice(new BigDecimal(200));

        JsonNode json = Json.toJson(createDto);

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(PUT)
                .uri(CAR_STORE_URI.concat("/100500")).bodyJson(json);

        Result result = route(app, request);
        assertEquals(NO_CONTENT, result.status());
    }

    /**
     * test: Update Model - OK
     */
    @Test
    public void updateModelOk() throws IOException {
        CarStoreMapper carStoreMapper = app.injector().instanceOf(CarStoreMapper.class);
        CarMarkMapper carMarkMapper = app.injector().instanceOf(CarMarkMapper.class);
        CarModelMapper carModelMapper = app.injector().instanceOf(CarModelMapper.class);

        //create car-model
        CarModelEntity carModelEntity = new CarModelEntity();
        carModelEntity.setName("Lada");
        carModelEntity.setStartYear(TimeUtils.now());
        carModelEntity.setEndYear(TimeUtils.now());
        carModelEntity = carModelMapper.save(carModelEntity.getName(), carModelEntity.getStartYear(), carModelEntity.getEndYear());

        //create car-mark
        CarMarkModel carMarkModel = new CarMarkModel();
        carMarkModel.setName("2110");
        carMarkModel.setCountry("Ukraine");
        carMarkModel = carMarkMapper.save(carMarkModel);

        //create car-store
        CarStoreModel carStoreModel = new CarStoreModel();
        carStoreModel.setCarMarkModel(carMarkModel);
        carStoreModel.setCarModelEntity(carModelEntity);
        carStoreModel.setMileage(10500);
        carStoreModel.setPrice(new BigDecimal(200));
        carStoreModel.setYearOfIssue(TimeUtils.now());
        carStoreModel = carStoreMapper.save(carStoreModel.getCarMarkModel().getId(), carStoreModel.getCarModelEntity().getId(), carStoreModel.getYearOfIssue(),
                carStoreModel.getMileage(), carStoreModel.getPrice());

        //create car-store
        CarStoreCreateUpdateDto createDto = new CarStoreCreateUpdateDto();
        createDto.setCarMarkModelId(carMarkModel.getId());
        createDto.setCarModelEntityId(carModelEntity.getId());
        createDto.setMileage(50100);
        createDto.setYearOfIssue(TimeUtils.now().plusYears(1));
        createDto.setPrice(new BigDecimal(900));

        JsonNode json = Json.toJson(createDto);

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(PUT)
                .uri(CAR_STORE_URI.concat("/").concat(String.valueOf(carStoreModel.getId()))).bodyJson(json);

        Result result = route(app, request);

        assertEquals(OK, result.status());

        CarStoreDto updated = objectMapper.readValue(contentAsString(result), CarStoreDto.class);

        assertNotNull(updated.getId());
        assertEquals(carStoreModel.getId(), updated.getId());
        assertEquals(createDto.getCarMarkModelId(), updated.getCarMarkModel().getId());
        assertEquals(createDto.getCarModelEntityId(), updated.getCarModelEntity().getId());
        assertEquals(createDto.getPrice().intValue(), updated.getPrice().intValue());
        assertEquals(createDto.getYearOfIssue(), updated.getYearOfIssue());
        assertEquals(createDto.getMileage(), updated.getMileage());
    }

    /**
     * test: Delete Model - NoContent
     */
    @Test
    public void deleteModelNoContent() {
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(DELETE)
                .uri(CAR_STORE_URI.concat("/100500"));

        Result result = route(app, request);
        assertEquals(NO_CONTENT, result.status());
    }

    /**
     * test: Delete Model - OK
     */
    @Test
    public void deleteModelOk() {
        CarStoreMapper carStoreMapper = app.injector().instanceOf(CarStoreMapper.class);
        CarMarkMapper carMarkMapper = app.injector().instanceOf(CarMarkMapper.class);
        CarModelMapper carModelMapper = app.injector().instanceOf(CarModelMapper.class);

        //create car-model
        CarModelEntity carModelEntity = new CarModelEntity();
        carModelEntity.setName("2110");
        carModelEntity.setStartYear(TimeUtils.now());
        carModelEntity.setEndYear(TimeUtils.now());
        carModelEntity = carModelMapper.save(carModelEntity.getName(), carModelEntity.getStartYear(), carModelEntity.getEndYear());

        //create car-mark
        CarMarkModel carMarkModel = new CarMarkModel();
        carMarkModel.setName("2110");
        carMarkModel.setCountry("Ukraine");
        carMarkModel = carMarkMapper.save(carMarkModel);

        //create car-store
        CarStoreModel carStoreModel = new CarStoreModel();
        carStoreModel.setCarMarkModel(carMarkModel);
        carStoreModel.setCarModelEntity(carModelEntity);
        carStoreModel.setMileage(10500);
        carStoreModel.setPrice(new BigDecimal(200));
        carStoreModel.setYearOfIssue(TimeUtils.now());
        carStoreModel = carStoreMapper.save(carStoreModel.getCarMarkModel().getId(), carStoreModel.getCarModelEntity().getId(), carStoreModel.getYearOfIssue(),
                carStoreModel.getMileage(), carStoreModel.getPrice());

        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(DELETE)
                .uri(CAR_STORE_URI.concat("/").concat(String.valueOf(carStoreModel.getId())));

        Result result = route(app, request);

        assertEquals(OK, result.status());

        request = new Http.RequestBuilder()
                .method(GET)
                .uri(CAR_STORE_URI.concat("/").concat(String.valueOf(carStoreModel.getId())));

        result = route(app, request);
        assertEquals(NO_CONTENT, result.status());
    }

    /**
     * test: Search - OK
     */
    @Test
    public void search() {
        CarStoreMapper carStoreMapper = app.injector().instanceOf(CarStoreMapper.class);
        CarMarkMapper carMarkMapper = app.injector().instanceOf(CarMarkMapper.class);
        CarModelMapper carModelMapper = app.injector().instanceOf(CarModelMapper.class);

        //create car-model
        CarModelEntity carModelEntity = new CarModelEntity();
        carModelEntity.setName("Gazel");
        carModelEntity.setStartYear(TimeUtils.now());
        carModelEntity.setEndYear(TimeUtils.now());
        carModelEntity = carModelMapper.save(carModelEntity.getName(), carModelEntity.getStartYear(), carModelEntity.getEndYear());

        //create car-mark
        CarMarkModel carMarkModel = new CarMarkModel();
        carMarkModel.setName("GAZ");
        carMarkModel.setCountry("Russia");
        carMarkModel = carMarkMapper.save(carMarkModel);

        //create car-store
        CarStoreModel carStoreModel = new CarStoreModel();
        carStoreModel.setCarMarkModel(carMarkModel);
        carStoreModel.setCarModelEntity(carModelEntity);
        carStoreModel.setMileage(9_000);
        carStoreModel.setPrice(new BigDecimal(1_000_000));
        carStoreModel.setYearOfIssue(TimeUtils.now());
        carStoreMapper.save(carStoreModel.getCarMarkModel().getId(), carStoreModel.getCarModelEntity().getId(), carStoreModel.getYearOfIssue(),
                carStoreModel.getMileage(), carStoreModel.getPrice());

        //check by price
        Http.RequestBuilder request = new Http.RequestBuilder()
                .method(GET)
                .uri(CAR_STORE_URI.concat("?price=1000000"));

        Result result = route(app, request);
        assertEquals(OK, result.status());

        //check by mileage
        request = new Http.RequestBuilder()
                .method(GET)
                .uri(CAR_STORE_URI.concat("?mileage=9000"));

        result = route(app, request);
        assertEquals(OK, result.status());

        //check by carMarkName
        request = new Http.RequestBuilder()
                .method(GET)
                .uri(CAR_STORE_URI.concat("?carMarkName=gaz"));

        result = route(app, request);
        assertEquals(OK, result.status());

        //check by carModelName
        request = new Http.RequestBuilder()
                .method(GET)
                .uri(CAR_STORE_URI.concat("?carModelName=gazel"));

        result = route(app, request);
        assertEquals(OK, result.status());
    }
}
