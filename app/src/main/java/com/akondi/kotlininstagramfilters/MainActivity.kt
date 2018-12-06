package com.akondi.kotlininstagramfilters

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.support.design.widget.Snackbar
import android.support.text.emoji.EmojiCompat
import android.support.text.emoji.bundled.BundledEmojiCompatConfig
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.akondi.kotlininstagramfilters.Adapter.ViewPagerAdapter
import com.akondi.kotlininstagramfilters.Fragments.*
import com.akondi.kotlininstagramfilters.Interface.*
import com.akondi.kotlininstagramfilters.Utils.BitmapUtils
import com.akondi.kotlininstagramfilters.Utils.NoSwipeableViewPager
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.yalantis.ucrop.UCrop
import com.zomato.photofilters.imageprocessors.Filter
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter
import ja.burhanrashid52.photoeditor.OnSaveBitmap
import ja.burhanrashid52.photoeditor.PhotoEditor
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.File
import java.lang.Exception
import java.lang.StringBuilder
import java.util.*

class MainActivity : AppCompatActivity(), FilterListFragmentListener, EditImageFragmentListener, BrushFragmentListener,
    EmojiFragmentListener, AddTextFragmentListener, AddFrameFragmentListener {

    override fun onFrameSelected(frame: Int) {
        val bitmap = BitmapFactory.decodeResource(resources, frame)
        photoEditor.addImage(bitmap)
    }

    override fun onAddTextListener(typeface: Typeface, text: String, color: Int) {
        photoEditor.addText(typeface, text, color)
    }

    override fun onEmojiSelected(emoji: String) {
        photoEditor.addEmoji(emoji)
    }

    override fun onBrushSizeChangedListener(size: Float) {
        photoEditor.brushSize = size
    }

    override fun onBrushOpacityChangedListener(size: Int) {
        photoEditor.setOpacity(size)
    }

    override fun onBrushColorChangedListener(color: Int) {
        photoEditor.brushColor = color
    }

    override fun onBrushStateChangedListener(isEraser: Boolean) {
        if (isEraser)
            photoEditor.brushEraser()
        else
            photoEditor.setBrushDrawingMode(true)
    }

    val SELECT_GALLERY_PERMISSION = 1000
    val PICK_IMAGE_GALLERY_PERMISSION = 1001


    internal var originalImage: Bitmap? = null
    internal lateinit var filteredImage: Bitmap
    internal lateinit var finalImage: Bitmap

    internal lateinit var filterListFragment: FilterListFragment
    internal lateinit var editImageFragment: EditImageFragment
    internal lateinit var brushFragment: BrushFragment
    internal lateinit var emojiFragment: EmojiFragment
    internal lateinit var addTextFragment: AddTextFragment
    internal lateinit var addFrameFragment: AddFrameFragment

    internal var brightnessFinal = 0
    internal var saturationFinal = 1.0f
    internal var contrastFinal = 1.0f

    lateinit var photoEditor: PhotoEditor

    internal var image_selected_uri: Uri? = null
    internal var imageUri: Uri? = null
    internal val CAMERA_REQUEST: Int = 9999

    object Main {
        const val IMAGE_NAME = "flash.jpg"
    }

    init {
        System.loadLibrary("NativeImageProcessor")
    }

    override fun onBrightnessChanged(brightness: Int) {
        brightnessFinal = brightness
        val myFilter = Filter()
        myFilter.addSubFilter(BrightnessSubFilter(brightness))
        image_preview.source.setImageBitmap(myFilter.processFilter(finalImage.copy(Bitmap.Config.ARGB_8888, true)))
    }

    override fun onSaturationChanged(saturation: Float) {
        saturationFinal = saturation
        val myFilter = Filter()
        myFilter.addSubFilter(SaturationSubfilter(saturation))
        image_preview.source.setImageBitmap(myFilter.processFilter(finalImage.copy(Bitmap.Config.ARGB_8888, true)))
    }

    override fun onContrastChanged(contrast: Float) {
        contrastFinal = contrast
        val myFilter = Filter()
        myFilter.addSubFilter(ContrastSubFilter(contrast))
        image_preview.source.setImageBitmap(myFilter.processFilter(finalImage.copy(Bitmap.Config.ARGB_8888, true)))
    }

    override fun onEditStarted() {
    }

    override fun onEditCompleted() {
        val bitmap = filteredImage.copy(Bitmap.Config.ARGB_8888, true)
        val myFilter = Filter()
        myFilter.addSubFilter(ContrastSubFilter(contrastFinal))
        myFilter.addSubFilter(SaturationSubfilter(saturationFinal))
        myFilter.addSubFilter(BrightnessSubFilter(brightnessFinal))
        finalImage = myFilter.processFilter(bitmap)
    }

    override fun onFilterSelected(filter: Filter) {
        //resetControls()
        filteredImage = originalImage!!.copy(Bitmap.Config.ARGB_8888, true)
        image_preview.source.setImageBitmap(filter.processFilter(filteredImage))
        finalImage = filteredImage.copy(Bitmap.Config.ARGB_8888, true)
    }

    private fun resetControls() {
        if (editImageFragment != null) {
            editImageFragment.resetControls()
        }
        brightnessFinal = 0
        saturationFinal = 1.0f
        contrastFinal = 1.0f
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupPhotoEditor()
        loadImage()
        setupEditImageFragment()
        setupFilterListFragment()
        setupBrushFragment()
        setupEmojiFragment()
        setupAddTextFragment()
        setupAddImageButton()
        setupAddFrameFragment()
        setupCropButton()
        setUpActionBar()

        val config = BundledEmojiCompatConfig(this)
        EmojiCompat.init(config)
    }

    private fun setupFilterListFragment() {
        filterListFragment = FilterListFragment.getInstance(null)
        btn_filters.setOnClickListener {
            if (filterListFragment != null) {
                filterListFragment.setListener(this@MainActivity)
                filterListFragment.show(supportFragmentManager, filterListFragment.tag)
            } else {
                filterListFragment.setListener(this@MainActivity)
                filterListFragment.show(supportFragmentManager, filterListFragment.tag)

            }
        }
    }

    private fun setupEditImageFragment() {
        editImageFragment = EditImageFragment.getInstance()
        btn_edit.setOnClickListener {
            if (editImageFragment != null) {
                editImageFragment.setListener(this@MainActivity)
                editImageFragment.show(supportFragmentManager, editImageFragment.tag)
            }
        }
    }

    private fun setupBrushFragment() {
        brushFragment = BrushFragment.getInstance()
        btn_brush.setOnClickListener {
            if (brushFragment != null) {
                photoEditor.setBrushDrawingMode(true)
                brushFragment.setListener(this@MainActivity)
                brushFragment.show(supportFragmentManager, brushFragment.tag)
            }
        }
    }

    private fun setupEmojiFragment() {
        emojiFragment = EmojiFragment.getInstance()
        btn_emoji.setOnClickListener {
            if (emojiFragment != null) {
                emojiFragment.setListener(this@MainActivity)
                emojiFragment.show(supportFragmentManager, emojiFragment.tag)
            }
        }
    }

    private fun setupAddTextFragment() {
        addTextFragment = AddTextFragment.getInstance()
        btn_add_text.setOnClickListener {
            if (addTextFragment != null) {
                addTextFragment.setListener(this@MainActivity)
                addTextFragment.show(supportFragmentManager, addTextFragment.tag)
            }
        }
    }

    private fun setupAddImageButton() {
        btn_add_image.setOnClickListener {
            addImageToPicture()
        }
    }

    private fun setupAddFrameFragment() {
        addFrameFragment = AddFrameFragment.getInstance()
        btn_add_frame.setOnClickListener {
            if (addFrameFragment != null) {
                addFrameFragment.setListener(this@MainActivity)
                addFrameFragment.show(supportFragmentManager, addFrameFragment.tag)
            }
        }
    }

    private fun setupCropButton() {
        btn_crop.setOnClickListener {
            startCrop(image_selected_uri)
        }
    }

    private fun startCrop(uri: Uri?) {
        var destinationFileName = StringBuilder(UUID.randomUUID().toString()).append(".jpg").toString()

        var uCrop = UCrop.of(uri!!, Uri.fromFile(File(cacheDir, destinationFileName)))

        uCrop.start(this@MainActivity)

    }

    private fun addImageToPicture() {
        Dexter.withActivity(this@MainActivity)
            .withPermissions(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        val intent = Intent(Intent.ACTION_PICK)
                        intent.type = "image/*"
                        startActivityForResult(intent, PICK_IMAGE_GALLERY_PERMISSION)
                    } else {
                        Toast.makeText(applicationContext, "Permission denied", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token!!.continuePermissionRequest()
                }
            }).check()
    }

    private fun setupPhotoEditor() {
        photoEditor = PhotoEditor.Builder(this@MainActivity, image_preview)
            .setPinchTextScalable(true)
            .setDefaultEmojiTypeface(Typeface.createFromAsset(assets, "emojione-android.ttf"))
            .build()
    }

    private fun setupViewPager(viewPager: NoSwipeableViewPager?) {
        val adapter = ViewPagerAdapter(supportFragmentManager)

        // add filter list fragment
        filterListFragment = FilterListFragment()
        filterListFragment.setListener(this)

        // add edit image fragment
        editImageFragment = EditImageFragment()
        editImageFragment.setListener(this)

        adapter.addFragment(filterListFragment, "FILTERS")
        adapter.addFragment(editImageFragment, "EDIT")
        viewPager!!.adapter = adapter
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolBar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Instagram Filter"
    }

    private fun loadImage() {
        originalImage = BitmapUtils.getBitmapFromAssets(this, Main.IMAGE_NAME, 300, 300)
        filteredImage = originalImage!!.copy(Bitmap.Config.ARGB_8888, true)
        finalImage = originalImage!!.copy(Bitmap.Config.ARGB_8888, true)
        image_preview.source.setImageBitmap(originalImage)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val id = item!!.itemId
        if (id == R.id.action_open) {
            openImageFromGallery()
            return true
        } else if (id == R.id.action_save) {
            saveImageToGallery()
            return true
        } else if (id == R.id.action_camera) {
            openCamera()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openCamera() {
        Dexter.withActivity(this)
            .withPermissions(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {

                        var values = ContentValues()
                        values.put(MediaStore.Images.Media.TITLE, "New Picture")
                        values.put(MediaStore.Images.Media.DESCRIPTION, "From Camera")
                        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

                        var cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                        startActivityForResult(cameraIntent, CAMERA_REQUEST)

                    } else
                        Toast.makeText(applicationContext, "Permission denied", Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token!!.continuePermissionRequest()
                }
            }).check()
    }

    private fun saveImageToGallery() {
        Dexter.withActivity(this)
            .withPermissions(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        saveImageFromPhotoEditor()
                    } else
                        Toast.makeText(applicationContext, "Permission denied", Toast.LENGTH_SHORT).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token!!.continuePermissionRequest()
                }
            }).check()
    }

    private fun saveImageFromPhotoEditor() {
        photoEditor.saveAsBitmap(object : OnSaveBitmap {

            override fun onFailure(e: Exception?) {
                val snackBar = Snackbar.make(coordinator, e!!.message.toString(), Snackbar.LENGTH_LONG)
                snackBar.show()
            }

            override fun onBitmapReady(saveBitmap: Bitmap?) {
                saveImageToPath(saveBitmap)
            }
        })
    }

    private fun saveImageToPath(saveBitmap: Bitmap?) {
        val path = BitmapUtils.insertImage(
            contentResolver,
            saveBitmap,
            System.currentTimeMillis().toString() + "_profile.jpg",
            ""
        )

        if (!TextUtils.isEmpty(path)) {
            val snackBar = Snackbar
                .make(coordinator, "Image saved to Gallery", Snackbar.LENGTH_LONG)
                .setAction("OPEN") {
                    openImage(path)
                }
            snackBar.show()
            image_preview.source.setImageBitmap(saveBitmap)
        } else {
            val snackBar = Snackbar.make(coordinator, "Unable to save image", Snackbar.LENGTH_LONG)
            snackBar.show()
        }
    }

    private fun openImage(path: String?) {
        // We will use Dexter library to request runtime permission and process
        Dexter.withActivity(this)
            .withPermissions(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        val intent = Intent()
                        intent.setDataAndType(Uri.parse(path), "image/*")
                        startActivity(intent)
                    } else {
                        Toast.makeText(applicationContext, "Permission denied", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token!!.continuePermissionRequest()
                }

            }).check()
    }

    private fun openImageFromGallery() {
        // We will use Dexter library to request runtime permission and process
        Dexter.withActivity(this)
            .withPermissions(
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if (report!!.areAllPermissionsGranted()) {
                        val intent = Intent(Intent.ACTION_PICK)
                        intent.type = "image/*"
                        startActivityForResult(intent, SELECT_GALLERY_PERMISSION)
                    } else {
                        Toast.makeText(applicationContext, "Permission denied", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    token!!.continuePermissionRequest()
                }

            }).check()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_GALLERY_PERMISSION) {
                var bitmap = BitmapUtils.getBitmapFromGallery(this, data!!.data, 800, 800)

                image_selected_uri = data.data!!

                // clear bitmap memory
                originalImage = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                filteredImage = originalImage!!.copy(Bitmap.Config.ARGB_8888, true)
                finalImage = originalImage!!.copy(Bitmap.Config.ARGB_8888, true)

                image_preview.source.setImageBitmap(originalImage)
                bitmap.recycle()

                filterListFragment = FilterListFragment.getInstance(originalImage!!)
                filterListFragment.setListener(this)

            } else if (requestCode == CAMERA_REQUEST) {
                var bitmap = BitmapUtils.getBitmapFromGallery(this, imageUri!!, 800, 800)

                image_selected_uri = imageUri

                // clear bitmap memory
                originalImage = bitmap.copy(Bitmap.Config.ARGB_8888, true)
                filteredImage = originalImage!!.copy(Bitmap.Config.ARGB_8888, true)
                finalImage = originalImage!!.copy(Bitmap.Config.ARGB_8888, true)

                image_preview.source.setImageBitmap(originalImage)
                bitmap.recycle()

                filterListFragment = FilterListFragment.getInstance(originalImage!!)
                filterListFragment.setListener(this)

            } else if (requestCode == PICK_IMAGE_GALLERY_PERMISSION) {
                var bitmap = BitmapUtils.getBitmapFromGallery(this, data!!.data, 200, 200)
                photoEditor.addImage(bitmap)

            } else if (requestCode == UCrop.REQUEST_CROP)
                handleCropRequest(data)

        } else if (resultCode == UCrop.RESULT_ERROR)
            handleCropError(data)
    }

    private fun handleCropError(data: Intent?) {
        var cropError = UCrop.getError(data!!)
        if (cropError != null) {
            Toast.makeText(this@MainActivity, cropError.message, Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this@MainActivity, "Unexpected Error", Toast.LENGTH_SHORT).show()
        }
    }

    private fun handleCropRequest(data: Intent?) {
        var resultUri = UCrop.getOutput(data!!)

        if (resultUri != null) {
            image_preview.source.setImageURI(resultUri)
            val bitmap = (image_preview.source.drawable as BitmapDrawable).bitmap
            originalImage = bitmap.copy(Bitmap.Config.ARGB_8888, true)
            filteredImage = originalImage!!
            finalImage = originalImage!!

        } else
            Toast.makeText(this@MainActivity, "Cannot retrieve crop image", Toast.LENGTH_SHORT).show()
    }
}
