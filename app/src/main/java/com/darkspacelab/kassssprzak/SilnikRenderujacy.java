package com.darkspacelab.kassssprzak;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by piotrmarcinkiewicz on 11/05/2017.
 */

public class SilnikRenderujacy extends View {
    public SilnikRenderujacy(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public int losujKolor()
    {
        int losowa;
        int color = 0;
        Random generuj = new Random();
            losowa = generuj.nextInt(7);

        switch (losowa) {
            case 0:
                color = 0xFFFF0000;
            break;
            case 1:
                color = 0xFF444444;
            break;
            case 2:
                color = 0xFF888888;
            break;
            case 3:
                color = 0xFFFFFFFF;
            break;
            case 4:
                color = 0xFF00FF00;
            break;
            case 5:
                color =0xFF0000FF;
                break;
            case 6:
                color = 0xFFFF00FF;

        }
        return color;
    }

    private int mPredkosc = 40;

    private class Segment {
        int x;
        int y;
        int color;
        Segment(int x, int y, int color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }
    }

    public int losujX()
    {
        int x = w / 2;
        // true - lewo, false - prawo
        boolean strona;
        Random generuj = new Random();
        strona = generuj.nextBoolean();

        if(strona) {
            x -= mPredkosc * generuj.nextInt(w/2 / mPredkosc);
        }
        else {
            x += mPredkosc * generuj.nextInt(w/2 / mPredkosc);
        }
        return x;
    }

    public int losujY()
    {
        int y = h / 2;
        // true - góra, false - dół
        boolean strona;
        Random generuj = new Random();
        strona = generuj.nextBoolean();

        if(strona) {
           y -= mPredkosc * generuj.nextInt(h/2 / mPredkosc);

        }
        else {
            y += mPredkosc * generuj.nextInt(h/2 / mPredkosc);
        }
        return y;
    }

    private class Owocek {

        int x;
        int y;
        int color;
        Owocek(int x, int y, int color) {
            this.x = x;
            this.y = y;
            this.color = color;
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

    private Owocek owoc = null;

    int licznikDoSegmentow = 0;

    public void resetujGra(){
        mSegmenty = new ArrayList<>();
        mSegmenty.add(new Segment(w / 2, h / 2,losujKolor()));
        mKierunek = Kierunki.GORA;
        mPrzegrana = false;
        invalidate();

    }

    boolean mPrzegrana = false;
    public void aktualizacja() {

        if (!jestOwocek) {
            owoc = new Owocek(losujX(), losujY(), losujKolor());
            System.out.println("X: " + owoc.x + " Y: " + owoc.y);
            jestOwocek = true;
        }

        if (mSegmenty != null) {
            Segment poprzedni = null, tymczasowy;
            for (Segment segment : mSegmenty) {
                tymczasowy = new Segment(segment.x, segment.y,losujKolor());
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

            Segment seg = mSegmenty.get(0);

            if (seg.x == owoc.x && seg.y == owoc.y) {
                mSegmenty.add(poprzedni);
                owoc = new Owocek(losujX(),losujY(),losujKolor());
            }
        }
//        Segment seg = mSegmenty.get(0);
//        if(owoc.x == seg.x && owoc.y == seg.y)
//        System.out.println("Seg x: " + seg.x + " Seg y: " + seg.y + " Speed " + mPredkosc);

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
    private boolean jestOwocek = false;

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

            if (jestOwocek) {
                paint.setColor(owoc.color);
                canvas.drawCircle(owoc.x, owoc.y, mPredkosc / 2, paint);
//                canvas.drawRect(owoc.x - mPredkosc / 2, owoc.y - mPredkosc / 2,
//                        owoc.x + mPredkosc / 2, owoc.y + mPredkosc / 2, paint);
            }


        if (mSegmenty != null) {
            for (Segment segment : mSegmenty) {

                paint.setColor(segment.color);
                canvas.drawCircle(segment.x, segment.y, mPredkosc / 2, paint);
            }
        }
    }
}
