/*
  Copyright 2008 Google Inc.
  
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
       http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
*/ 
package com.kmagic.solitaire;

import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.kmagic.solitaire.actionbar.ActionBarActivity;

// Base activity class.
public class Solitaire extends ActionBarActivity {
  
  // View extracted from main.xml.
  private View mMainView;
  private SolitaireView mSolitaireView;
  private SharedPreferences mSettings;

  private boolean mDoSave;
  
  // Shared preferences are where the various user settings are stored.
  public SharedPreferences GetSettings() { return mSettings; }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    mDoSave = true;

    // Force landscape and no title for extra room
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    //requestWindowFeature(Window.FEATURE_NO_TITLE);
    requestWindowFeature(Window.FEATURE_ACTION_BAR);

    // If the user has never accepted the EULA show it again.
    mSettings = getSharedPreferences("SolitairePreferences", 0);
    setContentView(R.layout.main);
    mMainView = findViewById(R.id.main_view);
    mSolitaireView = (SolitaireView) findViewById(R.id.solitaire);
    mSolitaireView.SetTextView((TextView) findViewById(R.id.text));

    //StartSolitaire(savedInstanceState);
  }

  // Entry point for starting the game.
  //public void StartSolitaire(Bundle savedInstanceState) {
  @Override
  public void onStart() {
    super.onStart();
    if (mSettings.getBoolean("SolitaireSaveValid", false)) {
      SharedPreferences.Editor editor = GetSettings().edit();
      editor.putBoolean("SolitaireSaveValid", false);
      editor.commit();
      // If save is corrupt, just start a new game.
      if (mSolitaireView.LoadSave()) {
        HelpSplashScreen();
        return;
      }
    }

    mSolitaireView.InitGame(mSettings.getInt("LastType", Rules.SOLITAIRE));
    HelpSplashScreen();
  }

  // Force show the help if this is the first time played. Sadly no one reads
  // it anyways.
  private void HelpSplashScreen() {
    if (!mSettings.getBoolean("PlayedBefore", false)) {
      mSolitaireView.DisplayHelp();
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    
    MenuInflater menuInflater = getMenuInflater();
    menuInflater.inflate(R.menu.main, menu);

    return super.onCreateOptionsMenu(menu);

  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_new_solitaire:
        mSolitaireView.InitGame(Rules.SOLITAIRE);
        break;
      case R.id.menu_new_spider:
        mSolitaireView.InitGame(Rules.SPIDER);
        break;
      case R.id.menu_new_freecell:
        mSolitaireView.InitGame(Rules.FREECELL);
        break;
      case R.id.menu_new_fortythieves:
        mSolitaireView.InitGame(Rules.FORTYTHIEVES);
        break;
      case R.id.menu_deal:
        mSolitaireView.dealGame();
        break;
      case R.id.menu_restart:
        mSolitaireView.RestartGame();
        break;
      case R.id.menu_stats:
        DisplayStats();
        break;
      case R.id.menu_options:
        DisplayOptions();
        break;
      case R.id.menu_help:
        mSolitaireView.DisplayHelp();
        break;
      case R.id.menu_save_quit:
        mSolitaireView.SaveGame();
        mDoSave = false;
        finish();
        break;
      case R.id.menu_quit:
        mDoSave = false;
        finish();
        break;
    }

    return false;
  }

  @Override
  protected void onPause() {
    super.onPause();
    mSolitaireView.onPause();
  }

  @Override
  protected void onStop() {
    super.onStop();
    if (mDoSave) {
      mSolitaireView.SaveGame();
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    mSolitaireView.onResume();
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
  }

  public void DisplayOptions() {
    mSolitaireView.SetTimePassing(false);
    new Options(this, mSolitaireView.GetDrawMaster());
  }

  public void DisplayStats() {
    mSolitaireView.SetTimePassing(false);
    new Stats(this, mSolitaireView);
  }

  public void CancelOptions() {
    setContentView(mMainView);
    mSolitaireView.requestFocus();
    mSolitaireView.SetTimePassing(true);
  }

  public void NewOptions() {
    setContentView(mMainView);
    mSolitaireView.InitGame(mSettings.getInt("LastType", Rules.SOLITAIRE));
  }

  // This is called for option changes that require a refresh, but not a new game
  public void RefreshOptions() {
    setContentView(mMainView);
    mSolitaireView.RefreshOptions();
  }
}
