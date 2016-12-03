package com.jz.uploadimage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;





import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import net.tsz.afinal.FinalBitmap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


/**
 * Created by Jay.
 * This program is used for data collection and method testing
 */
public class MainActivity extends Activity {

	ProgressDialog dialog=null;
	String result="";
	int i = 0;
	Button btn=null;
	TextView tv=null;
        ImageView iv=null;

        FinalBitmap fb=null;

	@SuppressLint("HandlerLeak")
	Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			dialog.dismiss();
			switch (msg.what) {
			case 1:
				Toast.makeText(getApplicationContext(), "Upload Succeed", Toast.LENGTH_SHORT).show();
				tv.setText("File"+Const.DOWNLOAD_URL+result);
				fb.display(iv, Const.DOWNLOAD_URL+result);
				break;
			case 2:
				Toast.makeText(getApplicationContext(), "Upload Failed", Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dialog=new ProgressDialog(this);
		dialog.setTitle("Uploading");

		fb=FinalBitmap.create(this);

		btn=(Button)findViewById(R.id.btn);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
				openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
				startActivityForResult(openAlbumIntent, 1);
			}
		});

		tv=(TextView)findViewById(R.id.tv);
		iv=(ImageView)findViewById(R.id.iv);

	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			return;
		}
		Uri uri = null;
		switch (requestCode) {
		case 1:
			if (data == null) {
				return;
			}
			uri = data.getData();
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = managedQuery(uri, proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			System.out.println(column_index);
			cursor.moveToFirst();
			final String path = cursor.getString(column_index);


			//scan folder and get a list of files to upload
			File folder = new File("/mnt/sdcard/Download/FilesToUpload");
			File[] listOfFiles = folder.listFiles();
                        String[] Methods = new String[3];
			Methods[0] = "xz";
			Methods[1] = "zip";
			Methods[2] = "def";
			int[] compressionLevel = {0,1,3,6,9};


			dialog.show();
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(60000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

			for(int n = 2; n<3; n++) {
				for (int k = 4; k<5; k++) {
					for (int i = 0; i < listOfFiles.length; i++) {
						//System.out.println("File " + listOfFiles[i].getName());
						//get a single file name
						String File_name = listOfFiles[i].getName();
						System.out.println("File Name is: " + File_name);
						//create file and do predict
						File dataset1 = new File("/mnt/sdcard/Download", "dataset1.txt");

						//prediction and optimization
						String srcFilename = "/mnt/sdcard/Download/FilesToUpload/" + File_name;
						//File input = new File(path);



						//get dataset
						File filebefore = new File(srcFilename);
						//File fileafter = new File(pathfinal);
						double bytes1 = filebefore.length();
						//double bytes2 = fileafter.length();
						//double TenKs1 = (bytes1 / (1024 * 1024));
						//double TenKs2 = (bytes2 / (1024 * 1024));
						//double Ratio = (TenKs2 / TenKs1);

						String features = File_name + "," + bytes1 + "," + "4";

						//remote prediction
						Prediction prediction = new Prediction();
						prediction.SetDataInput(srcFilename);
						String pathfinal = prediction.prediction("def", features);

						//startTime = System.nanoTime();
						for (int j = 0; j < 1; j++) {
							result = ServerUtils.formUpload(Const.UPLOAD_URL, pathfinal);
							Log.e("jj", "result:" + result);
							if (!TextUtils.isEmpty(result)) {
								handler.sendEmptyMessage(1);
							} else {
								handler.sendEmptyMessage(2);
							}
						}
						File tmpfile = new File(pathfinal);
						tmpfile.delete();

						//long UploadingTime = (System.nanoTime() - startTime) / 10000000;

						FileOutputStream stream = null;
						try {
							stream = new FileOutputStream(dataset1, true);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}

						//write dataset
						try {
							//
							//
							FileInputStream in2 = new FileInputStream("/mnt/sdcard/Download/dataset2.txt");
							BufferedReader br = new BufferedReader(new InputStreamReader(in2));
							String energy = br.readLine();
							in2.close();				

							String level = Integer.toString(compressionLevel[k]);
							String result = "FileName: " + File_name + \
                                                                        ", datasize: " + bytes1 + ", network3g:3 " + \
                                                                        ", method: " + Methods[n] + level + ", energy: " + \
                                                                         energy + "\n";
							//System.out.println(result);
							stream.write((result).getBytes());
							stream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			    }
			   }
			}).start();
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}


}
