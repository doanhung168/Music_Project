package com.doanhung.musicproject.view;

import static com.doanhung.musicproject.util.Constraint.LOAD_MUSIC_CURSOR_ID;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.doanhung.musicproject.R;
import com.doanhung.musicproject.data.model.DeviceItem;
import com.doanhung.musicproject.data.repository.ResourceRepository;
import com.doanhung.musicproject.databinding.ActivityMainBinding;
import com.doanhung.musicproject.view.adapter.DeviceItemAdapter;
import com.doanhung.musicproject.viewmodel.ResourceViewModel;

import java.util.Objects;
import java.util.concurrent.Executor;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements
        DeviceItemAdapter.OnClickDeviceItemListener,
        OnOpenNavigationViewListener,
        LoaderManager.LoaderCallbacks<Cursor> {


    private ActivityMainBinding mBinding;

    private Cursor mFilterCursor = new MatrixCursor(new String[]{});

    @Inject
    Executor mExecutor;

    @Inject
    DeviceItemAdapter mDeviceItemAdapter;

    @Inject
    ResourceRepository resourceRepository;
    private ResourceViewModel mResourceViewModel;

    private final ActivityResultLauncher<String> mRequestReadAndWriteExStoragePermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    LoaderManager.getInstance(this)
                            .initLoader(LOAD_MUSIC_CURSOR_ID, null, this);
                } else {
                    Toast.makeText(MainActivity.this, "Permission Deny", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        setupToTakeMusic();


        mResourceViewModel =
                new ViewModelProvider(
                        this,
                        new ResourceViewModel.ResourceViewModelFactory(resourceRepository))
                        .get(ResourceViewModel.class);

        NavHostFragment navHostFragment =
                (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = Objects.requireNonNull(navHostFragment).getNavController();

        setupBottomNav(navController);
        setupNavView();

    }

    private void setupToTakeMusic() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            LoaderManager.getInstance(this)
                    .initLoader(LOAD_MUSIC_CURSOR_ID, null, this);
        } else {
            mRequestReadAndWriteExStoragePermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private void setupBottomNav(NavController navController) {
        NavigationUI.setupWithNavController(mBinding.btNav, navController);
        mBinding.btNav.setItemIconTintList(null);
    }

    private void setupNavView() {
        mDeviceItemAdapter.setOnClickItemListener(this);
        mBinding.rcvNavViewItem.setLayoutManager(new LinearLayoutManager(this));
        mBinding.rcvNavViewItem.setAdapter(mDeviceItemAdapter);

        mResourceViewModel.loadDataForNavView();
        mResourceViewModel.mNavViewItems.observe(this, items -> {
            if (items != null) mDeviceItemAdapter.submitList(items);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBinding = null;
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
    public void openOrCloseNavView() {
        if (mBinding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            mBinding.drawerLayout.close();
        } else {
            mBinding.drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if (id == LOAD_MUSIC_CURSOR_ID) {

            String[] projection = {
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.DISPLAY_NAME,
                    MediaStore.Audio.Media.DURATION,
                    MediaStore.Audio.Media.ALBUM_ID
            };

            String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";

            return new CursorLoader(
                    MainActivity.this,
                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                    projection,
                    selection,
                    null,
                    null
            );
        }
        throw new IllegalArgumentException("ID of the loader is not supported");
    }

    private static final String TAG = "MainActivity";

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor cursor) {
        mExecutor.execute(() -> {
            if (cursor != mFilterCursor) {
                mFilterCursor = cursor;
                while (cursor.moveToNext()) {
                    Log.e(TAG, cursor.getString(0) + "||" + cursor.getString(1) + "||" + cursor.getString(2) + "||" + cursor.getString(3) + "||" + cursor.getString(4) + "||" + cursor.getString(5) + "||" + cursor.getString(6));
                }
            }
        });

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }

    @Override
    public void onClickDeviceItem(DeviceItem deviceItem) {
        Toast.makeText(MainActivity.this, deviceItem.getName(), Toast.LENGTH_SHORT).show();
    }
}