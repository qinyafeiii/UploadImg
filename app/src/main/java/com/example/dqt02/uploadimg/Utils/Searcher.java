package com.example.dqt02.uploadimg.Utils;
import android.graphics.Bitmap;
import android.graphics.Matrix;

import java.io.*;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Searcher {
	private static final String PARTNER_ID = "Testor";
	private static final int IMG_WIDTH = 640;
	private static final int IMG_HEIGHT = 640;
	private static final int HISTO_SIZE = 100;
	private static final int RECT_SIZE = 3;
	private static final int RECT_AREA = RECT_SIZE*RECT_SIZE;
	private String ADD_URL = "http://www.aiforu.com/api/Image/Fingerprint/add_image.shtml";
	private String SEARCH_URL = "http://www.aiforu.com/api/Image/Fingerprint/search_image.shtml";
	private String DELETE_URL = "http://www.aiforu.com/api/Image/Fingerprint/delete_image.shtml";
	private static final String ADDR_LIST_URL = "http://www.aiforu.com/api/Image/Index/lookup_table.shtml";
	private String SERVICE_ID = "AIFORU_ImageFingerprintG9";
	private String created_date;

	public Searcher() {
		SERVICE_ID = "AIFORU_ImageFingerprintG9";
		String params = "&module=fingerprintG9&action=add";
		try {
			ADD_URL = sendHttpRequest(ADDR_LIST_URL,params).toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		params = "&module=fingerprintG9&action=search";
		try {
			SEARCH_URL = sendHttpRequest(ADDR_LIST_URL,params).toString();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		params = "&module=fingerprintG9&action=delete";
		try {
			DELETE_URL = sendHttpRequest(ADDR_LIST_URL,params).toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		params = "&module=other&action=date";
		try {
			created_date = sendHttpRequest(ADDR_LIST_URL,params).toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public Searcher(boolean debug) {
		if(debug==true) {
			SERVICE_ID = "AIFORU_ImageDebugFingerprintG9";

			String params = "&module=fingerprintG9_debug&action=add";
			try {
				ADD_URL = sendHttpRequest(ADDR_LIST_URL,params).toString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			params = "&module=fingerprintG9_debug&action=search";
			try {
				SEARCH_URL = sendHttpRequest(ADDR_LIST_URL,params).toString();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			params = "&module=fingerprintG9_debug&action=delete";
			try {
				DELETE_URL = sendHttpRequest(ADDR_LIST_URL,params).toString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			params = "&module=other&action=date";
			try {
				created_date = sendHttpRequest(ADDR_LIST_URL,params).toString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			SERVICE_ID = "AIFORU_ImageFingerprintG9";

			String params = "&module=fingerprintG9&action=add";
			try {
				ADD_URL = sendHttpRequest(ADDR_LIST_URL,params).toString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			params = "&module=fingerprintG9&action=search";
			try {
				SEARCH_URL = sendHttpRequest(ADDR_LIST_URL,params).toString();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			params = "&module=fingerprintG9&action=delete";
			try {
				DELETE_URL = sendHttpRequest(ADDR_LIST_URL,params).toString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			params = "&module=other&action=date";
			try {
				created_date = sendHttpRequest(ADDR_LIST_URL,params).toString();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void updateToken() {
		String params = "&module=other&action=date";
		try {
			created_date = sendHttpRequest(ADDR_LIST_URL,params).toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String searchImage(Bitmap bitmap, String urlString, double threshold) throws IOException {
        Bitmap image= bitmap;
		int[] r = new int[HISTO_SIZE+1];
		int[] g = new int[HISTO_SIZE+1];
		int[] b = new int[HISTO_SIZE+1];
		int[] G9_r = new int[RECT_AREA];
		int[] G9_g = new int[RECT_AREA];
		int[] G9_b = new int[RECT_AREA];
		String params = "&threshold="+String.valueOf(threshold);
		getFingerprint(image,r,g,b,G9_r,G9_g,G9_b);
		for(int i=0;i<HISTO_SIZE;i++) {
			params += "&r[]=";
			params += String.valueOf(r[i]);
			params += "&g[]=";
			params += String.valueOf(g[i]);
			params += "&b[]=";
			params += String.valueOf(b[i]);
		}
		for(int i=0;i<9;i++) {
			params += "&r9[]=";
			params += String.valueOf(G9_r[i]);
			params += "&g9[]=";
			params += String.valueOf(G9_g[i]);
			params += "&b9[]=";
			params += String.valueOf(G9_b[i]);
		}
		params += "&token="+get_token(SERVICE_ID+"search_image"+created_date);
		StringBuffer res = sendHttpRequest(SEARCH_URL,params);
		return res.toString();
	}

	public String addImage(Bitmap image,String urlString,double threshold) throws IOException {
		URL url = new URL(urlString);
		int[] r = new int[HISTO_SIZE+1];
		int[] g = new int[HISTO_SIZE+1];
		int[] b = new int[HISTO_SIZE+1];
		int[] G9_r = new int[RECT_AREA];
		int[] G9_g = new int[RECT_AREA];
		int[] G9_b = new int[RECT_AREA];
		String params = "&hashCode="+String.valueOf(image.hashCode());
		params += "&threshold="+String.valueOf(threshold);
		getFingerprint(image,r,g,b,G9_r,G9_g,G9_b);
		for(int i=0;i<HISTO_SIZE;i++) {
			params += "&r[]=";
			params += String.valueOf(r[i]);
			params += "&g[]=";
			params += String.valueOf(g[i]);
			params += "&b[]=";
			params += String.valueOf(b[i]);
		}
		for(int i=0;i<9;i++) {
			params += "&r9[]=";
			params += String.valueOf(G9_r[i]);
			params += "&g9[]=";
			params += String.valueOf(G9_g[i]);
			params += "&b9[]=";
			params += String.valueOf(G9_b[i]);
		}
		params += "&url="+urlString;
		params += "&token="+get_token(SERVICE_ID+"add_image"+created_date);
		StringBuffer res = sendHttpRequest(ADD_URL,params);
		return res.toString();
	}

	public String deleteImage(String id, String hashCode) throws IOException {
		String params = "&id="+id+"&hashCode="+hashCode;
		params += "&token="+get_token(SERVICE_ID+"delete_image"+created_date);
		StringBuffer res = sendHttpRequest(DELETE_URL,params);
		return res.toString();
	}

	private StringBuffer sendHttpRequest(String urlString, String params) throws MalformedURLException,ProtocolException,IOException {
		params += "&partner_id="+PARTNER_ID;
		StringBuffer sb =null;
		URL url = new URL(urlString);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestMethod("POST");
		connection.setUseCaches(false);
		connection.setInstanceFollowRedirects(true);
		connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
		connection.connect();
		DataOutputStream out = new DataOutputStream(connection.getOutputStream());
		out.writeUTF(params);
		out.flush();
		out.close();
		BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String lines;
		sb = new StringBuffer("");
		while ((lines = reader.readLine()) != null) {
			lines = new String(lines.getBytes(), "utf-8");
			sb.append(lines);
		}
		reader.close();
		connection.disconnect();
		return sb;


	}

	private void getFingerprint(Bitmap image,int[] r, int[] g, int[] b
			, int[] G9_r, int[] G9_g, int[] G9_b) {
		image = resizeImage(image,IMG_WIDTH,IMG_HEIGHT);
		int width = image.getWidth();
		int height = image.getHeight();
		int[] r9_sum = new int[RECT_AREA];
		int[] g9_sum = new int[RECT_AREA];
		int[] b9_sum = new int[RECT_AREA];
		for(int i=0; i<width;i++) {
			for(int j=i;j<height;j++) {
				int pixel = image.getPixel(i,j);
				int blue = pixel & 0xff;
				int green = (pixel & 0xff00) >> 8;
				int red = (pixel & 0xff0000) >> 16;
				//fingerprint
				double sum = blue + green + red + 0.01;
				int b_i = (int)Math.round(HISTO_SIZE*blue/sum);
				int r_i = (int)Math.round(HISTO_SIZE*red/sum);
				int g_i = (int)Math.round(HISTO_SIZE*green/sum);

				r[r_i]++;
				g[g_i]++;
				b[b_i]++;

				//G9
				int g9_x = RECT_SIZE*i/(IMG_WIDTH);
				if(g9_x>RECT_SIZE) g9_x=RECT_SIZE;
				int g9_y = RECT_SIZE*j/(IMG_HEIGHT);
				if(g9_y>RECT_SIZE) g9_y=RECT_SIZE;
				int g9_i = g9_y + RECT_SIZE*g9_x;
				r9_sum[g9_i] += r_i;
				g9_sum[g9_i] += g_i;
				b9_sum[g9_i] += b_i;
			}
		}

		for(int i=0;i<RECT_AREA;i++) {
			for(int j=i;j<RECT_AREA;j++) {
				if(r9_sum[i]>r9_sum[j]) {
					G9_r[i] ++;
				}
				else {
					G9_r[j] ++;
				}
				if(g9_sum[i]>g9_sum[j]) {
					G9_g[i] ++;
				}
				else {
					G9_g[j] ++;
				}
				if(b9_sum[i]>b9_sum[j]) {
					G9_b[i] ++;
				}
				else {
					G9_b[j] ++;
				}
			}
		}
	}

	private Bitmap resizeImage(Bitmap bm, int width, int height){
        int widthOld = bm.getWidth();
        int heightOld = bm.getHeight();
        // 设置想要的大小
        // 计算缩放比例
        float scaleWidth = ((float) width) / widthOld;
        float scaleHeight = ((float) height) / heightOld;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, widthOld, heightOld, matrix,
                true);
        return newbm;
	}

	private String get_token(String input) {
		String result = input;
		if(input != null) {
			MessageDigest md=null;
			try {
				md = MessageDigest.getInstance("MD5");
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} //or "SHA-1"
			md.update(input.getBytes());
			BigInteger hash = new BigInteger(1, md.digest());
			result = hash.toString(16);
			while(result.length() < 32) {
				result = "0" + result;
			}
		}
		return result;

	}
}