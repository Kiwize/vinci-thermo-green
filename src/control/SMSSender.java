package control;

import com.vonage.client.VonageClient;
import com.vonage.client.sms.MessageStatus;
import com.vonage.client.sms.SmsSubmissionResponse;
import com.vonage.client.sms.messages.TextMessage;

public class SMSSender {

	private VonageClient vonageClient;
	private Controller controller;

	/**
	 * <p>
	 * Uses Vonage client API to send SMS.
	 * </p>
	 * 
	 * @author Thomas PRADEAU
	 */
	public SMSSender(Controller controller) {
		this.controller = controller;
		vonageClient = VonageClient.builder()
				.apiKey(controller.getConfigManager().getProperties().getProperty("api.api_key"))
				.apiSecret(controller.getConfigManager().getProperties().getProperty("api.api_secret")).build();
	}

	/**
	 * Sends SMS using Vonage API and the provided API keys from the loaded
	 * properties file.
	 * 
	 * @param message
	 */
	public void sendMessage(String message) {
		TextMessage textMessage = new TextMessage("Vinci Thermo Green",
				controller.getConsoleGui().getCurrentStadium().getContactNumber(),
				message.isEmpty() ? "Default Message" : message);

		SmsSubmissionResponse response = vonageClient.getSmsClient().submitMessage(textMessage);

		if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
			System.out.println("Message sent successfully.");
		} else {
			System.out.println("Message failed with error: " + response.getMessages().get(0).getErrorText());
		}
	}

}
