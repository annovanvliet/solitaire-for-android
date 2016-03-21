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

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.TreeMap;

import com.kmagic.solitaire.Card.SuiteEnum;
import com.kmagic.solitaire.Card.ValueEnum;


public class DrawMaster {

  private Context mContext;

  // Background
  private int mScreenWidth;
  private int mScreenHeight;
  private Paint mBGPaint;

  // Card stuff
  private final Paint mSuitPaint = new Paint();
  private Map<SuiteEnum, Map<ValueEnum, Bitmap>> mCardBitmap;
  private Bitmap mCardHidden;

  private Paint mEmptyAnchorPaint;
  private Paint mDoneEmptyAnchorPaint;
  private Paint mShadePaint;
  private Paint mLightShadePaint;
  
  private Paint mTimePaint;
  private int mLastSeconds;
  private String mTimeString;
  private Paint mScorePaint;

  private Bitmap mBoardBitmap;
  private Canvas mBoardCanvas;

  public DrawMaster(Context context) {

    mContext = context;
    // Default to this for simplicity
    mScreenWidth = 480;
    mScreenHeight = 295;

    // Background
    mBGPaint = new Paint();
    mBGPaint.setARGB(255, 0, 128, 0);

    mShadePaint = new Paint();
    mShadePaint.setARGB(200, 0, 0, 0);

    mLightShadePaint = new Paint();
    mLightShadePaint.setARGB(100, 0, 0, 0);

    // Card related stuff
    mEmptyAnchorPaint = new Paint();
    mEmptyAnchorPaint.setARGB(255, 0, 64, 0);
    mDoneEmptyAnchorPaint = new Paint();
    mDoneEmptyAnchorPaint.setARGB(128, 255, 0, 0);

    mTimePaint = new Paint();
    mTimePaint.setTextSize(18);
    mTimePaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
    mTimePaint.setTextAlign(Paint.Align.RIGHT);
    mTimePaint.setAntiAlias(true);
    mLastSeconds = -1;

    mCardBitmap = new TreeMap<Card.SuiteEnum, Map<ValueEnum, Bitmap>>();
    DrawCards(false);
    mBoardBitmap = Bitmap.createBitmap(mScreenWidth, mScreenHeight, Bitmap.Config.RGB_565);
    mBoardCanvas = new Canvas(mBoardBitmap);
  }

  public int GetWidth() { return mScreenWidth; }
  public int GetHeight() { return mScreenHeight; }
  public Canvas GetBoardCanvas() { return mBoardCanvas; }

  public void DrawCard(Canvas canvas, Card card) {
    float x = card.GetX();
    float y = card.GetY();
    //int idx = card.getSuit()*13+(card.getValue()-1);
    canvas.drawBitmap(mCardBitmap.get(card.getSuit()).get(card.getValue()), x, y, mSuitPaint);
  }

  public void DrawHiddenCard(Canvas canvas, Card card) {
    float x = card.GetX();
    float y = card.GetY();
    canvas.drawBitmap(mCardHidden, x, y, mSuitPaint);
  }

  public void DrawEmptyAnchor(Canvas canvas, float x, float y, boolean done) {
    RectF pos = new RectF(x, y, x + Card.WIDTH, y + Card.HEIGHT);
    if (!done) {
      canvas.drawRoundRect(pos, 4, 4, mEmptyAnchorPaint);
    } else {
      canvas.drawRoundRect(pos, 4, 4, mDoneEmptyAnchorPaint);
    }
  }

  public void DrawBackground(Canvas canvas) {
    canvas.drawRect(0, 0, mScreenWidth, mScreenHeight, mBGPaint);
  }

  public void DrawShade(Canvas canvas) {
    canvas.drawRect(0, 0, mScreenWidth, mScreenHeight, mShadePaint);
  }

  public void DrawLightShade(Canvas canvas) {
    canvas.drawRect(0, 0, mScreenWidth, mScreenHeight, mLightShadePaint);
  }

