package dev.visum.demoapp.data.api;

import com.google.gson.JsonObject;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dev.visum.demoapp.model.AddSalePrestResponseModel;
import dev.visum.demoapp.model.CustomerResponseModel;
import dev.visum.demoapp.model.MySaleModel;
import dev.visum.demoapp.model.ProductResponseModel;
import dev.visum.demoapp.model.ResponseAddClientModel;
import dev.visum.demoapp.model.ResponseModel;
import dev.visum.demoapp.model.SaleAddedResponseModel;

import dev.visum.demoapp.model.SurveyQuestionResponseModel;
import dev.visum.demoapp.model.SurveyResponseModel;

import dev.visum.demoapp.model.UserAgentResponseModel;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface GetDataService {

    @POST("login")
    Call<ResponseModel<UserAgentResponseModel>> loginUser(@NotNull @QueryMap Map<String, String> userAgentBodyModel);

    @GET("product")
    Call<ResponseModel<List<ProductResponseModel>>> getProductsList();

    @GET("products/find")
    Call<ResponseModel<List<ProductResponseModel>>> getProductFilteredList(@NotNull @Query("input") String input);

    @GET("inquiry")
    Call<ResponseModel<List<SurveyResponseModel>>> getSurveysList();

    @GET("link_question")
    Call<ResponseModel<List<SurveyQuestionResponseModel>>> getSurveyQuestionsList(@NotNull @Query("inquire_id") String input);

    @GET("client/find")
    Call<ResponseModel<List<CustomerResponseModel>>> getClientFilteredList(@NotNull @Query("input") String input);

    @POST("store_sale")
    Call<SaleAddedResponseModel> postSale(@NotNull @QueryMap Map<String, String> saleCreatedModel);

    @POST("store_prestation")
    Call<AddSalePrestResponseModel> postNextPrestSale(@NotNull @QueryMap Map<String, String> nextPrestSaleCreatedModel);

    @GET("sales")
    Call<ResponseModel<ArrayList<MySaleModel>>> getMySales(@NotNull @Query("input") String input);

    @Multipart
    @POST("client/signature")
    Call<JsonObject> uploadDigitalSignatureImage(@Part MultipartBody.Part file, @NotNull @Query("id") String input);

    @POST("clients/store")
    Call<ResponseAddClientModel> postAddClient(@NotNull @QueryMap Map<String, String> addClientModel);
}
