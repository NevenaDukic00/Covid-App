package interfaces;

import enums.Gender;
import enums.Vakcinisan;

public interface RegistratitonInterface {

	public void showDialog(String username,String password, String first_name, String last_name, String email, String jmbg, Gender gender);
	public void back();
}
