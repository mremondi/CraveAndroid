package com.cravings.network;

import com.cravings.data.Menu;
import com.cravings.data.MenuItem;
import com.cravings.data.Rating;
import com.cravings.data.Restaurant;
import com.cravings.data.User;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;


/**
 * Created by mremondi on 8/1/16.
 */
public interface CraveAPI {
    @GET("restaurants")
    Call<List<Restaurant>> getRestaurants();

    @GET("restaurants/{id}")
    Call<Restaurant> getRestaurantById(@Path("id") String id);

    @GET("restaurants/{latitude}/{longitude}")
    Call<List<Restaurant>> getNearbyRestaurants(@Path("latitude") Double lat, @Path("longitude") Double lon);


    @GET("restaurants/search/{query}")
    Call<List<Restaurant>> searchRestaurants(@Path("query") String query);

    @GET("menus")
    Call<Menu> getMenus();

    @GET("menus/{id}")
    Call<Menu> getMenuById(@Path("id") String id);

    @GET("menus_items/{id}")
    Call<List<MenuItem>> getMenuItems(@Path("id") String id);

    @GET("menus_items/{id}/{section}")
    Call<List<MenuItem>> getMenuItemsBySection(@Path("id") String id, @Path("section") String section);

    @GET("items")
    Call<List<MenuItem>> getItems();

    @PUT("items/{id}/{rating}/{user_id}")
    Call<MenuItem> rateItem(@Path("id") String id, @Path("rating") float rating, @Path("user_id") String user_id);

    @GET("items/{id}")
    Call<MenuItem> getItemById(@Path("id") String id);

    @GET("items/search/{query}")
    Call<List<MenuItem>> searchItems(@Path("query") String query);

    @GET("items/search/{query}/{filter}/{latitude}/{longitude}")
    Call<List<MenuItem>> searchItemsSorted(@Path("query") String query, @Path("latitude") Double lat,
                                           @Path("longitude") Double lon, @Path("filter") String filter);

    @GET("items/search/{menu_id}")
    Call<List<MenuItem>> searchForMenuItems(@Path("menu_id") String menu_id);

    @FormUrlEncoded
    @POST("login")
    Call<User> loginUser(@Field("email") String email, @Field(value = "password", encoded = true) String password);

    @GET("logout")
    Call<User> logOutUser();

    @FormUrlEncoded
    @POST("register")
    Call<User> registerUser(@Field("email") String email, @Field(value = "password", encoded = true) String password);

    @FormUrlEncoded
    @POST("updateUser")
    Call<User> updateUser(@Field("name") String name, @Field("email") String email, @Field("id") String id);

    @FormUrlEncoded
    @POST("updatePassword")
    Call<User> updatePassword(@Field(value = "password", encoded = true) String password, @Field("id") String user_id);

    @GET("ratings/{user_id}")
    Call<List<Rating>> getUserFavorites(@Path("user_id") String user_id);

    @GET("account/profile/{user_id}")
    Call<User> getUserProfile(@Path("user_id") String user_id);
}
