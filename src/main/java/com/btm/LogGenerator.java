/****
 * Class Name: LogGenerator
 * Author: Suresh & Krishna Praveen
 * Desc : This class generates dummy request and response pairs 
 *        for withdrawal transactions.
 */

package com.btm;

import java.io.IOException;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class LogGenerator {

	public static void main(String[] args) throws IOException {

		try {

			// Sets up all the stub data.
			StubData.setupStubData();

			// Generate dummy request and response pairs in logs - infinite times
			while (true) {
				String deviceData[] = StubData.getDeviceData();
				String binCardGroup = StubData.getBinCardGroup();
				String cardNumber = binCardGroup + StubData.generateCardNumber();
				String amount = StubData.getAmount();
				String responseCode = StubData.getResponseCode();
				String issuerCode = StubData.getIssuerCode();
				String rrnNumber = StubData.generateRRNNumber();

				/*
				 * Request Generation with all mandatory data
				 * Date&Time|DeviceID|Out|requestCode|CardNumber|ProcessingCode|Amount|RRNNumber
				 * |LineAddress|Brand
				 */
				String request = java.time.LocalDateTime.now() + "|" + deviceData[0] + "|" + "Out" + "|" + "0200"
						+ cardNumber + "010000" + amount + rrnNumber + deviceData[1] + deviceData[2] + "\n";
				
				System.out.println("ATM Request:" + request);

				Thread.sleep(500);

				/*
				 * Response Generation with all mandatory data
				 * Date&Time|DeviceID|In|responseCode|CardNumber|ProcessingCode|Amount|
				 * ResponseCode|Line address|Number of digits| Issuer Code
				 */
				String response = java.time.LocalDateTime.now() + "|" + deviceData[0] + "|" + "In" + "|" + "0210"
						+ cardNumber + "010000" + amount + responseCode + deviceData[1] + "002" + issuerCode + "\n";

				System.out.println("ATM Response:" + response);

				Thread.sleep(500);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception in main class is:" + e.getMessage());
		}

		finally {
		}
	}

	@RequestMapping("/ATM")
	String home() {
		System.out.println("ATM Test !!!");
		return "ATM Req-Res Successfull";
	}

}
