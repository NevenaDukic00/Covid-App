package entity;

public class User_Admin {

	private String jmbg;
	private String validna_propusnica;
	private String first_name;
	private String last_name;
	private String number;
	
	public String getFirst_name() {
		return first_name;
	}

	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	

	public User_Admin(String jmbg, String validna_propusnica, String first_name, String last_name, String number) {
		super();
		this.jmbg = jmbg;
		this.validna_propusnica = validna_propusnica;
		this.first_name = first_name;
		this.last_name = last_name;
		this.number = number;
	}

	public String getJmbg() {
		return jmbg;
	}

	public void setJmbg(String jmbg) {
		this.jmbg = jmbg;
	}

	public String getValidna_propusnica() {
		return validna_propusnica;
	}

	public void setValidna_propusnica(String validna_propusnica) {
		this.validna_propusnica = validna_propusnica;
	}
	
}
