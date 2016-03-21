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

import android.util.Log;

import com.kmagic.solitaire.Card.SuiteEnum;
import com.kmagic.solitaire.Card.ValueEnum;

/**
 * a Card with a value and a position.
 *
 * @author Anno van Vliet
 *
 */
class Card {
  
  public enum SuiteEnum {
    CLUBS(false),
    DIAMONDS(true),
    SPADES(false),
    HEARTS(true);

    private final boolean red;
    
    private SuiteEnum( boolean red) {
      this.red = red;
    }
    
    /**
     * @return a red Card
     */
    public boolean isRed() {
      return red;
    }

    /**
     * @return a black Card
     */
    public boolean isBlack() {
      return !isRed();
    }

    /**
     * @param i
     * @return
     */
    public static SuiteEnum valueOf(int i) {

      for (SuiteEnum suite : values()) {
        if ( suite.ordinal() == i )
          return suite;
        
      }
      return null;
    }   
  }

//  public static final int CLUBS = 0;
//  public static final int DIAMONDS = 1;
//  public static final int SPADES = 2;
//  public static final int HEARTS = 3;

  public enum ValueEnum {
    ACE("A"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9"),
    TEN("10"),
    JACK("J"),
    QUEEN("Q"),
    KING("K");
    
    private final String text;

    private ValueEnum(String text) {
      this.text = text;
    }
    
    /**
     * @return the text
     */
    public String getText() {
      return text;
    }
    
    /**
     * @param i
     * @return
     */
    public static ValueEnum valueOf(int i) {

      for (ValueEnum val : values()) {
        if ( val.ordinal() == i )
          return val;
        
      }
      return null;
    }

    /**
     * Is this a Face value?
     * @return
     */
    public boolean isFace() {
      return (this.ordinal() >= JACK.ordinal());
    }

    /**
     * @param v2
     * @return
     */
    public boolean isNext(ValueEnum v2) {
      return v2.ordinal() == this.ordinal() + 1;
    }   

    /**
     * @param v2
     * @return
     */
    public boolean isPrevious(ValueEnum v2) {
      return v2.ordinal() + 1 == this.ordinal();
    }   

  }
  

  public static int WIDTH = 45; //    mScreenWidth = 480; 10
  public static int HEIGHT = 64; // mScreenHeight = 295; 4

  private final ValueEnum mValue;
  private final SuiteEnum mSuit;
  private float mX;
  private float mY;

  public static void SetSize(int type) {
//    if (type == Rules.SOLITAIRE) {
//      WIDTH = 51;
//      HEIGHT = 72;
//    } else if (type == Rules.FREECELL) {
//      WIDTH = 49;
//      HEIGHT = 68;
//    } else {
//      WIDTH = 45;
//      HEIGHT = 64;
//    }
  }

  /**
   * @param width2
   * @param height2
   */
  public static void SetSize(int width2, int height2) {
    Log.d("Card.java", "SetSize");
    
    WIDTH = width2/11;
    HEIGHT = (WIDTH*64)/45;
        
  }

 
  
  public Card(ValueEnum value, SuiteEnum suit) {
    mValue = value;
    mSuit = suit;
    mX = 1;
    mY = 1;
  }

  /**
   * @param value
   * @param i
   */
  public Card(int value, int suiteValue) {
    this(ValueEnum.valueOf(value - 1), SuiteEnum.valueOf(suiteValue));
  }

  public float GetX() { return mX; }
  public float GetY() { return mY; }
  public ValueEnum getValue() { return mValue; }
  public SuiteEnum getSuit() { return mSuit; }

  public void SetPosition(float x, float y) {
    mX = x;
    mY = y;
  }

  public void MovePosition(float dx, float dy) {
    mX -= dx;
    mY -= dy;
  }

  /**
   * @param card
   * @return
   */
  public boolean isPrevious(Card previous) {
    return ((this.getSuit().isRed()) == (previous.getSuit().isRed()) ||
        this.getValue().isPrevious(previous.getValue()));
  }

}