  public void DrawLastBoard(Canvas canvas) {
    canvas.drawBitmap(mBoardBitmap, 0, 0, mSuitPaint);
  }

  public void SetScreenSize(int width, int height) {
    mScreenWidth = width;
    mScreenHeight = height;
    mBoardBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
    mBoardCanvas = new Canvas(mBoardBitmap);
  }

  public void DrawCards(boolean bigCards) {
    if (bigCards) {
      DrawBigCards(mContext.getResources());
    } else {
      DrawCards(mContext.getResources());
    }
  }

  private void DrawBigCards(Resources r) {

    Paint cardFrontPaint = new Paint();
    Paint cardBorderPaint = new Paint();
    //Map<SuiteEnum, Bitmap> suit = new TreeMap<Card.SuiteEnum, Bitmap>();
    //Map<SuiteEnum, Bitmap> bigSuit = new TreeMap<Card.SuiteEnum, Bitmap>();
    Canvas canvas;
    int width = Card.WIDTH;
    int height = Card.HEIGHT;

    Drawable drawable = r.getDrawable(R.drawable.cardback);

    mCardHidden = Bitmap.createBitmap(Card.WIDTH, Card.HEIGHT,
                                      Bitmap.Config.ARGB_4444);
    canvas = new Canvas(mCardHidden);
    drawable.setBounds(0, 0, Card.WIDTH, Card.HEIGHT);
    drawable.draw(canvas);

    Map<SuiteEnum, Bitmap> suit = fillDrawable(r, canvas, R.drawable.suits, 10, false);
    Map<SuiteEnum, Bitmap> bigSuit = fillDrawable(r, canvas, R.drawable.bigsuits, 25, false);
//    drawable = r.getDrawable(R.drawable.suits);
//    int pos = 0;
//    for (SuiteEnum suite: SuiteEnum.values()) {
//      suit.put(suite, Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_4444));
//      canvas = new Canvas(suit.get(suite));
//      drawable.setBounds(-pos*10, 0, -pos*10+40, 10);
//      drawable.draw(canvas);
//      pos++;
//    }
//
//    drawable = r.getDrawable(R.drawable.bigsuits);
//    pos = 0;
//    for (SuiteEnum suite: SuiteEnum.values()) {
//      bigSuit.put(suite, Bitmap.createBitmap(25, 25, Bitmap.Config.ARGB_4444));
//      canvas = new Canvas(bigSuit.get(suite));
//      drawable.setBounds(-pos*25, 0, -pos*25+100, 25);
//      drawable.draw(canvas);
//      pos++;
//    }

    Map<ValueEnum, Bitmap> blackFont = fillDrawable(r, canvas, R.drawable.bigblackfont, 18, 15, false);
    Map<ValueEnum, Bitmap> redFont = fillDrawable(r, canvas, R.drawable.bigredfont, 18, 15, false);

//    drawable = r.getDrawable(R.drawable.bigblackfont);
//    for (int i = 0; i < 13; i++) {
//      blackFont[i] = Bitmap.createBitmap(18, 15, Bitmap.Config.ARGB_4444);
//      canvas = new Canvas(blackFont[i]);
//      drawable.setBounds(-i*18, 0, -i*18+234, 15);
//      drawable.draw(canvas);
//    }
//
//    drawable = r.getDrawable(R.drawable.bigredfont);
//    for (int i = 0; i < 13; i++) {
//      redFont[i] = Bitmap.createBitmap(18, 15, Bitmap.Config.ARGB_4444);
//      canvas = new Canvas(redFont[i]);
//      drawable.setBounds(-i*18, 0, -i*18+234, 15);
//      drawable.draw(canvas);
//    }
//
    cardBorderPaint.setARGB(255, 0, 0, 0);
    cardFrontPaint.setARGB(255, 255, 255, 255);
    RectF pos = new RectF();
    for (SuiteEnum suite : SuiteEnum.values()) {
      mCardBitmap.put(suite, new TreeMap<Card.ValueEnum, Bitmap>());
      for (ValueEnum val: ValueEnum.values()) {
        mCardBitmap.get(suite).put(val, Bitmap.createBitmap(
            width, height, Bitmap.Config.ARGB_4444));
        canvas = new Canvas(mCardBitmap.get(suite).get(val));
        pos.set(0, 0, width, height);
        canvas.drawRoundRect(pos, 4, 4, cardBorderPaint);
        pos.set(1, 1, width-1, height-1);
        canvas.drawRoundRect(pos, 4, 4, cardFrontPaint);

        if (suite.isRed()) {
          canvas.drawBitmap(redFont.get(val), 3, 4, mSuitPaint);
        } else {
          canvas.drawBitmap(blackFont.get(val), 3, 4, mSuitPaint);
        }


        canvas.drawBitmap(suit.get(suite), width-14, 4, mSuitPaint);
        canvas.drawBitmap(bigSuit.get(suite), width/2-12, height/2-13, mSuitPaint);
      }
    }
  }

