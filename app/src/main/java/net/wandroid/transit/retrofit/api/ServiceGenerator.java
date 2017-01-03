package net.wandroid.transit.retrofit.api;

import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ServiceGenerator {

    /**
     * Create a retrofit service
     * @param serviceClass the service class
     * @param url the base url
     * @param interceptor interceptor. If null it will be ignored
     * @param <T> the service type
     * @return the service
     */
    public static <T> T createService(Class<T> serviceClass, String url, @Nullable Interceptor interceptor) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        if (interceptor != null) {
            httpClientBuilder.addInterceptor(interceptor);
        }

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(url)
                .client(httpClientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create());
        return builder.build().create(serviceClass);
    }

    /**
     * Interceptor that will not call http but instead use provided inputstream
     * as body. This is used for unit testing local files with retrofit
     * and early development before access to http api.
     */
    public static class LocalMockInterceptor implements Interceptor {

        private static final String LOCAL_MIME_TYPE = "application/json";
        private static final int HTTP_OK = 200;
        private final InputStream mInputStream;

        public LocalMockInterceptor(InputStream inputStream) {
            mInputStream = inputStream;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            return new Response.Builder()
                    .addHeader("content-type", LOCAL_MIME_TYPE)
                    .body(ResponseBody.create(MediaType.parse(LOCAL_MIME_TYPE), convertToString(mInputStream)))
                    .code(HTTP_OK)
                    .message("Response from local file")
                    .protocol(Protocol.HTTP_1_1)
                    .request(chain.request())
                    .build();
        }

        /**
         * Method that converts an inputstream to a string
         *
         * @param inputStream the inputstream
         * @return the stream as a String with new lines removed.
         * @throws IOException
         */
        private String convertToString(InputStream inputStream) throws IOException {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder("");
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
    }

}
