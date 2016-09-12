package com.cravings.network;

import com.cravings.data.Menu;
import com.cravings.data.MenuItem;
import com.cravings.data.Restaurant;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;


/**
 * Created by mremondi on 8/1/16.
 */
public interface CraveAPI {
    @GET("restaurants")
    Call<List<Restaurant>> getRestaurants();

    @GET("restaurants/{id}")
    Call<Restaurant> getRestaurantById(@Path("id") String id);

    @GET("restaurants/search/{query}")
    Call<Restaurant> searchRestaurants(@Path("query") String query);

    @GET("menus")
    Call<Menu> getMenus();

    @GET("menus/{id}")
    Call<Menu> getMenuById(@Path("id") String id);

    @GET("menus_items/{id}")
    Call<List<MenuItem>> getMenuItems(@Path("id") String id);

    @GET("items")
    Call<List<MenuItem>> getItems();

    @GET("items/{id}")
    Call<MenuItem> getItemById(@Path("id") String id);

    @GET("items/search/{query}")
    Call<List<MenuItem>> searchItems(@Path("query") String query);

    @GET("items/search/{menu_id}")
    Call<List<MenuItem>> searchForMenuItems(@Path("menu_id") String menu_id);
}