  /**
   * @param r 
   * @param canvas
   * @param rotate 
   * @param suits
   * @param i
   * @return
   */
  private Map<SuiteEnum, Bitmap> fillDrawable(Resources r, Canvas canvas, int suitResource, int bitmapSize, boolean rotate) {
    Map<SuiteEnum, Bitmap> suit = new TreeMap<Card.SuiteEnum, Bitmap>();
    Drawable drawable = r.getDrawable(suitResource);
    int pos = 0;
    for (SuiteEnum suite: SuiteEnum.values()) {
      suit.put(suite, Bitmap.createBitmap(bitmapSize, bitmapSize, Bitmap.Config.ARGB_4444));
      canvas = new Canvas(suit.get(suite));
      if (rotate) {
        canvas.rotate(180);
        drawable.setBounds(-pos*bitmapSize-bitmapSize, -bitmapSize, -pos*bitmapSize+3*bitmapSize, 0);
      } else {
        drawable.setBounds(-pos*bitmapSize, 0, -pos*bitmapSize+4*bitmapSize, bitmapSize);
      }
      drawable.draw(canvas);
      pos++;
    }
    return suit;
  }

  private Map<ValueEnum, Bitmap> fillDrawable(Resources r, Canvas canvas, int suitResource, int fontWidth, int fontHeight, boolean rotate) {
    Map<ValueEnum, Bitmap> suit = new TreeMap<ValueEnum, Bitmap>();
    Drawable drawable = r.getDrawable(suitResource);
    int pos = 0;
    for (ValueEnum val: ValueEnum.values()) {

      suit.put(val, Bitmap.createBitmap(fontWidth, fontHeight, Bitmap.Config.ARGB_4444));
      canvas = new Canvas(suit.get(val));
      if (rotate) {
        canvas.rotate(180);
        drawable.setBounds(-pos*fontWidth-fontWidth, -fontHeight, -pos*fontWidth+(12*fontWidth), 0);
      } else {
        drawable.setBounds(-pos*fontWidth, 0, -pos*fontWidth+13*fontWidth, fontHeight);
      }
      drawable.draw(canvas);
      pos++;
    }
    return suit;
  }

