package com.btm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class LogGenerator {
	//static Logger log = Logger.getLogger(LogGenerator.class.getName());
	private static final Logger logger = LoggerFactory.getLogger(LogGenerator.class);

	public static void main(String[] args) throws IOException {

		//Docker container
		String logFilePath = "/var/log/dummyapp/log-dummy.log";
		
		//Local
		//String logFilePath = "C:\\logs\\Output.log";
		
		int maxRandomNumber = 10; // default value
		ArrayList<String> deviceDataList = new ArrayList<String>();

		/* Card Number & Retrieval Reference Unique Number variables */
		long smallestCardNumber = 1000_0000_0000_0000L;
		long biggestCardNumber = 9999_9999_9999_9999L;
		long smallestRRNNumber = 1000_0000_0000L;
		long biggestRRNNumber = 9999_9999_9999L;

		/* Host Response codes */
		List<String> responseCodes = new ArrayList<String>();
		responseCodes.add("00");
		responseCodes.add("85");
		responseCodes.add("68");
		responseCodes.add("58");
		responseCodes.add("59");
		responseCodes.add("61");
		responseCodes.add("62");
		/* Region codes which approved the txns like Visa, Link, Master, Own host */
		List<String> issuerRegionCodes = new ArrayList<String>();
		issuerRegionCodes.add("28");
		issuerRegionCodes.add("VI");
		issuerRegionCodes.add("LN");
		issuerRegionCodes.add("FD");
		issuerRegionCodes.add("EP");

		try {
			
			InputStream stream1 = LogGenerator.class.getResourceAsStream("/DevicesData.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream1));

			String line = reader.readLine();
			while (line != null) {
				deviceDataList.add(line);
				line = reader.readLine();
			}
			reader.close();
			
		} catch (Exception e) {
			e.printStackTrace();
			// Handle a potential exception
			//log.error("Exception Occured :" + e);
		}

		File output = new File(logFilePath);
		FileWriter writer = new FileWriter(output);
		Random random = new Random();

		try {

			while (true) {
				int randomNumber = generateRandomNumber(maxRandomNumber);
				// String cardNumber = getCardNumber(cardnosList, randomNumber);
				String deviceData[] = getDeviceData(deviceDataList, randomNumber);
				/* Card Number Generation & Retrieval Reference Number generation */
				Long cardNumber = ThreadLocalRandom.current().nextLong(smallestCardNumber, biggestCardNumber + 1);
				Long RRNNumber = ThreadLocalRandom.current().nextLong(smallestRRNNumber, biggestRRNNumber + 1);
				String amount = "0000000" + ((new Random().nextInt((99 - 15) + 1))*10) + "00";
				/*
				 * Request Generation with all mandatory
				 * data|Date&Time|DeviceID|Out|requestCode|CardNumber|Processing
				 * Code|Amount|RRNNumber|LineAddress|Brand
				 */
				String request = java.time.LocalDateTime.now() + "|" + deviceData[0] + "|" + "Out" + "|" + "0200"
						+ cardNumber + "010000" + amount + RRNNumber + deviceData[1] + deviceData[2] + "\n";
				//log.info("Request Generated :" + request);
				/*
				 * Response Generation with all mandatory
				 * data|Date&Time|DeviceID|In|responseCode|CardNumber|Processing
				 * Code|Amount|ResponseCode from Host|Line address|Number of digits| Issuer Code
				 */
				String response = java.time.LocalDateTime.now() + "|" + deviceData[0] + "|" + "In" + "|" + "0210"
						+ cardNumber + "010000" + amount + responseCodes.get(random.nextInt(6)) + deviceData[1] + "002"
						+ issuerRegionCodes.get(random.nextInt(4)) + "\n";
				//log.info("Response Generated :" + response);
				//logger.info("ATM Request !!"+request);
				System.out.println("ATM Request:"+request);
				writer.write(request);
				Thread.sleep(500);
				//logger.info("ATM Response !!!"+response);
				System.out.println("ATM Response:"+response);
				writer.write(response);
				writer.flush();
				Thread.sleep(500);
			}
		} catch (Exception e) {
			e.printStackTrace();
			//log.error("Exception Occured :" + e);
		}

		finally {
			closeLogFile(writer);
		}
	}

	@RequestMapping("/ATM")
    String home() {
        logger.info("ATM TEST REQUEST !!!");
        System.out.println("ATM Test !!!");
        return "ATM Req-Res Successfull";
    }

	private static int generateRandomNumber(int maxRandomNumber) {

		Random rand = new Random();
		int randomNumber = rand.nextInt(maxRandomNumber);
		return randomNumber;

	}

	
	private static String[] getDeviceData(ArrayList<String> devicesDataList, int randomNumber) {

		String deviceData = devicesDataList.get(randomNumber);
		StringTokenizer st = new StringTokenizer(deviceData, "_");
		String deviceInfo[] = new String[3];
		deviceInfo[0] = st.nextToken();
		deviceInfo[1] = st.nextToken();
		deviceInfo[2] = st.nextToken();

		return deviceInfo;

	}

	private static void closeLogFile(FileWriter writer) {
		try {
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			//log.error("Exception Occured :" + e);
		}
	}
}
