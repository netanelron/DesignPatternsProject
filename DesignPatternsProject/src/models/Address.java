package models;

public class Address{
	private String address;
	public Address(String address) {
		this.address= address;
	}
	
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address=address;
    }
    
    public static boolean isValid(String address) {
		return address.matches("[0-9a-zA-Z]+");
	}

}
