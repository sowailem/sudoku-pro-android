/*
 * Andoku - a sudoku puzzle game for Android.
 * Copyright (C) 2009, 2010  Markus Wiederkehr
 *
 * This file is part of Andoku.
 *
 * Andoku is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Andoku is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Andoku.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.app2go.sudokufree;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.app2go.sudokufree.db.SudokuDatabase;
import com.app2go.sudokufree.model.PuzzleType;
import com.app2go.sudokufree.source.PuzzleSourceIds;
import com.google.ads.AdRequest;
import com.google.ads.AdView;

class Util {
	private Util() {
	}

	public static int[] colorRing(int color, int nColors) {
		return colorRing(color, nColors, 360f / nColors);
	}

	public static int[] colorRing(int color, int nColors, float hueIncrement) {
		if (nColors < 2)
			throw new IllegalArgumentException();

		int alpha = Color.alpha(color);
		float[] hsv = new float[3];
		Color.colorToHSV(color, hsv);

		int[] colors = new int[nColors];

		for (int i = 0; i < nColors; i++) {
			colors[i] = Color.HSVToColor(alpha, hsv);

			hsv[0] += hueIncrement;
			if (hsv[0] >= 360f)
				hsv[0] -= 360f;
		}

		return colors;
	}

	public static String getFolderName(SudokuDatabase db, String sourceId) {
		return db.getFolderName(PuzzleSourceIds.getDbFolderId(sourceId));
	}

	public static String getPuzzleName(Resources resources, PuzzleType puzzleType) {
		return resources.getString(getNameResourceId(puzzleType));
	}

	public static Drawable getPuzzleIcon(Resources resources, PuzzleType puzzleType) {
		return resources.getDrawable(getIconResourceId(puzzleType));
	}

	private static int getNameResourceId(PuzzleType puzzleType) {
		switch (puzzleType) {
			case STANDARD:
				return R.string.name_sudoku_standard;
			case STANDARD_X:
				return R.string.name_sudoku_standard_x;
			case STANDARD_HYPER:
				return R.string.name_sudoku_standard_hyper;
			case STANDARD_PERCENT:
				return R.string.name_sudoku_standard_percent;
			case STANDARD_COLOR:
				return R.string.name_sudoku_standard_color;
			case SQUIGGLY:
				return R.string.name_sudoku_squiggly;
			case SQUIGGLY_X:
				return R.string.name_sudoku_squiggly_x;
			case SQUIGGLY_HYPER:
				return R.string.name_sudoku_squiggly_hyper;
			case SQUIGGLY_PERCENT:
				return R.string.name_sudoku_squiggly_percent;
			case SQUIGGLY_COLOR:
				return R.string.name_sudoku_squiggly_color;
		}
		throw new IllegalStateException();
	}

	private static int getIconResourceId(PuzzleType puzzleType) {
		switch (puzzleType) {
			case STANDARD:
				return R.drawable.standard_n;
			case STANDARD_X:
				return R.drawable.standard_x;
			case STANDARD_HYPER:
				return R.drawable.standard_h;
			case STANDARD_PERCENT:
				return R.drawable.standard_p;
			case STANDARD_COLOR:
				return R.drawable.standard_c;
			case SQUIGGLY:
				return R.drawable.squiggly_n;
			case SQUIGGLY_X:
				return R.drawable.squiggly_x;
			case SQUIGGLY_HYPER:
				return R.drawable.squiggly_h;
			case SQUIGGLY_PERCENT:
				return R.drawable.squiggly_p;
			case SQUIGGLY_COLOR:
				return R.drawable.squiggly_c;
		}
		throw new IllegalStateException();
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
		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(activity);
		boolean runsInFullScreen = settings.getBoolean(Settings.KEY_FULLSCREEN_MODE, true);

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
