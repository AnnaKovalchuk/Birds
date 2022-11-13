package com.example.birds;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.CountDownTimer;
import android.view.MotionEvent;
import android.view.View;

public class GameView extends View {
    public GameView(Context context) {
        super(context);
        getLevel();

        //------------------------------Создание птицы, управляемой пользователем

        //Создадим объект класса Sprite
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.player);
        int w = b.getWidth()/5;
        int h = b.getHeight()/3;
        Rect firstFrame = new Rect(0, 0, w, h);
        playerBird = new Sprite(10, 0, 0, 200, firstFrame, b);

        //Добавим больше кадров с изображением птицы
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == 0 && j == 0) {
                    continue;
                }
                if (i == 2 && j == 3) {
                    continue;
                }
                playerBird.addFrame(new Rect(j * w, i * h, j * w + w, i  * w + w));
            }
        }

        //создание спрайта противника и добавление в него кадров
        b = BitmapFactory.decodeResource(getResources(), R.drawable.enemy);
        w = b.getWidth()/5;
        h = b.getHeight()/3;
        firstFrame = new Rect(4*w, 0, 5*w, h);
        enemyBird = new Sprite(2000, 250, -300, 0, firstFrame, b);
        for (int i = 0; i < 3; i++) {
            for (int j = 4; j >= 0; j--) {
                if (i ==0 && j == 4) {
                    continue;
                }
                if (i ==2 && j == 0) {
                    continue;
                }
                enemyBird.addFrame(new Rect(j*w, i*h, j*w+w, i*w+w));
            }
        }
        enemyBird1 = new Sprite(2000, 250, -300, 0, firstFrame, b);
        for (int i = 0; i < 3; i++) {
            for (int j = 4; j >= 0; j--) {
                if (i ==0 && j == 4) {
                    continue;
                }
                if (i ==2 && j == 0) {
                    continue;
                }
                enemyBird1.addFrame(new Rect(j*w, i*h, j*w+w, i*w+w));
            }
        }
        enemyBird2 = new Sprite(2000, 250, -300, 0, firstFrame, b);
        for (int i = 0; i < 3; i++) {
            for (int j = 4; j >= 0; j--) {
                if (i ==0 && j == 4) {
                    continue;
                }
                if (i ==2 && j == 0) {
                    continue;
                }
                enemyBird2.addFrame(new Rect(j*w, i*h, j*w+w, i*w+w));
            }
        }
        enemyBird3 = new Sprite(2000, 250, -300, 0, firstFrame, b);
        for (int i = 0; i < 3; i++) {
            for (int j = 4; j >= 0; j--) {
                if (i ==0 && j == 4) {
                    continue;
                }
                if (i ==2 && j == 0) {
                    continue;
                }
                enemyBird3.addFrame(new Rect(j*w, i*h, j*w+w, i*w+w));
            }
        }

        //Создадим и запустим таймер
        Timer t = new Timer();
        t.start();
    }

    private Sprite playerBird; //Птица игрока
    private Sprite enemyBird; //Птица противник
    private Sprite enemyBird1;
    private Sprite enemyBird2;
    private Sprite enemyBird3;

    //Актуальные размеры игрового поля
    private int viewWidth;
    private int viewHeight;

    //Поле для хранения очкков, набранных игроком
    private int points = 50;

    //Уровень
    private int level = 1;

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
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setTextSize(55.0f);
        p.setColor(Color.WHITE);
        canvas.drawText(points+"", viewWidth - 100, 70, p);
        canvas.drawText(level+"", viewWidth - 160, 70, p);
        playerBird.draw(canvas); //Нарисуем птицу игрока на экране
        if(level == 1){
            enemyBird.draw(canvas); //Нарисуем птицу противника на экране
        }
        else if(level ==2){
            enemyBird1.draw(canvas);
        }
        else if(level == 3){
            enemyBird2.draw(canvas);
        }
        else if(level == 4){
            enemyBird3.draw(canvas);
        }
    }


    //----------------------------------Добавление анимации у Птицы

    //промежуток времени, за который должно происходить изменение игровой модели
    private final int timerInterval = 30;

    //обновим состояние спрайта с птицей и перерисуем GameView
    protected void update () {
        playerBird.update(timerInterval);
        enemyBird.update(timerInterval); //Изменим состояние спрайта противника
        enemyBird1.update(timerInterval);
        enemyBird2.update(timerInterval);
        enemyBird3.update(timerInterval);
        invalidate();

        //не позволит птице игрока вылететь за пределы экрана
        if (playerBird.getY() + playerBird.getFrameHeight() >  viewHeight) {
            playerBird.setY(viewHeight -   playerBird.getFrameHeight());
            playerBird.setVy(-playerBird.getVy());
        }
        else if (playerBird.getY() < 0) {
            playerBird.setY(0);
            playerBird.setVy(-playerBird.getVy());
        }

        //Возвращение птицы противника в начальное положение осуществляется после пролета игрока
        if (enemyBird.getX() < - enemyBird.getFrameWidth()) {
            teleportEnemy (enemyBird);
            points +=10; //За облет птицы игроку начисляются очки
        }
        // Проверка столкновений
        if (enemyBird.intersect(playerBird)) {
            teleportEnemy (enemyBird);
            points -= 20; //За столкновения с птицей у игрока снимаются очки.
        }

        if (enemyBird1.getX() < - enemyBird1.getFrameWidth()) {
            teleportEnemy (enemyBird1);
            points +=10; //За облет птицы игроку начисляются очки
        }
        // Проверка столкновений
        if (enemyBird1.intersect(playerBird)) {
            teleportEnemy (enemyBird1);
            points -= 20; //За столкновения с птицей у игрока снимаются очки.
        }

        if (enemyBird2.getX() < - enemyBird2.getFrameWidth()) {
            teleportEnemy (enemyBird2);
            points +=10; //За облет птицы игроку начисляются очки
        }
        // Проверка столкновений
        if (enemyBird2.intersect(playerBird)) {
            teleportEnemy (enemyBird2);
            points -= 20; //За столкновения с птицей у игрока снимаются очки.
        }

        if (enemyBird3.getX() < - enemyBird3.getFrameWidth()) {
            teleportEnemy (enemyBird3);
            points +=10; //За облет птицы игроку начисляются очки
        }
        // Проверка столкновений
        if (enemyBird3.intersect(playerBird)) {
            teleportEnemy (enemyBird3);
            points -= 20; //За столкновения с птицей у игрока снимаются очки.
        }
    }

    // метод возвращения птицы противника после пролета
    private void teleportEnemy (Sprite enemy) {
        enemy.setX(viewWidth + Math.random() * 500);
        enemy.setY(Math.random() * (viewHeight - enemy.getFrameHeight()));
    }

        private void getLevel()
        {
            if (points < 80) {
                level = 1;
            }
            else if (points <= 120) {
                level = 2;
            }
            else if (points <= 230) {
                level = 3;
            }
            else if (points <= 500) {
                level = 4;
            }
        }

    //-------------------------Управление птицей и контроль столкновений
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();
        if (eventAction == MotionEvent.ACTION_DOWN) {
            playerBird.setVy(playerBird.getVy() *-1);
        }
        return true;
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