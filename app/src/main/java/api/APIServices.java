package api;

import com.endproject.endproject.Cars;
import com.endproject.endproject.Search;
import com.endproject.endproject.UserLogin;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIServices {
    String baseLink="http://192.168.121.3:4400/api/";
    Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    APIServices apiServices =new Retrofit.Builder()
            .baseUrl(baseLink)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(APIServices.class);
    @GET("car/list")
    Call<ArrayList> homeValues();
    @GET("car/list/{list}")
    Call<ArrayList> listValues(@Path("list") int list);
    @GET("car/company")
    Call<ResponseBody> companyValues();
    @GET("car/cars-of-company/{id}")
    Call<Cars> updateDataHomeValues(@Path("id") String id);
    @GET("car/find/{name}")
    Call<ArrayList> searchValues(@Path("name") String name);

    @FormUrlEncoded
    @POST("user/login")
    Call<ResponseBody> login(@Field("email") String email,@Field("password") String pass);
    @FormUrlEncoded
    @POST("user/get-user")
    Call<ResponseBody> getUser(@Field("id") String id);
    @FormUrlEncoded
    @POST("car/toggle-liked")
    Call<ResponseBody> toggleLike(@Field("idCar") String id,@Field("idUser") String idUser );

    @FormUrlEncoded
    @POST("car/sign-up")
    Call<ResponseBody> signUp(@Field("name") String name,@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST("user/change-password")
    Call<ResponseBody> changePassword(@Field("id") String id,@Field("oldPassword") String oldPassword, @Field("newPassword") String newPassword);

    @FormUrlEncoded
    @POST("user/update")
    Call<ResponseBody> updateUser(@Field("id") String id,@Field("name") String name, @Field("phoneNumber") String phoneNumber, @Field("sex") String sex);
}