  private void DrawCards(Resources r) {

    Paint cardFrontPaint = new Paint();
    Paint cardBorderPaint = new Paint();
    //Map<SuiteEnum, Bitmap> suit = new TreeMap<Card.SuiteEnum, Bitmap>();
    //Map<SuiteEnum, Bitmap> revSuit = new TreeMap<Card.SuiteEnum, Bitmap>();
//    Map<SuiteEnum, Bitmap> smallSuit = new TreeMap<Card.SuiteEnum, Bitmap>();
//    Map<SuiteEnum, Bitmap> revSmallSuit = new TreeMap<Card.SuiteEnum, Bitmap>();
//    Bitmap[] blackFont = new Bitmap[13];
//    Bitmap[] revBlackFont = new Bitmap[13];
//    Bitmap[] redFont = new Bitmap[13];
//    Bitmap[] revRedFont = new Bitmap[13];
    Bitmap redJack;
    Bitmap redRevJack;
    Bitmap redQueen;
    Bitmap redRevQueen;
    Bitmap redKing;
    Bitmap redRevKing;
    Bitmap blackJack;
    Bitmap blackRevJack;
    Bitmap blackQueen;
    Bitmap blackRevQueen;
    Bitmap blackKing;
    Bitmap blackRevKing;
    Canvas canvas;
    final int width = Card.WIDTH;
    final int height = Card.HEIGHT;
    final int fontWidth;
    final int fontHeight;
    fontWidth = 7;
    fontHeight = 9;

    float[] faceBox = { 9,8,width-10,8,
                        width-10,8,width-10,height-9,
                        width-10,height-9,9,height-9,
                        9,height-8,9,8
                      };
    Drawable drawable = r.getDrawable(R.drawable.cardback);

    mCardHidden = Bitmap.createBitmap(Card.WIDTH, Card.HEIGHT,
                                      Bitmap.Config.ARGB_4444);
    canvas = new Canvas(mCardHidden);
    drawable.setBounds(0, 0, Card.WIDTH, Card.HEIGHT);
    drawable.draw(canvas);

    Map<SuiteEnum, Bitmap> suit = fillDrawable(r, canvas, R.drawable.suits, 10, false);
    Map<SuiteEnum, Bitmap> revSuit = fillDrawable(r, canvas, R.drawable.suits, 10, true);

    Map<SuiteEnum, Bitmap> smallSuit = fillDrawable(r, canvas, R.drawable.smallsuits, 5, false);
    Map<SuiteEnum, Bitmap> revSmallSuit = fillDrawable(r, canvas, R.drawable.smallsuits, 5, true);

//    drawable = r.getDrawable(R.drawable.suits);
//    int pos = 0;
//    for (SuiteEnum suite: SuiteEnum.values()) {
//      suit.put(suite, Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_4444));
//      revSuit.put(suite, Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_4444));
//      canvas = new Canvas(suit.get(suite));
//      drawable.setBounds(-pos*10, 0, -pos*10+40, 10);
//      drawable.draw(canvas);
//      canvas = new Canvas(revSuit.get(suite));
//      canvas.rotate(180);
//      drawable.setBounds(-pos*10-10, -10, -pos*10+30, 0);
//      drawable.draw(canvas);
//      pos++;
//    }
//
//    drawable = r.getDrawable(R.drawable.smallsuits);
//    pos = 0;
//    for (SuiteEnum suite: SuiteEnum.values()) {
//      smallSuit.put(suite, Bitmap.createBitmap(5, 5, Bitmap.Config.ARGB_4444));
//      revSmallSuit.put(suite, Bitmap.createBitmap(5, 5, Bitmap.Config.ARGB_4444));
//      canvas = new Canvas(smallSuit.get(suite));
//      drawable.setBounds(-pos*5, 0, -pos*5+20, 5);
//      drawable.draw(canvas);
//      canvas = new Canvas(revSmallSuit.get(suite));
//      canvas.rotate(180);
//      drawable.setBounds(-pos*5-5, -5, -pos*5+15, 0);
//      drawable.draw(canvas);
//      pos++;
//    }

    Map<ValueEnum, Bitmap> blackFont = fillDrawable(r, canvas, R.drawable.medblackfont, 7, 9, false);
    Map<ValueEnum, Bitmap> redFont = fillDrawable(r, canvas, R.drawable.medredfont, 7, 9, false);
    Map<ValueEnum, Bitmap> revBlackFont = fillDrawable(r, canvas, R.drawable.medblackfont, 7, 9, true);
    Map<ValueEnum, Bitmap> revRedFont = fillDrawable(r, canvas, R.drawable.medredfont, 7, 9, true);

//    drawable = r.getDrawable(R.drawable.medblackfont);
//    for (int i = 0; i < 13; i++) {
//      blackFont[i] = Bitmap.createBitmap(fontWidth, fontHeight, Bitmap.Config.ARGB_4444);
//      revBlackFont[i] = Bitmap.createBitmap(fontWidth, fontHeight, Bitmap.Config.ARGB_4444);
//      canvas = new Canvas(blackFont[i]);
//      drawable.setBounds(-i*fontWidth, 0, -i*fontWidth+13*fontWidth, fontHeight);
//      drawable.draw(canvas);
//      canvas = new Canvas(revBlackFont[i]);
//      canvas.rotate(180);
//      drawable.setBounds(-i*fontWidth-fontWidth, -fontHeight, -i*fontWidth+(12*fontWidth), 0);
//      drawable.draw(canvas);
//    }
//
//    drawable = r.getDrawable(R.drawable.medredfont);
//    for (int i = 0; i < 13; i++) {
//      redFont[i] = Bitmap.createBitmap(fontWidth, fontHeight, Bitmap.Config.ARGB_4444);
//      revRedFont[i] = Bitmap.createBitmap(fontWidth, fontHeight, Bitmap.Config.ARGB_4444);
//      canvas = new Canvas(redFont[i]);
//      drawable.setBounds(-i*fontWidth, 0, -i*fontWidth+13*fontWidth, fontHeight);
//      drawable.draw(canvas);
//      canvas = new Canvas(revRedFont[i]);
//      canvas.rotate(180);
//      drawable.setBounds(-i*fontWidth-fontWidth, -fontHeight, -i*fontWidth+(12*fontWidth), 0);
//      drawable.draw(canvas);
//    }

    final int faceWidth = width - 20;
    final int faceHeight = height/2 - 9;
    drawable = r.getDrawable(R.drawable.redjack);
    redJack = Bitmap.createBitmap(faceWidth, faceHeight, Bitmap.Config.ARGB_4444);
    redRevJack = Bitmap.createBitmap(faceWidth, faceHeight, Bitmap.Config.ARGB_4444);
    canvas = new Canvas(redJack);
    drawable.setBounds(0, 0, faceWidth, faceHeight);
    drawable.draw(canvas);
    canvas = new Canvas(redRevJack);
    canvas.rotate(180);
    drawable.setBounds(-faceWidth, -faceHeight, 0, 0);
    drawable.draw(canvas);

    drawable = r.getDrawable(R.drawable.redqueen);
    redQueen = Bitmap.createBitmap(faceWidth, faceHeight, Bitmap.Config.ARGB_4444);
    redRevQueen = Bitmap.createBitmap(faceWidth, faceHeight, Bitmap.Config.ARGB_4444);
    canvas = new Canvas(redQueen);
    drawable.setBounds(0, 0, faceWidth, faceHeight);
    drawable.draw(canvas);
    canvas = new Canvas(redRevQueen);
    canvas.rotate(180);
    drawable.setBounds(-faceWidth, -faceHeight, 0, 0);
    drawable.draw(canvas);

    drawable = r.getDrawable(R.drawable.redking);
    redKing = Bitmap.createBitmap(faceWidth, faceHeight, Bitmap.Config.ARGB_4444);
    redRevKing = Bitmap.createBitmap(faceWidth, faceHeight, Bitmap.Config.ARGB_4444);
    canvas = new Canvas(redKing);
    drawable.setBounds(0, 0, faceWidth, faceHeight);
    drawable.draw(canvas);
    canvas = new Canvas(redRevKing);
    canvas.rotate(180);
    drawable.setBounds(-faceWidth, -faceHeight, 0, 0);
    drawable.draw(canvas);

    drawable = r.getDrawable(R.drawable.blackjack);
    blackJack = Bitmap.createBitmap(faceWidth, faceHeight, Bitmap.Config.ARGB_4444);
    blackRevJack = Bitmap.createBitmap(faceWidth, faceHeight, Bitmap.Config.ARGB_4444);
    canvas = new Canvas(blackJack);
    drawable.setBounds(0, 0, faceWidth, faceHeight);
    drawable.draw(canvas);
    canvas = new Canvas(blackRevJack);
    canvas.rotate(180);
    drawable.setBounds(-faceWidth, -faceHeight, 0, 0);
    drawable.draw(canvas);

    drawable = r.getDrawable(R.drawable.blackqueen);
    blackQueen = Bitmap.createBitmap(faceWidth, faceHeight, Bitmap.Config.ARGB_4444);
    blackRevQueen = Bitmap.createBitmap(faceWidth, faceHeight, Bitmap.Config.ARGB_4444);
    canvas = new Canvas(blackQueen);
    drawable.setBounds(0, 0, faceWidth, faceHeight);
    drawable.draw(canvas);
    canvas = new Canvas(blackRevQueen);
    canvas.rotate(180);
    drawable.setBounds(-faceWidth, -faceHeight, 0, 0);
    drawable.draw(canvas);

    drawable = r.getDrawable(R.drawable.blackking);
    blackKing = Bitmap.createBitmap(faceWidth, faceHeight, Bitmap.Config.ARGB_4444);
    blackRevKing = Bitmap.createBitmap(faceWidth, faceHeight, Bitmap.Config.ARGB_4444);
    canvas = new Canvas(blackKing);
    drawable.setBounds(0, 0, faceWidth, faceHeight);
    drawable.draw(canvas);
    canvas = new Canvas(blackRevKing);
    canvas.rotate(180);
    drawable.setBounds(-faceWidth, -faceHeight, 0, 0);
    drawable.draw(canvas);

    cardBorderPaint.setARGB(255, 0, 0, 0);
    cardFrontPaint.setARGB(255, 255, 255, 255);
    RectF pos = new RectF();
    for (SuiteEnum suite: SuiteEnum.values()) {
      mCardBitmap.put(suite, new TreeMap<Card.ValueEnum, Bitmap>());
      for (ValueEnum val: ValueEnum.values()) {
        mCardBitmap.get(suite).put(val, Bitmap.createBitmap(
            width, height, Bitmap.Config.ARGB_4444));
        canvas = new Canvas(mCardBitmap.get(suite).get(val));
        pos.set(0, 0, width, height);
        canvas.drawRoundRect(pos, 4, 4, cardBorderPaint);
        pos.set(1, 1, width-1, height-1);
        canvas.drawRoundRect(pos, 4, 4, cardFrontPaint);

        if (suite.isRed()) {
          canvas.drawBitmap(redFont.get(val), 2, 4, mSuitPaint);
          canvas.drawBitmap(revRedFont.get(val), width-fontWidth-2, height-fontHeight-4,
                            mSuitPaint);
        } else {
          canvas.drawBitmap(blackFont.get(val), 2, 4, mSuitPaint);
          canvas.drawBitmap(revBlackFont.get(val), width-fontWidth-2, height-fontHeight-4,
                            mSuitPaint);
        }
        if (fontWidth > 6) {
          canvas.drawBitmap(smallSuit.get(suite), 3, 5+fontHeight, mSuitPaint);
          canvas.drawBitmap(revSmallSuit.get(suite), width-7, height-11-fontHeight,
                            mSuitPaint);
        } else {
          canvas.drawBitmap(smallSuit.get(suite), 2, 5+fontHeight, mSuitPaint);
          canvas.drawBitmap(revSmallSuit.get(suite), width-6, height-11-fontHeight,
                            mSuitPaint);
        }

        if (val.isFace()) {
          canvas.drawBitmap(suit.get(suite), 10, 9, mSuitPaint);
          canvas.drawBitmap(revSuit.get(suite), width-21, height-20,
                            mSuitPaint);
        }

        int[] suitX = {9,width/2-5,width-20};
        int[] suitY = {7,2*height/5-5,3*height/5-5,height-18};
        int suitMidY = height/2 - 6;
        switch (val) {
          case ACE:
            canvas.drawBitmap(suit.get(suite), suitX[1], suitMidY, mSuitPaint);
            break;
          case TWO:
            canvas.drawBitmap(suit.get(suite), suitX[1], suitY[0], mSuitPaint);
            canvas.drawBitmap(revSuit.get(suite), suitX[1], suitY[3], mSuitPaint);
            break;
          case THREE:
            canvas.drawBitmap(suit.get(suite), suitX[1], suitY[0], mSuitPaint);
            canvas.drawBitmap(suit.get(suite), suitX[1], suitMidY, mSuitPaint);
            canvas.drawBitmap(revSuit.get(suite), suitX[1], suitY[3], mSuitPaint);
            break;
          case FOUR:
            canvas.drawBitmap(suit.get(suite), suitX[0], suitY[0], mSuitPaint);
            canvas.drawBitmap(suit.get(suite), suitX[2], suitY[0], mSuitPaint);
            canvas.drawBitmap(revSuit.get(suite), suitX[0], suitY[3], mSuitPaint);
            canvas.drawBitmap(revSuit.get(suite), suitX[2], suitY[3], mSuitPaint);
            break;
          case FIVE:
            canvas.drawBitmap(suit.get(suite), suitX[0], suitY[0], mSuitPaint);
            canvas.drawBitmap(suit.get(suite), suitX[2], suitY[0], mSuitPaint);
            canvas.drawBitmap(suit.get(suite), suitX[1], suitMidY, mSuitPaint);
            canvas.drawBitmap(revSuit.get(suite), suitX[0], suitY[3], mSuitPaint);
            canvas.drawBitmap(revSuit.get(suite), suitX[2], suitY[3], mSuitPaint);
            break;
          case SIX:
            canvas.drawBitmap(suit.get(suite), suitX[0], suitY[0], mSuitPaint);
            canvas.drawBitmap(suit.get(suite), suitX[2], suitY[0], mSuitPaint);
            canvas.drawBitmap(suit.get(suite), suitX[0], suitMidY, mSuitPaint);
            canvas.drawBitmap(suit.get(suite), suitX[2], suitMidY, mSuitPaint);
            canvas.drawBitmap(revSuit.get(suite), suitX[0], suitY[3], mSuitPaint);
            canvas.drawBitmap(revSuit.get(suite), suitX[2], suitY[3], mSuitPaint);
            break;
          case SEVEN:
            canvas.drawBitmap(suit.get(suite), suitX[0], suitY[0], mSuitPaint);
            canvas.drawBitmap(suit.get(suite), suitX[2], suitY[0], mSuitPaint);
            canvas.drawBitmap(suit.get(suite), suitX[0], suitMidY, mSuitPaint);
            canvas.drawBitmap(suit.get(suite), suitX[2], suitMidY, mSuitPaint);
            canvas.drawBitmap(suit.get(suite), suitX[1], (suitMidY+suitY[0])/2, mSuitPaint);
            canvas.drawBitmap(revSuit.get(suite), suitX[0], suitY[3], mSuitPaint);
            canvas.drawBitmap(revSuit.get(suite), suitX[2], suitY[3], mSuitPaint);
            break;
          case EIGHT:
            canvas.drawBitmap(suit.get(suite), suitX[0], suitY[0], mSuitPaint);
            canvas.drawBitmap(suit.get(suite), suitX[2], suitY[0], mSuitPaint);
            canvas.drawBitmap(suit.get(suite), suitX[0], suitMidY, mSuitPaint);
            canvas.drawBitmap(suit.get(suite), suitX[2], suitMidY, mSuitPaint);
            canvas.drawBitmap(suit.get(suite), suitX[1], (suitMidY+suitY[0])/2, mSuitPaint);
            canvas.drawBitmap(revSuit.get(suite), suitX[0], suitY[3], mSuitPaint);
            canvas.drawBitmap(revSuit.get(suite), suitX[2], suitY[3], mSuitPaint);
            canvas.drawBitmap(revSuit.get(suite), suitX[1], (suitY[3]+suitMidY)/2, mSuitPaint);
            break;
          case NINE:
            for (int i = 0; i < 4; i++) {
              canvas.drawBitmap(suit.get(suite), suitX[(i%2)*2], suitY[i/2], mSuitPaint);
              canvas.drawBitmap(revSuit.get(suite), suitX[(i%2)*2], suitY[i/2+2], mSuitPaint);
            }
            canvas.drawBitmap(suit.get(suite), suitX[1], suitMidY, mSuitPaint);
            break;
          case TEN:
            for (int i = 0; i < 4; i++) {
              canvas.drawBitmap(suit.get(suite), suitX[(i%2)*2], suitY[i/2], mSuitPaint);
              canvas.drawBitmap(revSuit.get(suite), suitX[(i%2)*2], suitY[i/2+2], mSuitPaint);
            }
            canvas.drawBitmap(suit.get(suite), suitX[1], (suitY[1]+suitY[0])/2, mSuitPaint);
            canvas.drawBitmap(revSuit.get(suite), suitX[1], (suitY[3]+suitY[2])/2, mSuitPaint);
            break;

          case JACK:
            canvas.drawLines(faceBox, cardBorderPaint);
            if (suite.isRed()) {
              canvas.drawBitmap(redJack, 10, 9, mSuitPaint);
              canvas.drawBitmap(redRevJack, 10, height-faceHeight-9, mSuitPaint);
            } else {
              canvas.drawBitmap(blackJack, 10, 9, mSuitPaint);
              canvas.drawBitmap(blackRevJack, 10, height-faceHeight-9, mSuitPaint);
            }
            break;
          case QUEEN:
            canvas.drawLines(faceBox, cardBorderPaint);
            if (suite.isRed()) {
              canvas.drawBitmap(redQueen, 10, 9, mSuitPaint);
              canvas.drawBitmap(redRevQueen, 10, height-faceHeight-9, mSuitPaint);
            } else {
              canvas.drawBitmap(blackQueen, 10, 9, mSuitPaint);
              canvas.drawBitmap(blackRevQueen, 10, height-faceHeight-9, mSuitPaint);
            }
            break;
          case KING:
            canvas.drawLines(faceBox, cardBorderPaint);
            if (suite.isRed()) {
              canvas.drawBitmap(redKing, 10, 9, mSuitPaint);
              canvas.drawBitmap(redRevKing, 10, height-faceHeight-9, mSuitPaint);
            } else {
              canvas.drawBitmap(blackKing, 10, 9, mSuitPaint);
              canvas.drawBitmap(blackRevKing, 10, height-faceHeight-9, mSuitPaint);
            }
            break;
        }
      }
    }
  }

