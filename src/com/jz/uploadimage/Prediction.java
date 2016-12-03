package com.jz.uploadimage;
import org.tukaani.xz.LZMA2Options;
import org.tukaani.xz.XZOutputStream;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.io.DataOutputStream;


/**
 * Created by Jay.
 */
public class Prediction {
        final String compressedPath = new String("/mnt/sdcard/Download/tmp");
        public String srcFilePath = new String();

        public void SetDataInput(String srcFilename) {
            this.srcFilePath = srcFilename;
        }

        private File GetDataInput() throws Exception {
                File input = new File(this.srcFilePath);
                return input;
            }

        public String prediction(String method, String features) {
            String pathfinal = null;
            int prediction_rst = 0;

            try {
                if (method=="def") {
                    if (prediction_rst==9) {
                        pathfinal = xzUtility(1);
                    }
                    else {
                        pathfinal = deflaterUtility(prediction_rst);
                    }
                    //pathfinal = zipUtility(9);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return pathfinal;
        }

        private String xzUtility(int k) throws Exception {
            File input = GetDataInput();
            String xzFile = ".xz";

            try {
                FileInputStream infile = new FileInputStream(input);
                FileOutputStream outxz = new FileOutputStream(compressedPath + xzFile);
                LZMA2Options options = new LZMA2Options();
                options.setPreset(k);

                XZOutputStream out = new XZOutputStream(outxz, options);

                byte[] buf = new byte[8192];
                int size;
                while ((size = infile.read(buf)) != -1) {
                    out.write(buf, 0, size);
                }

                out.finish();
                // close the InputStream
                infile.close();

                // close the ZipOutputStream
                outxz.close();
                out.close();
            }
            catch (IOException ioe) {
                System.out.println("Error creating zip file" + ioe);
            }

            return compressedPath+xzFile;
        }



        private String zipUtility(int k) throws Exception {
            File input = GetDataInput();
            String zipFile = ".zip";

            try {
                // create byte buffer
                byte[] buffer = new byte[8192];

                FileOutputStream fos = new FileOutputStream(compressedPath + zipFile);

                ZipOutputStream zos = new ZipOutputStream(fos);

                zos.setLevel(k);

                File srcFile = new File(srcFilePath);

                FileInputStream fis = new FileInputStream(srcFile);

                // new zip entry
                zos.putNextEntry(new ZipEntry(srcFile.getName()));

                int length;

                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }

                zos.closeEntry();
                fis.close();
                zos.close();

            }
            catch (IOException ioe) {
                System.out.println("Error creating zip file" + ioe);
            }
            return compressedPath+zipFile;
        }



        private String deflaterUtility(int k) throws Exception {
            System.out.println(k);

            File input = GetDataInput();
            String dzipFile = ".zz";
            DataInputStream in = new DataInputStream(new FileInputStream(input));
            byte[] data = InputStreamUtils.InputStreamTOByte(in);

            try {

                Deflater deflater = new Deflater();
                deflater.setLevel(k);
                deflater.setInput(data);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream(8192);
                FileOutputStream fop = new FileOutputStream(compressedPath+dzipFile);

                deflater.finish();
                while (!deflater.finished()) {
                    byte[] buffer = new byte[8192];
                    int count = deflater.deflate(buffer);
                    outputStream.write(buffer, 0, count);
                }
                outputStream.close();
                byte[] output = outputStream.toByteArray();
                fop.write(output);
                fop.flush();
                fop.close();

            }
            catch (IOException ioe) {
                System.out.println("Error creating zip file" + ioe);
            }
            return compressedPath+dzipFile;
        }

        public int remote_prediction(String features) {
            int rst = 0;
            String question = features;

            try {
                String serverAddress = "192.168.2.1";
                Socket s = new Socket(serverAddress, 9090);

                DataOutputStream dOut =
                        new DataOutputStream(s.getOutputStream());
                BufferedReader input =
                        new BufferedReader(new InputStreamReader(s.getInputStream()));

                dOut.writeBytes(question);
                String answer = input.readLine();

                //System.out.println(answer);
                switch (answer){
                    case "0": rst = 0;
                              break;
                    case "1": rst = 1;
                        break;
                    case "3": rst = 3;
                        break;
                    case "6": rst = 6;
                        break;
                }
                //System.out.println(rst);
            }
            catch (IOException ioe) {
                System.out.println("Network Error");
            }
            return rst;
        }
}
