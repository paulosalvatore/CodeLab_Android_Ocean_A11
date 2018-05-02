package br.com.paulosalvatore.codelab_android_ocean_a11;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by paulo on 01/05/2018.
 */

public interface GoogleMapsService {
	String key = "AIzaSyBXsvWOGTrufcD2fdNTPNldIPa-Fjwm2jo";

	@GET("geocode/json?key=" + key)
	Call<ResultadoMaps> buscarEndereco(
			@Query("address") String address
	);
}
