package controllers;

import play.mvc.Http;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public interface CrudController {

    CompletionStage<Result> getById(Long id);

    CompletionStage<Result> create(Http.Request request);

    CompletionStage<Result> update(Long id, Http.Request request);

    CompletionStage<Result> deleteById(Long id);
}
