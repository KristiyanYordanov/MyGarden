package com.example.mygarden.bean;

import java.util.Arrays;

public class Plant {
	@Override
	public String toString() {
		return "Plant [_id=" + _id + ", plantName=" + plantName
				+ ", plantType=" + plantType + ", plantLocation="
				+ plantLocation + ", plantImage=" + Arrays.toString(plantImage)
				+ "]";
	}

	int _id;
	String plantName;
	String plantType;
	String plantLocation;
	byte[] plantImage;

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getPlantName() {
		return plantName;
	}

	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}

	public String getPlantType() {
		return plantType;
	}

	public void setPlantType(String plantType) {
		this.plantType = plantType;
	}

	public String getPlantLocation() {
		return plantLocation;
	}

	public void setPlantLocation(String plantLocation) {
		this.plantLocation = plantLocation;
	}

	public byte[] getPlantImage() {
		return plantImage;
	}

	public void setPlantImage(byte[] plantImage) {
		this.plantImage = plantImage;
	}

	public Plant() {
		super();
	}

	public Plant( String plantName, String plantType,
			String plantLocation, byte[] plantImage) {
		super();
		this.plantName = plantName;
		this.plantType = plantType;
		this.plantLocation = plantLocation;
		this.plantImage = plantImage;
	}

}
