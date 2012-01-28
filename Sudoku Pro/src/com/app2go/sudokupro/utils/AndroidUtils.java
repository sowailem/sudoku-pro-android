package com.app2go.sudokupro.utils;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.app2go.sudokupro.R;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class AndroidUtils {
	/**
	 * Indicates whether the specified action can be used as an intent. This
	 * method queries the package manager for installed packages that can
	 * respond to an intent with the specified action. If no suitable package is
	 * found, this method returns false.
	 *
	 * @param context The application's environment.
	 * @param action The Intent action to check for availability.
	 *
	 * @return True if an Intent with the specified action can be sent and
	 *         responded to, false otherwise.
	 */
	public static boolean isIntentAvailable(Context context, String action) {
	    final PackageManager packageManager = context.getPackageManager();
	    final Intent intent = new Intent(action);
	    List<ResolveInfo> list =
	            packageManager.queryIntentActivities(intent,
	                    PackageManager.MATCH_DEFAULT_ONLY);
	    return list.size() > 0;
	}
	
	public static void setThemeFromPreferences(Context context) {
		SharedPreferences gameSettings = PreferenceManager.getDefaultSharedPreferences(context);
		String theme = gameSettings.getString("theme", "default");
		if (theme.equals("default")) {
			context.setTheme(R.style.Theme_Default);
		} else if (theme.equals("paperi")) {
			context.setTheme(R.style.Theme_PaperI);
		} else if (theme.equals("paperii")) {
			context.setTheme(R.style.Theme_PaperII);
		} else {
			context.setTheme(R.style.Theme_Default);
		}
	}
	
	/** 
	 * Returns version code of OpenSudoku.
	 * @return
	 */
	public static int getAppVersionCode(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	/** 
	 * Returns version name of OpenSudoku.
	 * @return
	 */
	public static String getAppVersionName(Context context) {
		try {
			return context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Setup Ad Metwork Has to be called when the activity starts (onCreate()) but after the layout
	 * has been set (setContentView())
	 * 
	 * @param activity
	 */
	public static void setupAdNetwork(final Activity activity) {
		// let's make this bullet proof
		try {
			final View adView = activity.findViewById(R.id.ad);
			if (adView == null)
				return; // should not happen...
			adView.setVisibility(View.GONE);
			final AdRequest adRequest = new AdRequest();
			adView.setVisibility(View.VISIBLE);
			((AdView) adView).loadAd(adRequest);
		}
		catch (Throwable ignore) {
			// just ignore this, no ads are better than a crashing app
		}
	}

	/**
	 * Set's the window features for edit/play Sudoku
	 * 
	 * @param activity
	 * @param keepScreenOn
	 * @return True if the activity runs in fullscreen mode, False otherwise
	 */
	public static boolean setWindowFeatures(Activity activity) {
		boolean runsInFullScreen = false;

		Display display = activity.getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		boolean isPortrait = (width < height);
		// free version with ads
		if ((isPortrait && width <= 480 && height <= 800) || // portrait
				(!isPortrait && height <= 640)) { // landscape
			// go fullscreen for devices with small screen
			runsInFullScreen = true;
		}

		// now set screen mode
		Window window = activity.getWindow();
		if (runsInFullScreen) {
			// hide title and run in full screen
			activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		}

		return runsInFullScreen;
	}
}
