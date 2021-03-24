package com.github.skrox.travelally.ui.auth

import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.github.skrox.travelally.R
import com.github.skrox.travelally.TravelAllyApplication
import com.github.skrox.travelally.data.repositories.UserRepository
import com.github.skrox.travelally.databinding.ActivityLoginBinding
import com.github.skrox.travelally.di.LoginComponent
import com.github.skrox.travelally.ui.mainscreen.MainActivity
import com.github.skrox.travelally.util.ActivityNavigation
import com.github.skrox.travelally.util.hide
import com.github.skrox.travelally.util.show
import com.github.skrox.travelally.util.snackbar
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject


class LoginActivity : AppCompatActivity(), ActivityNavigation, AuthListener{


    @Inject lateinit var userRepo:UserRepository
    @Inject lateinit var mGoogleSignInClient:GoogleSignInClient
    @Inject lateinit var factory: AuthViewModelFactory

    lateinit var loginComponent:LoginComponent

    private lateinit var vm:AuthViewModel
    fun decodeSampledBitmapFromResource(
        res: Resources,
        resId: Int,
        reqWidth: Int,
        reqHeight: Int
    ): Bitmap {
        // First decode with inJustDecodeBounds=true to check dimensions
        return BitmapFactory.Options().run {
            inJustDecodeBounds = true
            BitmapFactory.decodeResource(res, resId, this)

            // Calculate inSampleSize
            inSampleSize = calculateInSampleSize(this, reqWidth, reqHeight)

            // Decode bitmap with inSampleSize set
            inJustDecodeBounds = false

            BitmapFactory.decodeResource(res, resId, this)
        }
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        // Raw height and width of image
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1
        Log.e("login",""+inSampleSize +" " + reqHeight +" "+ reqWidth)
        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getSupportActionBar()!!.hide();

//Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Creates an instance of Registration component by grabbing the factory from the app graph
        loginComponent = (application as TravelAllyApplication).appComponent.LoginComponent().create(this)
        // Injects this activity to the just created registration component
        loginComponent.inject(this)

        super.onCreate(savedInstanceState)

//        val gso =
//            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .requestIdToken(resources.getString(R.string.google_client_id))
//                .build()
//
//        val mGoogleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(this, gso);
//        val userReposiotry = UserRepository(MyApi(NetworkConnectionInterceptor(this)), AppDatabase.invoke(this),
//            PreferenceProvider(this)
//        )

        val viewModel: AuthViewModel by viewModels{factory}
        vm = viewModel

        val binding: ActivityLoginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.viewmodel = viewModel

        viewModel.authListener=this
//        viewModel.updateuser((GoogleSignIn.getLastSignedInAccount(this)));

        viewModel.getuser(this).observe(this, Observer { user->
            if (user!=null){
                Intent(this, MainActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }
            }
        })
//        Intent(this,MainActivity::class.java).also { startActivity(it) }
        subscribeUi()

//        val options = BitmapFactory.Options().apply {
//            inJustDecodeBounds = true
//        }
//        BitmapFactory.decodeResource(resources, R.raw.unnamed, options)
//        val imageHeight: Int = options.outHeight
//        val imageWidth: Int = options.outWidth
//        val imageType: String = options.outMimeType
        val imageView = findViewById<ImageView>(R.id.imageBG)
        imageView.setImageBitmap(
            decodeSampledBitmapFromResource(resources, R.raw.desert2, imageView.maxWidth, imageView.maxHeight)
        )

    }


    //Called from onCreate once the ViewModel is initialized.
    private fun subscribeUi() {
        //this sets the LifeCycler owner and receiver
        vm.startActivityForResultEvent.setEventReceiver(this, this)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        vm.onResultFromActivity(requestCode,resultCode,data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onStarted() {
        progress_circular.show()
    }

    override fun onSuccess() {
        progress_circular.hide()

    }

    override fun onFailure(msg: String) {
        progress_circular.hide()
        Log.e("auth error", msg)
        root_layout.snackbar(msg)
    }

}