  public void DrawTime(Canvas canvas, int millis) {
    int seconds = (millis / 1000) % 60;
    int minutes = millis / 60000;
    if (seconds != mLastSeconds) {
      mLastSeconds = seconds;
      // String.format is insanely slow (~15ms)
      if (seconds < 10) {
        mTimeString = minutes + ":0" + seconds;
      } else {
        mTimeString = minutes + ":" + seconds;
      }
    }
    mTimePaint.setARGB(255, 20, 20, 20);
    canvas.drawText(mTimeString, mScreenWidth-9, mScreenHeight-9, mTimePaint);
    mTimePaint.setARGB(255, 0, 0, 0);
    canvas.drawText(mTimeString, mScreenWidth-10, mScreenHeight-10, mTimePaint);
  }

  public void DrawRulesString(Canvas canvas, String score) {
    mTimePaint.setARGB(255, 20, 20, 20);
    canvas.drawText(score, mScreenWidth-9, mScreenHeight-29, mTimePaint);
    if (score.charAt(0) == '-') {
      mTimePaint.setARGB(255, 255, 0, 0);
    } else {
      mTimePaint.setARGB(255, 0, 0, 0);
    }
    canvas.drawText(score, mScreenWidth-10, mScreenHeight-30, mTimePaint);

  }
}
