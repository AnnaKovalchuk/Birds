package com.example.birds;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.view.View;

public class GameView extends View {
    public GameView(Context context) {
        super(context);

        //------------------------------Создание птицы, управляемой пользователем

        //Создадим объект класса Sprite
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.player);
        int w = b.getWidth()/5;
        int h = b.getHeight()/3;
        Rect firstFrame = new Rect(0, 0, w, h);
        playerBird = new Sprite(10, 0, 0, 100, firstFrame, b);

        //Добавим больше кадров с изображением птицы
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                if (i == 2 && j == 3) {
                    continue;
                }
                playerBird.addFrame(new Rect(j * w, i * h, j * w + w, i
                        * w + w));
            }
        }

        //Создадим и запустим таймер
        Timer t = new Timer();
        t.start();
    }

    private Sprite playerBird;

    //Актуальные размеры игрового поля
    private int viewWidth;
    private int viewHeight;

    //Поле для хранения очкков, набранных игроком
    private int points = 0;

    //--------------------------------Определение размеров игрового поля
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth = w;
        viewHeight = h;
    }

    //---------------------Рисовка фона и количества очков на поверхности компонента
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawARGB(250, 127, 199, 255); // цвет фона
        playerBird.draw(canvas); //Нарисуем птицу на экране
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setTextSize(55.0f);
        p.setColor(Color.WHITE);
        canvas.drawText(points+"", viewWidth - 100, 70, p);
    }


    //----------------------------------Добавление анимации у Птицы

    //промежуток времени, за который должно происходить изменение игровой модели
    private final int timerInterval = 30;

    //обновим состояние спрайта с птицей и перерисуем GameView
    protected void update () {
        playerBird.update(timerInterval);
        invalidate();
    }

    //Добавление класса таймера
    class Timer extends CountDownTimer {
        public Timer() {
            super(Integer.MAX_VALUE, timerInterval); // общее время работы таймера и время периодического срабатывания
        }
        // указываются действия, которые нужно делать периодически
        @Override
        public void onTick(long millisUntilFinished) {
            update();
        }
        //действия, когда таймер заканчивает свою работу
        @Override
        public void onFinish() {
        }

    }
}

