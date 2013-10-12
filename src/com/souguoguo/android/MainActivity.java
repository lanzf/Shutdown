package com.souguoguo.android;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {
	private final static String[] ARGS = { "ls", "-l" };
	private final static String[] ROOT = { "su" };
	private final static String[] SHUTDOWN = { "reboot", "-p" };
	private final static String[] REBOOT = { "reboot", };
	private final static String TAG = "com.yin.system";

	// Button mButton;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// mButton = (Button) findViewById(R.id.myButton);
		//
		// mButton.setOnClickListener(new OnClickListener() {
		//
		// public void onClick(View v) {
		// // root();
		// }
		// });
		shutdown();
	}

	public void getResult() {
		ShellExecute cmdexe = new ShellExecute();
		try {
			cmdexe.execute(ARGS, "/");
		} catch (IOException e) {
			Log.e(TAG, "IOException");
			e.printStackTrace();
		}
	}

	/**
	 * 获取root权限
	 * 
	 * @return
	 */
	public void root() {
		ShellExecute cmdexe = new ShellExecute();
		try {
			cmdexe.execute(ROOT, null);
		} catch (IOException e) {
			Log.e(TAG, "IOException");
			e.printStackTrace();
		}
	}

	/**
	 * 关机
	 * 
	 * @return
	 */
	public void shutdown() {
		// ShellExecute cmdexe = new ShellExecute();
		// try {
		// cmdexe.execute(SHUTDOWN, null);
		// } catch (IOException e) {
		// Log.e(TAG, "IOException");
		// e.printStackTrace();
		// }

		// Process localProcess;
		// try {
		// localProcess = Runtime.getRuntime().exec("su");
		// // if (localProcess.waitFor() != 0) {
		// // System.err.println("exit value = " + localProcess.exitValue());
		// // }
		// ProcessBuilder builder = new ProcessBuilder(SHUTDOWN);
		// builder.redirectErrorStream(true);
		// builder.start();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// catch (InterruptedException e) {
		// e.printStackTrace();
		// }

		Process proc = null;
		try {
			proc = Runtime.getRuntime().exec("/system/bin/sh", null,
					new File("/system/bin")); // android中使用
			// proc = Runtime.getRuntime().exec("/bin/bash", null, new
			// File("/bin")); //Linux中使用
			// 至于windows，由于没有bash，和sh 所以这种方式可能行不通
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (proc != null) {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					proc.getInputStream()));
			PrintWriter out = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(proc.getOutputStream())), true);
			// 获取root权限
			out.println("su");
			out.println("reboot -p");
			try {
				String line;
				while ((line = in.readLine()) != null) {
					System.out.println(line);
					Log.d("command", line);
				}
				// proc.waitFor(); //上面读这个流是阻塞的，所以waitfor 没太大必要性
				in.close();
				out.close();
				proc.destroy();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private class ShellExecute {
		/*
		 * args[0] : shell 命令 如"ls" 或"ls -1"; args[1] : 命令执行路径 如"/" ;
		 */
		public void execute(String[] cmmand, String directory)
				throws IOException {
			String result = "";
			try {

				// Process localProcess = Runtime.getRuntime().exec("su");
				// if (localProcess.waitFor() != 0) {
				// System.err.println("exit value = "
				// + localProcess.exitValue());
				// }

				ProcessBuilder builder = new ProcessBuilder(cmmand);

				if (directory != null) {
					builder.directory(new File(directory));
				}
				builder.redirectErrorStream(true);
				Process process = builder.start();
				// 得到命令执行后的结果
				InputStream is = process.getInputStream();
				byte[] buffer = new byte[1024];
				while (is.read(buffer) != -1) {
					result = result + new String(buffer);
				}
				is.close();

				// } catch (InterruptedException e) {
				// System.err.println(e);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}