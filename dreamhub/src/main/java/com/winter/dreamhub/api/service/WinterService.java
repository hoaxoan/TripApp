package com.winter.dreamhub.api.service;

import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import com.squareup.okhttp.RequestBody;
import com.winter.dreamhub.api.service.model.Auth;
import com.winter.dreamhub.api.service.model.Category;
import com.winter.dreamhub.api.service.model.Entity;
import com.winter.dreamhub.api.service.model.EntitySnippets;
import com.winter.dreamhub.api.service.model.Guideline;
import com.winter.dreamhub.api.service.model.Guidelines;
import com.winter.dreamhub.api.service.model.Review;
import com.winter.dreamhub.api.service.model.Trip;
import com.winter.dreamhub.api.service.request.ReviewRequest;
import com.winter.dreamhub.api.service.request.VisitedPlacesRequest;
import com.winter.dreamhub.api.service.response.ReviewResponse;
import com.winter.dreamhub.api.service.response.VisitedPlacesResponse;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Map;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by hoaxoan on 12/3/2016.
 */

public interface WinterService {
    //    String ENDPOINT = "http://dreamhub-dgxstudio.rhcloud.com/";
    public static String ENDPOINT = "http://192.168.0.110:1337";
    String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss Z";

    int PER_PAGE_MAX = 100;
    int PER_PAGE_DEFAULT = 30;

    /* Trips */
    @GET("/trips")
    Call<List<Trip>> getTrips(@Query("_q") String query,
                              @Query("_start") Integer page,
                              @Query("_limit") Integer pageSize);

    @GET("trips/{id}")
    Call<Trip> getTrip(@Path("id") long id);

    /* Entity */
    @GET("entities")
    Call<List<Entity>> getEntities(@Query("trip_id") Long tripId,
                                      @Query("type") @RelatedType int type,
                                      @Query("_start") Integer page,
                                      @Query("_limit") Integer pageSize);

    @GET("entities")
    Call<List<Entity>> getEntitiesByMoods(@Query("trip_id") Long tripId,
                                       @Query("type") @RelatedType int type,
                                       @Query("mood_id") Long moodId,
                                       @Query("_start") Integer page,
                                       @Query("_limit") Integer pageSize);

    @GET("entities")
    Call<List<Entity>> getEntitiesByCategories(@Query("trip_id") Long tripId,
                                          @Query("type") @RelatedType int type,
                                          @Query("category_id") Long categoryId,
                                          @Query("_start") Integer page,
                                          @Query("_limit") Integer pageSize);

    @GET("entities/{id}")
    Call<Entity> getEntity(@Path("id") Long id);

    @GET("categories")
    Call<List<Category>> getCategoryByTypes(@Query("type") @CategoryType int type);

    /* Guidelines */
    @GET("guidelines")
    Call<Guidelines> getSubGuideline(@Query("trip_id") Long tripId,
                                     @Query("parent_id") long parentId);

    @GET("guidelines")
    Call<List<Guideline>> getParentGuideline(@Query("trip_id") Long tripId,
                                                    @Query("parent_id") long parentId);

    // Save Places
    @GET("visitedplaces")
    Call<List<Entity>> getSavePlaces(@Query("trip_id") Long tripId,
                                     @Query("user_id") Long userId);

    /* Searchs */
    @GET("searchs")
    Call<List<Entity>> search(@Query("_q") String query,
                              @Query("trip_id") Long tripId,
                              @Query("type") @RelatedType int type,
                              @Query("mood_id") Long moodId,
                              @Query("_start") Integer page,
                              @Query("_limit") Integer pageSize,
                              @Query("_sort") @SortOrder String sort);

    @GET("searchs/suggests.json")
    Call<EntitySnippets> suggest();

    /* Comments */
    @GET("reviews")
    Call<List<Review>> getReviews(@Query("entity_id") Long entityId,
                             @Query("_start") Integer page,
                             @Query("_limit") Integer pageSize);

    @POST("reviews")
    Call<ReviewResponse> postReview(@Body ReviewRequest request);

    /* Watch */
    @POST("/visitedplaces")
    Call<VisitedPlacesResponse> postWatch(@Body VisitedPlacesRequest request);

    @DELETE("/visitedplaces/{id}")
    Call<Void> postUnWatch(@Path("id") long id);

    /* Authentication */
    @FormUrlEncoded
    @POST("/auth/local")
    Call<Auth> login(@Field("identifier") String identifier,
                     @Field("password") String password);

    @FormUrlEncoded
    @POST("/auth/local/register")
    Call<Auth> register(@Field("username") String username,
                        @Field("email") String email,
                        @Field("password") String password);

    /**
     * magic constants
     **/
    // Sort
    String SORT_POPULAR = "id:asc";
    String SORT_RECENT = "name:asc";

    // Type
    public static int TYPE_TRIP = 1;
    public static int TYPE_RESTAURANT = 2;
    public static int TYPE_GUIDELINE = 3;
    public static int TYPE_LANDMARKS = 1;
    public static int TYPE_HOTELS = 2;
    public static int TYPE_RESTAURANTS = 3;

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            SORT_POPULAR,
            SORT_RECENT
    })
    @interface SortOrder {
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
        TYPE_TRIP,
        TYPE_RESTAURANT,
        TYPE_GUIDELINE
    })
    @interface CategoryType {
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            TYPE_LANDMARKS,
            TYPE_HOTELS,
            TYPE_RESTAURANTS
    })
    @interface RelatedType {
    }
}
