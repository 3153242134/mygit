package comp.netease.statistics;

import java.io.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

	public static final String DEFAULT_DEST = "D:/result01";

	static SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static void main(String[] args) throws ParseException, Exception {
		// TODO Auto-generated method stub
//		stat("07");
//		stat("08");
//		stat("09");
//		stat("10");
//	stat("11");
//	stat("12");
		
//		merge("07");
//		merge("08");
//		merge("09");
//		merge("10");
//		merge("11");
//		merge("12");
		
		int scale = 6 * 5;
		simplify("07",scale);
		simplify("08",scale);
		simplify("09",scale);
		simplify("10",scale);
		simplify("11",scale);
		simplify("12",scale);
//		String line = "[INFO ]2016-11-07 00:00:01,606,[DefaultFutureCallBack], trace=2312699103.1478448001614.5039.6, action=/xhr/list/newItem.json, urlparams={\"size\":\"10\",\"itemId\":\"1061001\",\"sortType\":\"0\",\"descSorted\":\"0\",\"categoryL1Id\":\"0\"} finished, result=200, costs=6, totalCosts=6";
//		System.out.println(getCosts(line));
	}
	static String regexStr = ".*[^a-zA-Z]costs=([0-9]*).*";
	static Pattern r = Pattern.compile(regexStr);
	static int getCosts(String line) {
		Matcher m = r.matcher(line);
		if(!m.matches()) {
			System.err.println("error matches , line : " + line);
			return 0;
		}
		String str = m.group(1);
		try {
			int costs = Integer.parseInt(str);
			return costs;
		}catch (Exception e) {
			System.err.println("wrong matche!,str: " + str +" ,line : " + line);
			return 0;
		}

	}
	
	public static void stat(String date) throws IOException, ParseException {
		stat(null,"C:\\Users\\hzliyuhang\\Documents\\我的POPO\\yanxuan\\wzp-yanxuan-daily-2016-11-"+ date +".log",
				"C:\\Users\\hzliyuhang\\Documents\\我的POPO\\yanxuan\\result11-"+ date +"-0","2016-11-"+ date);
		stat(null,"C:\\Users\\hzliyuhang\\Documents\\我的POPO\\yanxuan\\wzp-yanxuan-daily-2016-11-"+ date +".log-2",
				"C:\\Users\\hzliyuhang\\Documents\\我的POPO\\yanxuan\\result11-"+ date +"-1","2016-11-"+ date);
		stat(null,"C:\\Users\\hzliyuhang\\Documents\\我的POPO\\yanxuan\\wzp-yanxuan-daily-2016-11-"+ date +".log-3",
				"C:\\Users\\hzliyuhang\\Documents\\我的POPO\\yanxuan\\result11-"+ date +"-2","2016-11-"+ date);
		System.out.println(date + " complete");
	}
	
	public static void merge(String date) throws IOException {
		gatherFile(new String[]{"C:\\Users\\hzliyuhang\\Documents\\我的POPO\\yanxuan\\result11-"+ date +"-0"
								,"C:\\Users\\hzliyuhang\\Documents\\我的POPO\\yanxuan\\result11-"+ date +"-1"
								,"C:\\Users\\hzliyuhang\\Documents\\我的POPO\\yanxuan\\result11-"+ date +"-2"},
					"C:\\Users\\hzliyuhang\\Documents\\我的POPO\\yanxuan\\result" + date);
		
	}
	
	public static void simplify(String date,int scale) throws ParseException, Exception {
		gatherTime("C:\\Users\\hzliyuhang\\Documents\\我的POPO\\yanxuan\\result" + date,
				"C:\\Users\\hzliyuhang\\Documents\\我的POPO\\yanxuan\\simple" + date, scale , f.parse("2016-11-" + date + " 00:00:00"));
	}
	
	
	

	
	public static void stat(Date d,String path,String dest,String date) throws IOException, ParseException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)) );
		FileWriter writer = new FileWriter(dest, false);
		String line = null;
		String time = date + " 00:00:0";
		int count = 0;
		int costs = 0;
		while((line = br.readLine() ) != null ) {

			if(!line.contains(date)) {
				continue;
			}
			if(line.contains(time) ) {
				count++;
				costs += getCosts(line);
			}
			else {
				String last = time;
				BigDecimal countb = new BigDecimal(count);
				BigDecimal costsb = new BigDecimal(costs);
				BigDecimal avg = costsb.divide(countb, 2, BigDecimal.ROUND_HALF_EVEN);
				writer.write(time + "0\r" + count + "\r" + costs + "\r" + avg.toString() + "\r");
				int start = line.indexOf(date);
				if(start == -1) {
					while((line = br.readLine()) != null) {
						if(line.indexOf(date) != -1)
							break;
					}
					start = line.indexOf(date);
					if(start == -1) {
						System.out.println("warn");
						
						break;
					}
						
				}
				time = line.substring(start, start + 18);		
				if(!time.equals(last)) {
					count = 1;
					costs = getCosts(line);
				}
			}
		}
		BigDecimal countb = new BigDecimal(count);
		BigDecimal costsb = new BigDecimal(costs);
		BigDecimal avg = costsb.divide(countb, 2, BigDecimal.ROUND_HALF_EVEN);
		writer.write(time + "0\r" + count + "\r" + costs + "\r" + avg.toString() + "\r");
		br.close();
		writer.close();
	}


	public static void gatherFile(String[] paths,String destFilepath) throws IOException {
		class Pack {
			int sum;
			int costs;
		}
		
		HashMap<String,Pack> map = new LinkedHashMap<String, Pack>();
		for(String path : paths) {
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)) );
			String line = null;
			while((line = br.readLine() ) != null ) {
				System.out.println(line);
				if(line.contains("2016-")) {
					String numstr = br.readLine();
					String costsSumStr = br.readLine();
					
					String costsAvg = br.readLine();
					
					Pack p = map.get(line);
					if(p == null)  
						map.put(line,p = new Pack());
					p.sum += Integer.parseInt(numstr);
					p.costs += Integer.parseInt(costsSumStr);
					
				}
			}
			br.close();
		}
		
		FileWriter writer = new FileWriter(destFilepath, false);
		for(Map.Entry<String, Pack> e : map.entrySet()) {
			BigDecimal countb = new BigDecimal(e.getValue().sum);
			BigDecimal costsb = new BigDecimal(e.getValue().costs);
			BigDecimal avg = costsb.divide(countb, 2, BigDecimal.ROUND_HALF_EVEN);
			writer.write(e.getKey() + "," + e.getValue().sum + "," + e.getValue().costs + "," + avg.toString() + "\r");
		}
		writer.close();
	}
	
	static String div(int a,int b) {
		BigDecimal countb = new BigDecimal(b);
		BigDecimal costsb = new BigDecimal(a);
		BigDecimal avg = costsb.divide(countb, 2, BigDecimal.ROUND_HALF_EVEN);
		return avg.toString();
	}
	
	public static void gatherTime(String path,String destFilepath,int unitSize,Date startDate) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(path)) );
		FileWriter writer = new FileWriter(destFilepath, false);
		
		
		String line = null;
		while((line = br.readLine() ) != null ) {
			String time = line.substring(0, 19);
			Date date = f.parse(time); 
			if(date.compareTo(startDate) == 0) {
				break;
			}
		}
		System.out.print(f.format(startDate) + " to ");
		writer.write(f.format(startDate).substring(11) + " to ");
		int lineSum = 0,costsSum = 0;
		lineSum += Integer.parseInt(line.split(",")[1]);
		costsSum += Integer.parseInt(line.split(",")[2]);
		Date endDate = startDate;
		for(int i = 0; i < unitSize - 1; i++) {
			line = br.readLine();
			String date = line.split(",")[0];
			String count = line.split(",")[1];
			String costs = line.split(",")[2];
			lineSum += Integer.parseInt(count);
			costsSum += Integer.parseInt(costs);
			endDate = f.parse(date);
		}
		endDate = addSeconds(endDate,10);
		endDate = backSecond(endDate);
		System.out.print(f.format(endDate ));
		System.out.println("," + lineSum);
		writer.write(f.format(endDate).substring(11) + "," + lineSum+  "," + costsSum + "," + div(lineSum, unitSize * 10) + ","  + div(costsSum,lineSum) +"\r");
		lineSum = 0;
		costsSum = 0;
		int i = 0;
		
		while((line = br.readLine() ) != null) {
			String date = line.split(",")[0];
			String count = line.split(",")[1];
			String costs = line.split(",")[2];
			if(i == 0) {
				System.out.print(date + " to ");
				writer.write(date.substring(11) + " to ");
			}
			
			lineSum += Integer.parseInt(count);
			costsSum += Integer.parseInt(costs);
			endDate = f.parse(date);
			i++;
			
			
			if(i == unitSize) {
				endDate = addSeconds(endDate,10);
				endDate = backSecond(endDate);
				System.out.print(f.format(endDate ));
				System.out.println("," + lineSum);
				writer.write(f.format(endDate ).substring(11) + "," + lineSum + "," + costsSum + "," + div(lineSum, unitSize * 10) + "," + div(costsSum,lineSum)  +","  + "\r");
				lineSum = 0;
				costsSum = 0;
				i = 0;
			}
		}
		
		br.close();
		writer.close();
	}
	
	
	
	
	public static void reconstruct(String filepath,int count,String destFilepath) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filepath)) );
		FileWriter writer = new FileWriter(destFilepath, true);

		String line = null;
		while((line = br.readLine() ) != null ) {
			if(line.contains("2016-")) {
				int sum = 0;
				String numstr = br.readLine();
				try{
					for(int i = 0; i < count; i++) {
						sum += Integer.parseInt(numstr);
					}
				}catch(Exception e) {
					e.printStackTrace();
					System.err.println("warn : " + line + " ,sum = " + sum );
				}
				
				writer.write(line + "," + sum + "\r");
				
			}
		}
		
        writer.close();
		br.close();
	}
	
	
	static Date addSeconds(Date d , int seconds) {
		Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.SECOND, seconds);
        Date date4 = cal.getTime();
        return date4;
	}
	
	static Date backSecond(Date d) {
		Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.SECOND, -1);
        Date date4 = cal.getTime();
        return date4;
	}

}
