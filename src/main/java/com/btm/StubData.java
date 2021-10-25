/****
 * Class Name: StubData
 * Author: Suresh & Krishna Praveen
 * Desc : This class generate stub data for withdrawal transactions
 *        which will be used by LogGenerator class.
 */
package com.btm;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.concurrent.ThreadLocalRandom;

public class StubData {

	static ArrayList<String> deviceDataList = new ArrayList<String>();
	static List<String> responseCodes = new ArrayList<String>();
	static List<String> issuerRegionCodes = new ArrayList<String>();
	static List<String> binCardGroups = new ArrayList<String>();

	/**
	 * This method generates 10 digit card number.
	 * @return cardNumber
	 */
	public static String generateCardNumber() {
		long smallestNumber = 1000000000L;
		long biggestNumber = 9999999999L;
		Long cardNumber = ThreadLocalRandom.current().nextLong(smallestNumber, biggestNumber + 1);
		return ""+cardNumber;
	}
	
	/**
	 * This method does initial setup of stubdata
	 */
	public static void setupStubData() {
		loadDevicesData();
		loadResponseCodes();
		loadBinCardGroups();
		loadIssuerCodes();
	}

	/**
	 * This method adds all stub response codes into
	 * responseCodes arraylist.
	 */
	public static void loadResponseCodes() {
		responseCodes.add("00");
		responseCodes.add("03");
		responseCodes.add("04");
		responseCodes.add("05");
		responseCodes.add("14");
		responseCodes.add("33");
		responseCodes.add("41");
		responseCodes.add("43");
		responseCodes.add("51");
		responseCodes.add("54");
		responseCodes.add("55");
		responseCodes.add("57");
		responseCodes.add("61");
		responseCodes.add("62");
		responseCodes.add("65");
		responseCodes.add("66");
		responseCodes.add("67");
		responseCodes.add("75");
		responseCodes.add("76");
		responseCodes.add("91");
		responseCodes.add("Q1");
		responseCodes.add("Q2");
	}

	/**
	 * This method adds all the different issuer codes
	 * into issuerRegionCodes arraylist.
	 */
	public static void loadIssuerCodes() {
		/* Region codes which approved the txns like Visa, Link, Master, Own host */
		issuerRegionCodes.add("28"); // lloyds
		issuerRegionCodes.add("76"); // BOS
		issuerRegionCodes.add("78"); // Halifax
		issuerRegionCodes.add("VI");
		issuerRegionCodes.add("LN");
		issuerRegionCodes.add("EP");

	}
	
	/**
	 * This method adds all the different bincard groups
	 * into binCardGroups arraylist.
	 */
	public static void loadBinCardGroups() {
		binCardGroups.add("446259_LYDS"); 
		binCardGroups.add("446261_LYDS"); 
		binCardGroups.add("492181_LYDS"); 
		binCardGroups.add("446272_LYDS"); 
		binCardGroups.add("446274_LYDS");
		
		binCardGroups.add("446278_HAL"); 
		binCardGroups.add("446291_HAL");
		binCardGroups.add("472411_HAL"); 
		binCardGroups.add("472411_HAL"); 
		binCardGroups.add("454103_HAL"); 
		
		binCardGroups.add("446204_BOS"); 
		binCardGroups.add("446271_BOS");
		binCardGroups.add("446292_BOS");
		binCardGroups.add("475732_BOS");
		binCardGroups.add("476221_BOS");
		
		binCardGroups.add("557483_LINK"); 
		binCardGroups.add("535733_LINK");
		binCardGroups.add("522497_LINK");
		binCardGroups.add("535778_LINK");
		binCardGroups.add("535744_LINK");
		
		binCardGroups.add("476134_VISA"); 
		binCardGroups.add("412370_VISA");
		binCardGroups.add("417234_VISA");
		binCardGroups.add("405106_VISA");
		binCardGroups.add("476173_VISA");
		
		binCardGroups.add("538675_MC");
		binCardGroups.add("540002_MC");
		binCardGroups.add("540537_MC");
		binCardGroups.add("541166_MC");
		binCardGroups.add("542515_MC");
	}

	/**
	 * This method loads the devices information from DeviceData.txt
	 * file into deviceDataList arraylist.
	 */
	public static void loadDevicesData() {
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
		}
	}
	
	/**
	 * This method generates random number and then retrieves devicedata from arraylist.
	 * @return deviceData
	 */
	public static String[] getDeviceData() {
		String deviceData = deviceDataList.get(generateRandomNumber(StubData.deviceDataList.size()));
		StringTokenizer st = new StringTokenizer(deviceData, "_");
		String deviceInfo[] = new String[3];
		deviceInfo[0] = st.nextToken();
		deviceInfo[1] = st.nextToken();
		deviceInfo[2] = st.nextToken();
		return deviceInfo;
	}
	
	/**
	 * This method generates random number and then retrieves response code from
	 * responseCodes arraylist.
	 * @return responseCode
	 */
	public static String getResponseCode() {
		String responseCode = responseCodes.get(generateRandomNumber(StubData.responseCodes.size()));
		return responseCode;
	}
	
	/**
	 * This method generates random number and then retrieves issuer region code from
	 * issuerRegionCodes arraylist.
	 * @return issuerCode
	 */
	public static String getIssuerCode() {
		String issuerCode = issuerRegionCodes.get(generateRandomNumber(StubData.issuerRegionCodes.size()));
		return issuerCode;
	}
	
	/**
	 * This method generates random number and then retrieves binCardGroup from
	 * binCardGroups arraylist.
	 * @return binCardGroup
	 */
	public static String getBinCardGroup() {
		String binCardGroup = binCardGroups.get(generateRandomNumber(StubData.binCardGroups.size()));
		StringTokenizer st = new StringTokenizer(binCardGroup, "_");
		binCardGroup = st.nextToken();
		return binCardGroup;
	}

	/**
	 * This method generates random amount
	 * @return amount
	 */
	public static String getAmount() {
		String amount = "0000000" + ((new Random().nextInt((99 - 15) + 1)) * 10) + "00";
		return amount;
	}

	/**
	 * This method generats 12 digit random RRN number
	 * @return rrrNumber
	 */
	public static String generateRRNNumber() {
		long smallestRRNNumber = 1000_0000_0000L;
		long biggestRRNNumber = 9999_9999_9999L;
		Long rrrNumber = ThreadLocalRandom.current().nextLong(smallestRRNNumber, biggestRRNNumber + 1);
		return ""+rrrNumber;

	}
	
	/**
	 * This method generates random number based on input value which is 
	 * considered as maximum limit for generating random number.
	 * @param maxRandomNumber
	 * @return
	 */
	private static int generateRandomNumber(int maxRandomNumber) {
		Random rand = new Random();
		int randomNumber = rand.nextInt(maxRandomNumber);
		return randomNumber;
	}
}
