package br.com.paulosalvatore.codelab_android_ocean_a11;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by paulo on 01/05/2018.
 */

public class ResultadoMaps {
	@SerializedName(value = "results")
	private List<Endereco> enderecos;

	@SerializedName(value = "status")
	private String status;

	public List<Endereco> getEnderecos() {
		return enderecos;
	}

	public String getStatus() {
		return status;
	}
}

class Endereco {
	@SerializedName(value = "formatted_address")
	private String endereco;

	@SerializedName(value = "geometry")
	private EnderecoGeometry geometria;

	public String getEndereco() {
		return endereco;
	}

	public EnderecoGeometry getGeometria() {
		return geometria;
	}
}

class EnderecoGeometry {
	@SerializedName(value = "location")
	private EnderecoLocalizacao localizacao;

	public EnderecoLocalizacao getLocalizacao() {
		return localizacao;
	}
}

class EnderecoLocalizacao {
	@SerializedName(value = "lat")
	private double latitude;

	@SerializedName(value = "lng")
	private double longitude;

	public double getLatitude() {
		return latitude;
	}

	public double getLongitude() {
		return longitude;
	}
}
