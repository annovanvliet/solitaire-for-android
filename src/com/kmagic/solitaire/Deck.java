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

import java.util.Random;

import com.kmagic.solitaire.Card.SuiteEnum;
import com.kmagic.solitaire.Card.ValueEnum;

/**
 * Represent a stock of decks, which is shuffled and can be dealt.
 *
 */
public class Deck {

  private Card[] mCard;
  private int mCardCount;

  public Deck(int decks) {
    this(decks, 4);
  }

  /**
   * Create a stock of decks with each deck containing the specified amount of colors.
   * 
   * Each deck consists of 52 cards, so when only one color is used, each deck contains four card of each value and color.
   * 
   * @param decks number of decks
   * @param suits number of colors
   */
  public Deck(int decks, int suits) {
    if (suits == 2) {
      decks *= 2;
    } else if (suits == 1) {
      decks *= 4;
    }
    init(decks, suits);
  }

  private void init(int decks, int suits) {
    mCardCount = decks * 13 * suits;
    mCard = new Card[mCardCount];
    for (int deck = 0; deck < decks; deck++) {
      int i = 0;
      for (SuiteEnum suit : SuiteEnum.values()) {
        if ( i < suits ) {
          int value = 0;
          for (ValueEnum val : ValueEnum.values()) {
            mCard[deck*suits*13 + i*13 + value] = new Card(val, suit);
            value++;
          }
        }
        i++;
      }
    }

    shuffle();
    shuffle();
    shuffle();
  }

  /**
   * p]Push card back to stock.
   * 
   * @param card
   */
  public void PushCard(Card card) {
    mCard[mCardCount++] = card;
  }

  /**
   * Get a card from stock.
   * 
   * @return null when stock is empty.
   */
  public Card PopCard() {
    if (mCardCount > 0) {
      return mCard[--mCardCount];
    }
    return null;
  }

  public boolean Empty() {
    return mCardCount == 0;
  }

  private void shuffle() {
    int lastIdx = mCardCount - 1;
    int swapIdx;
    Card swapCard;
    Random rand = new Random();

    while (lastIdx > 1) {
      swapIdx = rand.nextInt(lastIdx);
      swapCard = mCard[swapIdx];
      mCard[swapIdx] = mCard[lastIdx];
      mCard[lastIdx] = swapCard;
      lastIdx--;
    }
  }
}
