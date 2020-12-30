package com.codingwithmitch.espressouitestexamples

import android.app.Activity.RESULT_OK
import android.app.Instrumentation.ActivityResult
import android.content.ContentResolver
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.provider.MediaStore
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.*
import androidx.test.espresso.intent.rule.IntentsTestRule
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.platform.app.InstrumentationRegistry
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4ClassRunner::class)
class MainActivityTest{

    @get:Rule
    val intentsTestRule = IntentsTestRule(MainActivity::class.java)

    @Test
    fun  test_validateIntentSentToPickPackage() {

        // GIVEN
        val expectedIntent: Matcher<Intent> = allOf(
            hasAction(Intent.ACTION_PICK),
            hasData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        )
        val activityResult = createGalleryPickActivityResultStub()//here we created activity result
        intending(expectedIntent).respondWith(activityResult)//we tell that we intent to receive that activity reult from the dialling expectedIntent

        // Execute and Verify
        onView(withId(R.id.button_open_gallery)).perform(click())//we execute a real click
        intended(expectedIntent)// we verify that as intended or not
    }
    //here we create a customized activity result as we don't guarantee what will be chosen from the user,
    // we set a uri of already existing launcher image in every android project
    private fun createGalleryPickActivityResultStub(): ActivityResult {
        val resources: Resources = InstrumentationRegistry.getInstrumentation().context.resources
        val imageUri = Uri.parse(
            ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                    resources.getResourcePackageName(R.drawable.ic_launcher_background) + '/' +
                    resources.getResourceTypeName(R.drawable.ic_launcher_background) + '/' +
                    resources.getResourceEntryName(R.drawable.ic_launcher_background)
        )
        val resultIntent = Intent()
        resultIntent.setData(imageUri)//here we fill the activity result intent with the image uri
        return ActivityResult(RESULT_OK, resultIntent)//here we fill the activity result with the intent
    }
}













