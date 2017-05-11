package com.darkspacelab.kassssprzak;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by piotrmarcinkiewicz on 11/05/2017.
 */

public class SilnikRenderujacy extends View {
    public SilnikRenderujacy(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private int mPredkosc = 40;

    private class Segment {
        int x;
        int y;
        Segment(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public enum Kierunki {
        GORA,
        DOL,
        LEWO,
        PRAWO
    }

    //Ciekawe co się stanie jak ktoś wciśnie klawisz w lewo przed startem gry?
    private Kierunki mKierunek;

    private ArrayList<Segment> mSegmenty = null;

    int licznikDoSegmentow = 0;

    public void resetujGra(){
        mSegmenty = new ArrayList<>();
        mSegmenty.add(new Segment(w / 2, h / 2));
        mKierunek = Kierunki.GORA;
        mPrzegrana = false;
        invalidate();
        licznikDoSegmentow = 0;
    }

    boolean mPrzegrana = false;
    public void aktualizacja() {
        if (mSegmenty != null) {
            Segment poprzedni = null, tymczasowy;
            for (Segment segment : mSegmenty) {
                tymczasowy = new Segment(segment.x, segment.y);
                if (poprzedni == null) {
                    switch(mKierunek) {
                        case GORA:
                            segment.y -= mPredkosc;
                            break;
                        case DOL:
                            segment.y += mPredkosc;
                            break;
                        case LEWO:
                            segment.x -= mPredkosc;
                            break;
                        case PRAWO:
                            segment.x += mPredkosc;
                            break;
                    }
                    //Sprawdzenie kolizji z innym segmentem
                    for (Segment inny: mSegmenty) {
                        if (inny != segment) {
                            if (inny.x == segment.x && inny.y == segment.y) {
                                mPrzegrana = true;
                                break;
                            }
                        }
                    }
                    if (!(mPredkosc < segment.x && segment.x < w - mPredkosc &&
                            mPredkosc < segment.y && segment.y < h - mPredkosc)) {
                        mPrzegrana = true;
                    }
                } else {
                    segment.x = poprzedni.x;
                    segment.y = poprzedni.y;
                }
                poprzedni = tymczasowy;
            }
            ++licznikDoSegmentow;
            if (licznikDoSegmentow > 3) {
                licznikDoSegmentow = 0;
                mSegmenty.add(poprzedni);
            }
        }
        invalidate();
    }

    public boolean czyPrzegrana() {
        return mPrzegrana;
    }

    public int ilePuntkow() {
        return mSegmenty.size();
    }
    public void wPrawo() {
        switch (mKierunek) {
            case GORA:
                mKierunek = Kierunki.PRAWO;
                break;
            case DOL:
                mKierunek = Kierunki.LEWO;
                break;
            case LEWO:
                mKierunek = Kierunki.GORA;
                break;
            case PRAWO:
                mKierunek = Kierunki.DOL;
                break;
        }
    }
    public void wLewo() {
        switch (mKierunek) {
            case GORA:
                mKierunek = Kierunki.LEWO;
                break;
            case DOL:
                mKierunek = Kierunki.PRAWO;
                break;
            case LEWO:
                mKierunek = Kierunki.DOL;
                break;
            case PRAWO:
                mKierunek = Kierunki.GORA;
                break;
        }
    }

    private int w;
    private int h;

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        super.onSizeChanged(w, h, oldw, oldh);
        mPredkosc = (w > h ? h : w) / 20;
        System.out.println("w: " + w + " h: " + h);
    }
    @Override
    public void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        if (mSegmenty != null) {
            for (Segment segment : mSegmenty) {
                paint.setColor(Color.WHITE);
                canvas.drawCircle(segment.x, segment.y, mPredkosc / 2, paint);
            }
        }
    }
}
