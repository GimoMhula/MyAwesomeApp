package dev.visum.demoapp.data.api;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

import dev.visum.demoapp.model.CustomerResponseModel;
import dev.visum.demoapp.model.ProductResponseModel;
import dev.visum.demoapp.model.ResponseModel;
import dev.visum.demoapp.model.SaleAddedResponseModel;
import dev.visum.demoapp.model.SaleCreatedModel;
import dev.visum.demoapp.model.UserAgentBodyModel;
import dev.visum.demoapp.model.UserAgentResponseModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface GetDataService {

    @POST("login")
    Call<ResponseModel<UserAgentResponseModel>> loginUser(@NotNull @QueryMap Map<String, String> userAgentBodyModel);

    @GET("product")
    Call<ResponseModel<List<ProductResponseModel>>> getProductsList();

    @GET("products/find")
    Call<ResponseModel<List<ProductResponseModel>>> getProductFilteredList(@NotNull @Query("input") String input);

    @GET("client/find")
    Call<ResponseModel<List<CustomerResponseModel>>> getClientFilteredList(@NotNull @Query("input") String input);

    @POST("sale_product")
    Call<SaleAddedResponseModel> postSale(@NotNull @QueryMap Map<String, String> saleCreatedModel);
}
