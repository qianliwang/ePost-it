package test.console;

public class TestProvider {

	public static void main(String args[]){
	
		Provider provider = Provider.getInstance();
		String message = "123:42,-93";

		provider.sendMessage(message);
	}
	
}
