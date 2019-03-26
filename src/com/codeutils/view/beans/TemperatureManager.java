package com.codeutils.view.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.model.SelectItem;
import javax.inject.Named;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

@Named("temperatureBean")
@SessionScoped
public class TemperatureManager implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6585793306322734606L;
	private String temperatureConversionUnits; // SOURCE_DESTINATION FORMAT
	private double inputTemperature;
	private String sourceTemperature;
	private String destinationTemperature;
	private List<SelectItem> temperatureItems;

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
		System.out.println("Called the submit");
		System.out.println(this.sourceTemperature);
		System.out.println(this.destinationTemperature);
		System.out.println(this.inputTemperature);

		try {

			Client client = Client.create();

			WebResource webResource = client.resource("http://localhost:8080/ChangeTemperature/api/temperature");
			String input = "{\"sourceDestUnit\":\"" + this.sourceTemperature + "_" + this.destinationTemperature
					+ "\",\"temperatureValue\":" + this.inputTemperature + "}";
			ClientResponse response = webResource.header("Content-Type", "application/json")
					.header("Accept", "application/json").accept("application/json").post(ClientResponse.class, input);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			String output = response.getEntity(String.class);

			System.out.println("Output from Server .... \n");
			System.out.println(output);

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
		try {

			Client client = Client.create();

			WebResource webResource = client.resource("http://localhost:8080/ChangeTemperature/api/temperature");
			String input = "{\"sourceDestUnit\":\"" + "CELSIUS" + "_" + "KELVIN" + "\",\"temperatureValue\":" + 38.99
					+ "}";

			ClientResponse response = webResource.header("Content-Type", "application/json")
					.header("Accept", "application/json").accept("application/json").post(ClientResponse.class, input);

			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			}

			String output = response.getEntity(String.class);

			System.out.println("Output from Server .... \n");
			System.out.println(output);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
