package uk.co.mpkdashx.CBTT;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class StartDownloadUpdate extends AsyncTask<String, Integer, String> {

	MainActivity main = new MainActivity();
	protected Context applicationContext;

	@Override
	protected String doInBackground(String... sUrl) {
		String theServerVersion = sUrl[1];
		String result = null;
		try {

			URL url = new URL(sUrl[0]);
			URLConnection connection = url.openConnection();
			connection.connect();
			// this will be useful so that you can show a typical 0-100%
			// progress bar
			int fileLength = connection.getContentLength();

			InputStream input = new BufferedInputStream(url.openStream());

			Uri.Builder b = Uri.parse(Environment.getExternalStorageDirectory() + "/download/"	+ "CapgeminiBusTimeTable_" + theServerVersion+ ".apk").buildUpon();

			String file = b.build().toString();;

			OutputStream output = new FileOutputStream(file);

			byte data[] = new byte[1024];
			long total = 0;
			int count;
			while ((count = input.read(data)) != -1) {
				total += count;
				// publishing the progress....
				publishProgress((int) (total * 100 / fileLength));
				output.write(data, 0, count);
			}

			output.flush();
			output.close();
			input.close();

			result = file;
			return result;
		} catch (Exception e) {
			Log.e("Error Downloading Update: ", e.getMessage(), e);
		}
		return result;

	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		MainActivity.mProgressDialog.show();
	}

	@Override
	protected void onProgressUpdate(Integer... progress) {
		super.onProgressUpdate(progress);
		MainActivity.mProgressDialog.setProgress(progress[0]);
	}

	protected void onPostExecute(String result) {
		super.onPostExecute(null);
		MainActivity.mProgressDialog.dismiss();
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(
				Uri.fromFile(new File(result)),
				"application/vnd.android.package-archive");
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		applicationContext.startActivity(intent);
		main.finish();
	}

}
