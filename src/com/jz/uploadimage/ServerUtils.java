package com.jz.uploadimage;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Jay.
 */
public class ServerUtils {

	/**
	 * file upload
	 *
	 * @param urlStr
	 * @param filePath
	 * @return
	 */
	public static String formUpload(String urlStr, String filePath) {

		String rsp = "";
		HttpURLConnection conn = null;
		String BOUNDARY = "|"; // head of request and diameter
		try {
			URL url = new URL(urlStr);
			conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000000);
			conn.setReadTimeout(30000000);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("User-Agent",
			conn.setRequestProperty("Content-Type",
					"multipart/form-data; boundary=" + BOUNDARY);

			OutputStream out = new DataOutputStream(conn.getOutputStream());
			File file = new File(filePath);
			String filename = file.getName();
			String contentType = "";
			if (filename.endsWith(".png")) {
				contentType = "image/png";
			}
			if (filename.endsWith(".jpg")) {
				contentType = "image/jpg";
			}
			if (filename.endsWith(".jpeg")) {
				contentType = "image/jpeg";
			}
			if (filename.endsWith(".gif")) {
				contentType = "image/gif";
			}
			if (filename.endsWith(".bmp")) {
				contentType = "image/bmp";
			}
			if (filename.endsWith(".txt")) {
				contentType = "text/txt";
			}
			if (contentType == null || contentType.equals("")) {
				contentType = "application/octet-stream";
			}
			StringBuffer strBuf = new StringBuffer();
			strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
			strBuf.append("Content-Disposition: form-data; name=\"" + filePath
					+ "\"; filename=\"" + filename + "\"\r\n");
			strBuf.append("Content-Type:" + contentType + "\r\n\r\n");
			out.write(strBuf.toString().getBytes());

			DataInputStream in = new DataInputStream(new FileInputStream(file));
			int bytes = 0;
			byte[] bufferOut = new byte[1024];
			while ((bytes = in.read(bufferOut)) != -1) {
				out.write(bufferOut, 0, bytes);
			}
			in.close();
			byte[] endData = ("\r\n--" + BOUNDARY + "--\r\n").getBytes();
			out.write(endData);
			out.flush();
			out.close();

			// read the returned data
			StringBuffer buffer = new StringBuffer();
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line).append("\n");
			}
			rsp = buffer.toString();
			reader.close();
			reader = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
				conn = null;
			}
		}
		return rsp;


	}

}
