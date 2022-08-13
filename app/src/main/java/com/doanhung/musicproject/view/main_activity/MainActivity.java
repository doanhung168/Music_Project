package com.doanhung.musicproject.view.main_activity;

import android.Manifest;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.doanhung.musicproject.R;
import com.doanhung.musicproject.broadcast_receive.ChargeReceive;
import com.doanhung.musicproject.data.model.app_system_model.DeviceItem;
import com.doanhung.musicproject.data.repository.AppSystemRepository;
import com.doanhung.musicproject.databinding.ActivityMainBinding;
import com.doanhung.musicproject.di.AppModule;
import com.doanhung.musicproject.service.MusicService;
import com.doanhung.musicproject.service.MusicServiceController;
import com.doanhung.musicproject.view.common_adapter.DeviceItemAdapter;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements
        DeviceItemAdapter.OnClickDeviceItemListener,
        OnOpenNavigationViewListener {


    private static final String TAG = "MainActivity";


    private ActivityMainBinding mBinding;
    private NavController mNavController;

    private ScheduledExecutorService mScheduledExecutor;

    @Inject
    @AppModule.DeviceItemAdapterAnnotation
    DeviceItemAdapter mDeviceItemAdapter;

    @Inject
    AppSystemRepository mAppSystemRepository;
    private ExtraMainViewModel mExtraMainViewModel;

    @Inject
    MusicServiceController mMusicServiceController;
    private MainViewModel mMainViewModel;

    private ChargeReceive chargeReceive;


    private final ActivityResultLauncher<String> mRequestAccessExStoragePermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (!isGranted) {
                    Toast.makeText(this, "Permission Deny", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createView();

        initNavController();
        initAndAttackViewModels();
        setupBottomNav(mNavController);
        setupNavView();

        restoreUIWithMusicService();
        handlerMusicSeekBar();

        // request permission to read and write external storage
        requestAccessExternalStorage();

        registerChargeReceiver();

    }

    private void createView() {
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        // extra handler for obtain exactly UI
        mBinding.musicPlayerBar.seekBar.setPadding(0, 0, 0, 0);
    }

    private void initNavController() {
        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        mNavController = Objects.requireNonNull(navHostFragment).getNavController();
    }

    private void initAndAttackViewModels() {
        mExtraMainViewModel = new ViewModelProvider(this,
                new ExtraMainViewModel.ExtraMainViewModelFactory(mAppSystemRepository))
                .get(ExtraMainViewModel.class);

        mMainViewModel = new ViewModelProvider(this,
                new MainViewModel.MainViewModelFactory(getApplication(), mMusicServiceController))
                .get(MainViewModel.class);

        mBinding.setLifecycleOwner(this);
        mBinding.setViewModel(mMainViewModel);
    }

    private void setupBottomNav(NavController navController) {
        NavigationUI.setupWithNavController(mBinding.btNav, navController);
        mBinding.btNav.setItemIconTintList(null);
    }

    private void setupNavView() {
        mBinding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.START);

        mDeviceItemAdapter.setOnClickItemListener(this);
        mBinding.rcvNavViewItem.setAdapter(mDeviceItemAdapter);

        if (mExtraMainViewModel.checkHasNoNavViewData()) {
            mExtraMainViewModel.loadNavViewData();
        }

        mExtraMainViewModel.mNavViewItems.observe(this, items -> {
            if (items != null) mDeviceItemAdapter.submitList(items);
        });
    }

    private void restoreUIWithMusicService() {
        if (MusicService.isHasServiceRunning) {
            mMainViewModel.restoreUIWithService();
        }
    }

    private void handlerMusicSeekBar() {
        mMainViewModel.mCurrentSong.observe(this, song -> {
            if (song != null) {
                updateMusicSeekBar();
            } else {
                mScheduledExecutor.shutdown();
            }
        });

        mBinding.musicPlayerBar.root.setOnClickListener(v -> mNavController.navigate(R.id.musicPlayingFragment));

        mBinding.musicPlayerBar.seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    if (mMainViewModel.getMediaPlayer() != null) {
                        mMainViewModel.getMediaPlayer().seekTo(progress);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void updateMusicSeekBar() {
        mBinding.musicPlayerBar.seekBar.setMax(mMainViewModel.getMediaPlayer().getDuration());
        mScheduledExecutor = Executors.newSingleThreadScheduledExecutor();
        mScheduledExecutor.scheduleWithFixedDelay(
                this::updateProgressBar,
                0,
                999,
                TimeUnit.MILLISECONDS
        );
    }

    private void updateProgressBar() {
        int currentPos = mMainViewModel.getMediaPlayer().getCurrentPosition();
        runOnUiThread(() -> {
            if (mBinding != null) {
                mBinding.musicPlayerBar.seekBar.setProgress(currentPos);
            }
        });
    }

    private void requestAccessExternalStorage() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            mRequestAccessExStoragePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    private void registerChargeReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_POWER_CONNECTED);
        intentFilter.addAction(Intent.ACTION_POWER_DISCONNECTED);
        chargeReceive = new ChargeReceive();
        registerReceiver(chargeReceive, intentFilter);
    }

    @Override
    public void onClickDeviceItem(@NonNull DeviceItem deviceItem) {
        Toast.makeText(MainActivity.this, deviceItem.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void openOrCloseNavView() {
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.close();
        } else {
            mBinding.drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @Override
    public void onBackPressed() {
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mScheduledExecutor != null) {
            mScheduledExecutor.shutdown();
        }
        mBinding = null;
        unregisterReceiver(chargeReceive);
    }
}