package entity;

import enums.Vakcinisan;

public class User {


	private String doza;
	private String answer;
	private String vakcina;
	private String datum = "nije unesen";
	
	
	


	public User(String information[]) {
		this.answer =information[0];
		this.vakcina = information[1];
		
	}


	public String getDatum() {
		return datum;
	}


	public void setDatum(String datum) {
		this.datum = datum;
	}


	public String getDoza() {
		return doza;
	}


	public void setDoza(String doza) {
		this.doza = doza;
	}


	public String getAnswer() {
		return answer;
	}


	public void setAnswer(String answer) {
		this.answer = answer;
	}


	public String getVakcina() {
		return vakcina;
	}


	public void setVakcina(String vakcina) {
		this.vakcina = vakcina;
	}
	
	
}