package io.indico.network;

import java.util.Map;

import retrofit.http.Body;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Chris on 8/13/15.
 */
public interface IndicoService {
    @POST("/{api}")
    void call(
        @Path("api") String api,
        @Body Map<String, Object> data,
        @Query("key") String key,
        @Query("version") Integer version,
        IndicoCallback<Map<String, Object>> callback
    );

    @POST("/{api}/batch")
    void batch_call(
        @Path("api") String api,
        @Body Map<String, Object> data,
        @Query("key") String key,
        @Query("version") Integer version,
        IndicoCallback<Map<String, Object>> callback
    );

    @POST("/apis/{api}")
   void multi_call(
        @Path("api") String api,
        @Body Map<String, Object> data,
        @Query("apis") String apis,
        @Query("key") String key,
        IndicoCallback<Map<String, Object>> callback
    );

    @POST("/apis/{api}/batch")
    void multi_batch_call(
        @Path("api") String api,
        @Body Map<String, Object> data,
        @Query("apis") String apis,
        @Query("key") String key,
        IndicoCallback<Map<String, Object>> callback
    );
}
