package com.codeutils.view.beans;

import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Named("temperatureBean")
@SessionScoped
public class TemperatureManager implements Serializable {

	private static final String API_URL = "http://localhost:8080/ChangeTemperature/api/temperature";
	/**
	 * 
	 */
	private static final long serialVersionUID = 6585793306322734606L;
	private String temperatureConversionUnits; // SOURCE_DESTINATION FORMAT
	private double inputTemperature;
	private String sourceTemperature;
	private String destinationTemperature;
	private List<SelectItem> temperatureItems;
	private String responseFromRest;

	public List<SelectItem> getTemperatureItems() {
		return temperatureItems;
	}

	public void setTemperatureItems(List<SelectItem> temperatureItems) {
		this.temperatureItems = temperatureItems;
	}

	public String getSourceTemperature() {
		return sourceTemperature;
	}

	public void setSourceTemperature(String sourceTemperature) {
		this.sourceTemperature = sourceTemperature;
	}

	@PostConstruct
	public void init() {
		temperatureItems = new ArrayList<SelectItem>();
		SelectItem item1 = new SelectItem("CELSIUS", "CELSIUS");
		SelectItem item2 = new SelectItem("KELVIN", "KELVIN");
		SelectItem item3 = new SelectItem("FARENHEIT", "FARENHEIT");
		temperatureItems.add(item1);
		temperatureItems.add(item2);
		temperatureItems.add(item3);

	}

	public String getTemperatureConversionUnits() {
		return temperatureConversionUnits;
	}

	public void setTemperatureConversionUnits(String temperatureConversionUnits) {
		this.temperatureConversionUnits = temperatureConversionUnits;
	}

	public double getInputTemperature() {
		return inputTemperature;
	}

	public void setInputTemperature(double inputTemperature) {
		this.inputTemperature = inputTemperature;
	}

	public void callRest() {
		System.out.println("Called the submit again");
		System.out.println(this.sourceTemperature);
		System.out.println(this.destinationTemperature);
		System.out.println(this.inputTemperature);

		try {
			System.out.println("Calling via Jersey Client");
			Client client = Client.create();

			WebResource webResource = client.resource(API_URL);
			String input = "{\"sourceDestUnit\":\"" + this.sourceTemperature + "_" + this.destinationTemperature
					+ "\",\"temperatureValue\":" + this.inputTemperature + "}";
			ClientResponse response = webResource.header("Content-Type", "application/json")
					.header("Accept", "application/json").accept("application/json").post(ClientResponse.class, input);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}
			JsonReader jsonReader = Json.createReader(new StringReader(response.getEntity(String.class)));
			JsonObject jsonObject = jsonReader.readObject();
			responseFromRest = jsonObject.get("temperatureValue").toString();

			System.out.println("Output from Server .... \n");
			System.out.println(responseFromRest);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public String getDestinationTemperature() {
		return destinationTemperature;
	}

	public void setDestinationTemperature(String destinationTemperature) {
		this.destinationTemperature = destinationTemperature;
	}

	public static void main(String[] args) {

		JsonReader jsonReader = Json.createReader(new StringReader("{\"temperatureValue\":38.99}"));
		JsonObject jsonObject = jsonReader.readObject();
		System.out.println(jsonObject.get("temperatureValue").toString());

	}

	public String getResponseFromRest() {
		return responseFromRest;
	}

	public void setResponseFromRest(String responseFromRest) {
		this.responseFromRest = responseFromRest;
	}

}
