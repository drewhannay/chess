package gui;

import java.io.Serializable;

public class TestPacket implements Serializable {
	
	private static final long serialVersionUID = -4060661054871293804L;
	
	private String message;
	
	public TestPacket(String message){
		this.message = message;
	}
	
	public String toString(){
		return message;
	}

}
