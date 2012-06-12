package uk.co.mpkdashx.CBTT;

import org.acra.*;
import org.acra.annotation.*;

import android.app.Application;

@ReportsCrashes(formUri ="http://www.bugsense.com/api/acra?api_key=d71aad6d",  formKey="", mode = ReportingInteractionMode.TOAST, resToastText = R.string.crash_toast_text)
public class MyApplication extends Application {

	private static MyApplication app;

	@Override
	public void onCreate() {
		// The folowing line triggers the initialization of ACRA
		ACRA.init(this);
		super.onCreate();
	}

	public static MyApplication getApp() {
		return app;
	}

}
