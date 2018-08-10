package id.co.imastudio.mengenalprofesi;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.eftimoff.viewpagertransformers.ZoomOutSlideTransformer;

public class PlayActivity extends AppCompatActivity {

    private ViewPager viewPager;

    private int[] chapter1 = {
            R.drawable.profesi01,
            R.drawable.profesi02,
            R.drawable.profesi03,
            R.drawable.profesi04,
            R.drawable.profesi05,
            R.drawable.profesi06,
            R.drawable.profesi07,
            R.drawable.profesi08
    };


    private int[] suarabilal = {
            R.raw.harab01,
            R.raw.harab02,
            R.raw.harab03,
            R.raw.harab04,
            R.raw.harab05,
            R.raw.harab06,
            R.raw.harab07,
            R.raw.harab08
    };

    //    private int[] textAdab = {
//            R.drawable.text01,
//            R.drawable.text02,
//            R.drawable.text03,
//            R.drawable.text04,
//            R.drawable.text05,
//            R.drawable.text06,
//            R.drawable.text07,
//            R.drawable.text08,
//            R.drawable.text09
//    };
    private ImageView btnPlayOption;
    private ImageView btnPlayHome;
    private ImageView btnPlayBack;
    private ImageView btnPlayNext;
    private ImageView btnPlayAuto;

    private int page = 0;
    private ImageView btnOn;
    private String settinganSound;
    AudioManager audioManager;
    private int currentPage;

    ViewPagerAdapter pageradapter;
    private Handler handler;
    private Runnable run;
    private Button btnHome;
    private Button btnRestart;
    private String mode = "auto";
    ;
    private boolean auto = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        int pilihan = getIntent().getIntExtra("posisi", 0);

        initView();

//        YoYo.with(Techniques.FlipInX)
//                .duration(3000)
//                .repeat(1000)
//                .playOn(viewPager);

        pageradapter = new ViewPagerAdapter(this, chapter1, suarabilal);

        viewPager.setAdapter(pageradapter);
        viewPager.setPageTransformer(true, new ZoomOutSlideTransformer());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
//                Toast.makeText(PlayActivity.this, "posisi"+position, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        btnPlayNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound();

                if (currentPage == pageradapter.getCount() - 1) {
//                    Toast.makeText(PlayActivity.this, "Tes Halaman Terakhir", Toast.LENGTH_SHORT).show();
                    final Dialog dialog = new Dialog(PlayActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.setContentView(R.layout.dialog_hebat);
                    dialog.show();

                    MediaPlayer player = MediaPlayer.create(PlayActivity.this, R.raw.suarabilalhebat);
                    player.start();

                    btnHome = (Button) dialog.findViewById(R.id.btn_home);
                    btnHome.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //ngambil data
                            dialog.dismiss();
                            Intent intent = new Intent(PlayActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                                finishAffinity();
                            }
                        }
                    });

                    btnRestart = (Button) dialog.findViewById(R.id.btn_restart);
                    btnRestart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //ngambil data
                            dialog.dismiss();
                            viewPager.setCurrentItem(0);
                        }
                    });
                }


                viewPager.arrowScroll(View.FOCUS_RIGHT);

            }
        });

        btnPlayBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound();
                viewPager.arrowScroll(View.FOCUS_LEFT);
            }
        });


        btnPlayOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound();
                viewPager.setCurrentItem(0);
            }
        });


        btnPlayHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PlayActivity.this, MainActivity.class));
                finish();
            }
        });

        btnPlayAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound();
                if (auto) {
                    viewPager.postDelayed(run, 0);
                    viewPager.removeCallbacks(run);
                    mode = "auto";
                    auto = false;
                    btnPlayAuto.setImageResource(R.drawable.button_auto);
                } else {
                    run = new Runnable() {
                        @Override
                        public void run() {
                            if (pageradapter.getCount() - 1 == page) {
                                page = 0;
                            } else {
                                page++;
                            }
                            viewPager.arrowScroll(View.FOCUS_RIGHT);
                            viewPager.postDelayed(run, 3000);
                        }
                    };
                    viewPager.postDelayed(run, 0);
                    auto = true;
                    mode = "manual";
                    btnPlayAuto.setImageResource(R.drawable.button_manual);
                }

            }
        });
    }

    private void playSound() {
        MediaPlayer player = MediaPlayer.create(PlayActivity.this, R.raw.sfx_button);
        player.start();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        btnPlayHome = (ImageView) findViewById(R.id.btnPlayHome);
        btnPlayOption = (ImageView) findViewById(R.id.btnPlayRestart);
        btnPlayAuto = (ImageView) findViewById(R.id.btnPlayAuto);
        btnPlayBack = (ImageView) findViewById(R.id.btnPlayBack);
        btnPlayNext = (ImageView) findViewById(R.id.btnPlayNext);
    }
}
