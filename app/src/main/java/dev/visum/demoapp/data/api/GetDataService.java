package dev.visum.demoapp.data.api;

import org.jetbrains.annotations.NotNull;

import dev.visum.demoapp.model.ResponseModel;
import dev.visum.demoapp.model.UserAgentResponseModel;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GetDataService {

    @POST("login")
    Call<ResponseModel<UserAgentResponseModel>> loginUser(@NotNull @Query("email") String email, @NotNull @Query("password") String password);
}
